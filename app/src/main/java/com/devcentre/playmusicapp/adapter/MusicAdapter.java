package com.devcentre.playmusicapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devcentre.playmusicapp.R;
import com.devcentre.playmusicapp.model.MusicItem;

import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private List<MusicItem> musicItems;
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int currentHolder ;

    public MusicAdapter(Context context){
        this.context = context;
    }

    public void setMusicItems(List<MusicItem> musicItems) {
        this.musicItems = musicItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_item, parent, false);
        return new ViewHolder(itemView);
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        MusicItem item = musicItems.get(position);
        holder.title.setText(item.getTitle());

        if (currentHolder == position ){
            holder.play.setImageDrawable( context.getDrawable(R.drawable.ic_pause));
        }else{
            holder.play.setImageDrawable( context.getDrawable(R.drawable.ic_play_circle));
        }


        holder.play.setOnClickListener(view -> {
            try {
                currentHolder = position;
                if(mediaPlayer == null)
                    mediaPlayer = new MediaPlayer();

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                Uri myUri = Uri.parse(item.getUri());
                mediaPlayer.reset();
                mediaPlayer.setDataSource(context, myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();

                holder.play.setImageDrawable( context.getDrawable(R.drawable.ic_pause));


            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }

    @Override
    public int getItemCount() {
        if(musicItems == null)
            return 0;
        return musicItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView play;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            play = itemView.findViewById(R.id.imvPlay);
        }
    }
}
