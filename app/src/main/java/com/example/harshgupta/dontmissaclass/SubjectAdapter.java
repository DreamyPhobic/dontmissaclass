package com.example.harshgupta.dontmissaclass;

/**
 * Created by Harsh Gupta on 04-Mar-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<Subject> mPeopleList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView SubjectNameTxtV;
        TextView SubjectPresentTxtV;
        TextView SubjectAbsentTxtV;
        ImageView SubjectImageImgV;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            SubjectNameTxtV = (TextView) v.findViewById(R.id.tv_subjectNName);
            SubjectPresentTxtV = (TextView) v.findViewById(R.id.tv_presentCount);
            SubjectAbsentTxtV = (TextView) v.findViewById(R.id.tv_absentCount);
            SubjectImageImgV = (ImageView) v.findViewById(R.id.i_subjectIcon);
        }

    }

    public void add(int position, Subject Subject) {
        mPeopleList.add(position,Subject);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mPeopleList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public SubjectAdapter(List<Subject> myDataset, Context context, RecyclerView recyclerView) {
        mPeopleList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.subjectcard, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
///data/user/0/com.example.harshgupta.dontmissaclass/cache
        final Subject Subject = mPeopleList.get(position);
        holder.SubjectNameTxtV.setText(Subject.getName());
        holder.SubjectPresentTxtV.setText(String.valueOf(Subject.getPresent()));
        holder.SubjectAbsentTxtV.setText(String.valueOf(Subject.getAbsent()));
        if(!Subject.getImage().isEmpty()){
            File file = new File("/storage/emulated/0/DMC_Images"+Subject.getName());
            if(!file.exists()){
                Picasso.with(mContext).load(Subject.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.SubjectImageImgV);
            }
            else
                Picasso.with(mContext).load(file).placeholder(R.mipmap.ic_launcher).into(holder.SubjectImageImgV);
        }
        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetail = new Intent(mContext, DetailActivity.class);
                goToDetail.putExtra("USER_ID",Subject.getID());
                mContext.startActivity(goToDetail);
            }
        });


    }
    // Return the size of your dataset (invoked by the layout manPresentr)
    @Override
    public int getItemCount() {
        return mPeopleList.size();
    }



}
