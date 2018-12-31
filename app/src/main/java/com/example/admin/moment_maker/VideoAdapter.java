package com.example.admin.moment_maker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends ArrayAdapter<VideoDetails> {
    Context context;
    ArrayList<VideoDetails> al ;
    String bdaygirl;
    public VideoAdapter(@NonNull Context context, ArrayList al) {
        super(context,R.layout.video_listview,al);
        this.al = al;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.video_listview,parent,false);
        TextView t1 =convertView.findViewById(R.id.name);
        TextView t2 = convertView.findViewById(R.id.date);
        Button b1 = convertView.findViewById(R.id.showbtn);
        t1.setText(al.get(position).getName());
        t2.setText(al.get(position).getDate());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bdaygirl = al.get(position).getName();
                Intent intent = new Intent(context,ShowVideo.class);
                intent.putExtra("bdaygirl",bdaygirl);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
