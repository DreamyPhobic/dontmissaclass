package com.example.harshgupta.dontmissaclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harshgupta.dontmissaclass.MainActivity;
import com.example.harshgupta.dontmissaclass.R;
import com.squareup.picasso.Picasso;

import java.io.File;


public class DetailActivity extends AppCompatActivity {

    private TextView mNameTextView;
    private TextView mPresentTextView;
    private TextView mAbsentTextView;
    private TextView mTotalTextView;
    private ImageView mImageView;

    private SubjectDBhelper dbHelper;
    private int receivedSubjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNameTextView = (TextView) findViewById(R.id.tv_subjectname);
        mPresentTextView = (TextView) findViewById(R.id.tv_presentCount);
        mAbsentTextView = (TextView) findViewById(R.id.tv_absentCount);
        mTotalTextView = (TextView) findViewById(R.id.tv_totalCount);
        mImageView = (ImageView) findViewById(R.id.i_subjectimage);
        Button mPresent = (Button) findViewById(R.id.b_present);
        Button mAbsent = (Button) findViewById(R.id.b_absent);
        Button mHome = (Button) findViewById(R.id.b_home);

        dbHelper = new SubjectDBhelper(this);

        try {
            //get intent to get Subject id
            receivedSubjectId = getIntent().getIntExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        Subject queriedSubject = dbHelper.getSubject(receivedSubjectId);
        //set field to this user data
        mNameTextView.setText(queriedSubject.getName());
        mPresentTextView.setText(String.valueOf(queriedSubject.getPresent()));
        mAbsentTextView.setText(String.valueOf(queriedSubject.getAbsent()));
        mTotalTextView.setText(String.valueOf(queriedSubject.getTotal()));
        if(!queriedSubject.getImage().isEmpty()){
            File file = new File("/storage/emulated/0/DMC_Images"+queriedSubject.getName());
            if(!file.exists()){
                Picasso.with(DetailActivity.this).load(queriedSubject.getImage()).placeholder(R.mipmap.ic_launcher).into(mImageView);
            }
            else
                Picasso.with(DetailActivity.this).load(file).placeholder(R.mipmap.ic_launcher).into(mImageView);
        }
       // Picasso.with(DetailActivity.this).load(queriedSubject.getImage()).placeholder(R.mipmap.ic_launcher).into(mImageView);


        //listen to add button click to update
        mPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save Subject method
                Subject temp = dbHelper.getSubject(receivedSubjectId);
                temp.setPresent(temp.getPresent() + 1);
                temp.setTotal(temp.getTotal() + 1);
                dbHelper.updateSubjectRecord(receivedSubjectId, DetailActivity.this, temp);
                refresh();
            }
        });

        mAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject temp = dbHelper.getSubject(receivedSubjectId);
                temp.setAbsent(temp.getAbsent() + 1);
                temp.setTotal(temp.getTotal() + 1);
                dbHelper.updateSubjectRecord(receivedSubjectId, DetailActivity.this, temp);
                refresh();
            }
        });
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(DetailActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void refresh() {
        Subject queriedSubject = dbHelper.getSubject(receivedSubjectId);
        //set field to this user data
        mNameTextView.setText(queriedSubject.getName());
        mPresentTextView.setText(String.valueOf(queriedSubject.getPresent()));
        mAbsentTextView.setText(String.valueOf(queriedSubject.getAbsent()));
        mTotalTextView.setText(String.valueOf(queriedSubject.getTotal()));
        Picasso.with(DetailActivity.this).load(queriedSubject.getImage()).placeholder(R.mipmap.ic_launcher).into(mImageView);
    }


}