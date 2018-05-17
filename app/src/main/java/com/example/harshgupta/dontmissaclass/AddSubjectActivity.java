package com.example.harshgupta.dontmissaclass;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;

import static java.lang.Integer.parseInt;

/**
 * Created by Harsh Gupta on 04-Mar-18.
 */


public class AddSubjectActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPresentEditText;
    private EditText mAbsentEditText;
    private EditText mImageEditText;
    private Button mPreviewBtn;
    private Button mAddBtn;
    ImageView SubjectImageImgV;

    private SubjectDBhelper dbHelper;
    String subjectname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_subject);

        //init
        mNameEditText = (EditText) findViewById(R.id.subjectName);
        mPresentEditText = (EditText) findViewById(R.id.Present);
        mAbsentEditText = (EditText) findViewById(R.id.Absent);
        mImageEditText = (EditText) findViewById(R.id.subjectImageLink);
        mPreviewBtn = (Button) findViewById(R.id.preview_image);
        mAddBtn = (Button) findViewById(R.id.addNewSubjectButton);
        SubjectImageImgV=(ImageView) findViewById(R.id.iv_imagePreview);

        //listen to add button click
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save Subject method
                preview();

            }
        });
        mAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean res =saveSubject();
                if(res)
                   goBackHome();
            }
        });

    }

    private boolean saveSubject() {
        String name = mNameEditText.getText().toString().trim();
        subjectname=name;
        String present = mPresentEditText.getText().toString().trim();
        String absent = mAbsentEditText.getText().toString().trim();
        String image = mImageEditText.getText().toString().trim();
        dbHelper = new SubjectDBhelper(this);

        if (name.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (present.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter presents till now", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (absent.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter absents till now", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (image.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
            return false;
        }
        int presentInt = parseInt(present);
        int absentInt = parseInt(absent);
        int totalInt = presentInt + absentInt;

        //create new Subject
        Subject Subject = new Subject(name, presentInt, absentInt, totalInt, image);
        dbHelper.saveNewSubject(Subject);
        return true;
        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete

    }
    private void preview(){
        String image = mImageEditText.getText().toString().trim();
        new downloadImage().execute(image,null,null);
    }

    private class downloadImage extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String cacheDir = "/storage/emulated/0/DMC_Images";
            Log.d("path",cacheDir);
            //String dirPath = Utils.getRootDirPath(getApplicationContext());
            final String site=strings[0];
            final String name = mNameEditText.getText().toString().trim();
            int downloadId = PRDownloader.download(site, getCacheDir().toString(),name).build()
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Toast.makeText(AddSubjectActivity.this,"Download Complete",Toast.LENGTH_SHORT).show();
                            if(!(site.isEmpty())){
                                File file = new File(getCacheDir().toString()+"/"+name);
                                Context mContext=getBaseContext();
                                if(!file.exists()){
                                    Picasso.with(mContext).load(site).placeholder(R.mipmap.ic_launcher).into(SubjectImageImgV);
                                    Toast.makeText(mContext,"Loaded from internet",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Picasso.with(mContext).load(file).placeholder(R.mipmap.ic_launcher).into(SubjectImageImgV);
                                    Toast.makeText(mContext,"Loaded from file",Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(AddSubjectActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    });
            return null;
        }
    }

    private void goBackHome() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}