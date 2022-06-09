package com.vpn.wallpaperswitcher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vpn.wallpaperswitcher.R;
import com.vpn.wallpaperswitcher.ViewWallpaperActivity;

import java.util.ArrayList;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.ViewHolder> {
    Context context;
    ArrayList<String> wallpaperList;

    public WallPaperAdapter(Context context, ArrayList<String> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(wallpaperList.get(position)).into(holder.ivWallpaper);
        holder.ivWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewWallpaperActivity.class);
                intent.putExtra("imgUrl", wallpaperList.get(position));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivWallpaper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivWallpaper = itemView.findViewById(R.id.ivWallpaper);
        }
    }
}
