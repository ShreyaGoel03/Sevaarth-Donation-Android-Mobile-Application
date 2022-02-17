package com.example.sevaarth;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Galleryviewholder>{
    Context context;
    ArrayList<UploadInfo> image_info;

    public GalleryAdapter(Context context, ArrayList<UploadInfo> image_urls) {
        this.image_info = image_urls;
        this.context = context;
    }

    @NonNull
    @Override
    public Galleryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new Galleryviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Galleryviewholder holder, int position) {
        UploadInfo url = image_info.get(position);
        //Picasso.get().load("https://cdn.pixabay.com/photo/2012/04/24/11/54/picasso-39592_960_720.png").into(holder.imageView);
        Picasso.with(holder.imageView.getContext()).load(url.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        Log.d("RUN", "Count "+image_info.size());
        return image_info.size();
    }

    public class Galleryviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Galleryviewholder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_item_view);
        }
    }
}
