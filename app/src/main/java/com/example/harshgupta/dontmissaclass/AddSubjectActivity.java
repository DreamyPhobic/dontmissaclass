package com.example.harshgupta.dontmissaclass;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;

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
    private Button mAddBtn;

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
        mAddBtn = (Button) findViewById(R.id.addNewSubjectButton);

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save Subject method
                saveSubject();
            }
        });

    }

    private void saveSubject() {
        String name = mNameEditText.getText().toString().trim();
        subjectname=name;
        String present = mPresentEditText.getText().toString().trim();
        String absent = mAbsentEditText.getText().toString().trim();
        String image = mImageEditText.getText().toString().trim();
        dbHelper = new SubjectDBhelper(this);

        if (name.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (present.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter an age", Toast.LENGTH_SHORT).show();
            return;
        }

        if (absent.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image.isEmpty()) {
            //error name is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
            return;
        }
        new downloadImage().execute(image,null,null);
        int presentInt = parseInt(present);
        int absentInt = parseInt(absent);
        int totalInt = presentInt + absentInt;

        //create new Subject
        Subject Subject = new Subject(name, presentInt, absentInt, totalInt, image);
        dbHelper.saveNewSubject(Subject);
        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();
    }

    private class downloadImage extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String cacheDir = "/storage/emulated/0/DMC_Images";
            Log.d("path",cacheDir);
            //String dirPath = Utils.getRootDirPath(getApplicationContext());
            String site=strings[0];

            int downloadId = PRDownloader.download(site, cacheDir,subjectname).build()
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Toast.makeText(AddSubjectActivity.this,"Download Complete",Toast.LENGTH_SHORT).show();
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
        finish();
    }
}