package com.example.admin.moment_maker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }

    public void setDetails(Context ctx,String title,String description,String date,String image){
        TextView mTitle=mView.findViewById(R.id.fetchTitle);
        TextView mDescription=mView.findViewById(R.id.fetchDescription);
        TextView mDate=mView.findViewById(R.id.fetchDate);
        ImageView mImage=mView.findViewById(R.id.fetchImage);
        mTitle.setText(title);
        mDescription.setText(description);
        mDate.setText(date);
        Picasso.with(ctx).load(image).into(mImage);
    }
}
