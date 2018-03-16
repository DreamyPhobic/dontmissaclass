package com.example.harshgupta.dontmissaclass;

import android.provider.BaseColumns;

/**
 * Created by Harsh Gupta on 27-Feb-18.
 */

public class Subject {

    //private variables
    private int subject_id;
    private String subject_name;
    private int present;
    private int absent;
    private int total;
    private String image;
    // Empty constructor
    public Subject() {
    }

    // constructor
    public Subject(int id, String name, int present, int absent, int total,String image) {
        this.subject_id = id;
        this.subject_name = name;
        this.present = present;
        this.absent = absent;
        this.total = total;
        this.image=image;
    }

    // constructor
    public Subject(String name, int present, int absent,int total,String image) {
        this.subject_name = name;
        this.present = present;
        this.absent = absent;
        this.total = total;
        this.image=image;
    }

    // getting ID
    public int getID() {
        return this.subject_id;
    }

    // setting id
    public void setID(int id) {
        this.subject_id = id;
    }

    // getting name
    public String getName() {
        return this.subject_name;
    }

    // setting name
    public void setName(String name) {
        this.subject_name = name;
    }

    public int getPresent(){
        return this.present;
    }

    // setting id
    public void setPresent(int present){
        this.present= present;
    }

    public int getAbsent(){
        return this.absent;
    }

    // setting id
    public void setAbsent(int absent){
        this.absent= absent;
    }

    public int getTotal(){
        return this.total;
    }

    // setting id
    public void setTotal(int total){
        this.total= total;
    }
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }






}

