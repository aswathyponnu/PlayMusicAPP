package com.devcentre.playmusicapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devcentre.playmusicapp.R;
import com.devcentre.playmusicapp.model.MusicItem;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private List<MusicItem> musicItems;
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler;
    private int mEndTime;
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

        if (holder.play.getTag().toString().equals("1")){
            holder.play.setImageDrawable( context.getDrawable(R.drawable.ic_play_circle));
        }else{
            holder.play.setImageDrawable( context.getDrawable(R.drawable.ic_pause));
        }

        holder.seekBar.setVisibility(View.INVISIBLE);
        holder.play.setTag(0);

        holder.play.setOnClickListener(view -> {
            try {

                holder.play.setTag(1);
                currentHolder = position;
                if(mediaPlayer == null)
                    mediaPlayer = new MediaPlayer();

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                Uri myUri = Uri.parse(item.getUri());
                mediaPlayer.reset();
                mediaPlayer.setDataSource(context, myUri);
                mediaPlayer.prepareAsync();
                //mediaPlayer.start();
                mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    mediaPlayer.start();
                    mEndTime = mediaPlayer.getDuration();
                    holder.seekBar.setVisibility(View.VISIBLE);
                    holder.seekBar.setProgress(mEndTime);

                    

                });
                mediaPlayer.setOnCompletionListener(MediaPlayer::stop);

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
        public SeekBar seekBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            play = itemView.findViewById(R.id.imvPlay);
            seekBar = itemView.findViewById(R.id.skBar);


        }
    }
}
