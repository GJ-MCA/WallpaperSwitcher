package com.vpn.wallpaperswitcher.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vpn.wallpaperswitcher.R;

import java.util.ArrayList;

public class WallpaparFolderAdapter extends RecyclerView.Adapter<WallpaparFolderAdapter.ViewHolder> {

    Context context;
    ArrayList<Bitmap> wallpaperList;

    boolean isSelectedMode = false;
    ArrayList<Bitmap> selectedList = new ArrayList<>();

    public WallpaparFolderAdapter(Context context, ArrayList<Bitmap> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_folder_list, parent, false);

        return new WallpaparFolderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivWallpaper.setImageBitmap(wallpaperList.get(position));

        holder.ivWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectedMode == true) {

                    if (selectedList.contains(wallpaperList.get(position))) {
                        holder.cvFolderWallpaper.setBackgroundColor(Color.TRANSPARENT);
                        selectedList.remove(wallpaperList.get(position));
                    } else {
                        holder.cvFolderWallpaper.setBackgroundColor(Color.CYAN);
                        selectedList.add(wallpaperList.get(position));
                    }

                    if (selectedList.size() == 0) {
                        isSelectedMode = false;
                    }

                } else {
                    //code for simple on click event
                    //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.ivWallpaper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                isSelectedMode = true;
                if (selectedList.contains(wallpaperList.get(position))) {
                    holder.cvFolderWallpaper.setBackgroundColor(Color.TRANSPARENT);
                    selectedList.remove(wallpaperList.get(position));
                } else {
                    holder.cvFolderWallpaper.setBackgroundColor(Color.CYAN);
                    selectedList.add(wallpaperList.get(position));
                }

                if (selectedList.size() == 0) {
                    isSelectedMode = false;
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivWallpaper;
        CardView cvFolderWallpaper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivWallpaper = itemView.findViewById(R.id.ivFolderWallpaper);
            cvFolderWallpaper = itemView.findViewById(R.id.cvFolderWallpaper);

        }
    }
}
