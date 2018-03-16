package com.example.harshgupta.dontmissaclass;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Harsh Gupta on 04-Mar-18.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;;
import android.content.ContentValues;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;


class SubjectDBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "subjects.db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "Subjects";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_Subject_NAME = "name";
    private static final String COLUMN_Subject_Present = "present";
    private static final String COLUMN_Subject_Absent = "absent";
    private static final String COLUMN_Subject_Total = "total";
    private static final String COLUMN_Subject_IMAGE = "image";
    private Toast m_currentToast;



    SubjectDBhelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Subject_NAME + " TEXT NOT NULL, " +
                COLUMN_Subject_Present + " INTEGER NOT NULL, " +
                COLUMN_Subject_Absent + " INTEGER NOT NULL, " +
                COLUMN_Subject_Total + " INTEGER NOT NULL, "+
                COLUMN_Subject_IMAGE + " BLOB NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    void saveNewSubject(Subject Subject) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_Subject_NAME, Subject.getName());
        values.put(COLUMN_Subject_Present, Subject.getPresent());
        values.put(COLUMN_Subject_Absent, Subject.getAbsent());
        values.put(COLUMN_Subject_Total,Subject.getTotal());
        values.put(COLUMN_Subject_IMAGE, Subject.getImage());


        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    List<Subject>subjectList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Subject> SubjectLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Subject Subject;

        if (cursor.moveToFirst()) {
            do {
                Subject = new Subject();

                Subject.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                Subject.setName(cursor.getString(cursor.getColumnIndex(COLUMN_Subject_NAME)));
                Subject.setPresent(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Present)));
                Subject.setAbsent(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Absent)));
                Subject.setTotal(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Total)));
                Subject.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_Subject_IMAGE)));
                SubjectLinkedList.add(Subject);
            } while (cursor.moveToNext());
        }


        return SubjectLinkedList;
    }

    /**Query only 1 record**/
    Subject getSubject(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Subject receivedSubject = new Subject();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            receivedSubject.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            receivedSubject.setName(cursor.getString(cursor.getColumnIndex(COLUMN_Subject_NAME)));
            receivedSubject.setPresent(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Present)));
            receivedSubject.setAbsent(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Absent)));
            receivedSubject.setTotal(cursor.getInt(cursor.getColumnIndex(COLUMN_Subject_Total)));
            receivedSubject.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_Subject_IMAGE)));
        }



        return receivedSubject;


    }


    /**delete record**/
    public void deleteSubjectRecord(int id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID+"=?",new String[] { String.valueOf(id)});
       // db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        db.close();
        //Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();
        showToast("Deleted successfully.",context);
    }

    /**update record**/
    void updateSubjectRecord(int SubjectId, Context context, Subject updatedSubject) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedSubject.getName() + "', Present ='" + updatedSubject.getPresent()+ "', Absent ='"+ updatedSubject.getAbsent() + "', Total ='"+ updatedSubject.getTotal() + "', image ='"+ updatedSubject.getImage() + "'  WHERE _id='" + SubjectId + "'");
       // Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
        showToast("Updated successfully.",context);
    }

   int getallCount(){
       return subjectList("").size();
   }

   void update(){
       SQLiteDatabase db=this.getWritableDatabase();
       String query="UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME="+"'"+TABLE_NAME+"'";
       db.execSQL(query);
       db.close();
   }

    private void showToast(String text, Context context)
    {
        if(m_currentToast == null)
        {
            m_currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            m_currentToast.show();
        }

       else{ m_currentToast.setText(text);
        m_currentToast.setDuration(Toast.LENGTH_SHORT);
        m_currentToast.show();}
    }


}
