package com.example.harshgupta.dontmissaclass;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SubjectDBhelper dbHelper;
    private SubjectAdapter adapter;
    private String filter = "";
    ItemTouchHelper helper;
    List<Subject> Dataset;
    FloatingActionButton fab;
    boolean isNotificationOn=true;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref=getPreferences(Context.MODE_PRIVATE);
        isNotificationOn=sharedPref.getBoolean("NotificationStatus",true);
        if(isNotificationOn){

            Intent serviceIntent=new Intent("com.example.harshgupta.dontmissaclass.MyService");
            serviceIntent.setPackage(getPackageName());
            this.startService(serviceIntent);
        }
        else{
            Intent serviceIntent=new Intent("com.example.harshgupta.dontmissaclass.MyService");
            serviceIntent.setPackage(getPackageName());
            this.stopService(serviceIntent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbHelper = new SubjectDBhelper(this);
        Dataset=dbHelper.subjectList(filter);
        if(Dataset.isEmpty()){
            dbHelper.update();
        }
        setAdapter();
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        helper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//               int from=viewHolder.getAdapterPosition();
//               int to=target.getAdapterPosition();

                Subject temp1=Dataset.get(viewHolder.getAdapterPosition());
                Subject temp2=Dataset.get(target.getAdapterPosition());

                int dbto=temp2.getID();
                int dbfrom=temp1.getID();
                //temp1.setID(to);
                //temp2.setID(from);
                dbHelper.updateSubjectRecord(dbto,MainActivity.this,temp1);
                dbHelper.updateSubjectRecord(dbfrom,MainActivity.this,temp2);
                Dataset=dbHelper.subjectList(filter);
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                //adapter.notifyDataSetChanged();
               // setAdapter();
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int idk=viewHolder.getAdapterPosition();
                final Subject temp=Dataset.get(idk);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.delete_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Delete the Subject

                                Log.d("idtoremove",String.valueOf(idk));
                                Subject temp=Dataset.get(idk);
                                int resId=temp.getID();

                                Subject subject=dbHelper.getSubject(resId);
                                Log.d("idtoRemoveinDB",String.valueOf(resId));
                                dbHelper.deleteSubjectRecord(resId,MainActivity.this);
                                Dataset.remove(idk);
                                Log.d("totalitems",String.valueOf(dbHelper.getallCount()));
                                adapter.notifyItemRemoved(idk);
                                //adapter.notifyDataSetChanged();
//                               Log.d("totalitems",String.valueOf(dbHelper.getSubjectsCount()));
//                               mAdapter.notifyItemRangeChanged(0,dbHelper.getSubjectsCount());

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();


            }
        });
        helper.attachToRecyclerView(mRecyclerView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundColor(getResources().getColor(R.color.afterClick));
                Intent intent = new Intent(MainActivity.this, AddSubjectActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                finish();
            }
        });
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item= menu.findItem(R.id.notification);
        if(isNotificationOn){
            item.setIcon(R.drawable.ic_star_black_24dp);
        }
        else
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {

            if(!item.isChecked()){
                item.setChecked(true);
                item.setIcon(R.drawable.ic_star_black_24dp);
                isNotificationOn=true;
                Intent serviceIntent=new Intent("com.example.harshgupta.dontmissaclass.MyService");
                serviceIntent.setPackage(getPackageName());
                this.startService(serviceIntent);
                //scheduleNotification(getNotification("Review Today's Attendence"),1);


                }
            else{
                item.setChecked(false);
                item.setIcon(R.drawable.ic_star_border_black_24dp);
                isNotificationOn=false;
                Intent serviceIntent=new Intent("com.example.harshgupta.dontmissaclass.MyService");
                serviceIntent.setPackage(getPackageName());
                this.stopService(serviceIntent);
                //scheduleNotification(getNotification("Review Today's Attendence"),0);
            }

            editor= sharedPref.edit();
            editor.putBoolean("NotificationStatus", isNotificationOn);
            editor.apply();
            return true;
        }

        return false;
    }



    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();

    }



    public void setAdapter(){
        adapter = new SubjectAdapter(Dataset, this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        Log.d("totalEntries",String.valueOf(dbHelper.getallCount()));
    }





}
