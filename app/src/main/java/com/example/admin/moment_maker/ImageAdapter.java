package com.example.admin.moment_maker;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
   private Context mContext;
   private List<UserDetails> mUploads;
    private StorageReference storageReference,imgRef;
    File localfile;
   public ImageAdapter(Context context,List<UserDetails> uploads){
       mContext = context;
       mUploads = uploads;
       storageReference=FirebaseStorage.getInstance().getReference("Uploads");
       try {
           localfile=File.createTempFile("bdayimage","jpeg");
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        CardView cv = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hug_listview,viewGroup,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int i) {
       final CardView cardView = holder.cardView;
        TextView txt1 = cardView.findViewById(R.id.fetchTitle);
        TextView txt2 = cardView.findViewById(R.id.fetchDate);
        TextView txt3 = cardView.findViewById(R.id.fetchDescription);
        Button btn = cardView.findViewById(R.id.invitebtn);
        txt1.setText(mUploads.get(i).getTitle());
        txt2.setText(mUploads.get(i).getDate());
        txt3.setText(mUploads.get(i).getDescription());
        imgRef = storageReference.child(mUploads.get(i).getTitle());
        try {
            imgRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    ImageView imageView = cardView.findViewById(R.id.fetchImage);
                    imageView.setImageURI(Uri.fromFile(localfile));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }catch (Exception e){
        }
    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v)
        {
            super(v);
            cardView=v;
        }
     }
}




