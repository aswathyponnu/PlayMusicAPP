package com.devcentre.playmusicapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicItem> musicItems;
    private final Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int mEndTime;
    private int currentHolder = -1;
    private SeekBarUpdater seekBarUpdater;
    private int lastVisibleItem;

    public MusicAdapter(Context context) {
        this.context = context;
    }

    public void setMusicItems(List<MusicItem> musicItems) {
        this.musicItems = musicItems;
        seekBarUpdater = new SeekBarUpdater();
    }

    public void setLastVisibleItem(int lastVisibleItem) {
        this.lastVisibleItem = lastVisibleItem;
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
        holder.subtitle.setText("00 min, 00 sec");

        if (lastVisibleItem <= currentHolder || currentHolder != position ) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();

            holder.seekBar.setEnabled(false);
            holder.seekBar.setProgress(0);
            holder.seekBar.setVisibility(View.INVISIBLE);
            holder.play.setImageResource(R.drawable.ic_play_circle);
            holder.seekBar.removeCallbacks(seekBarUpdater);
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.play.setOnClickListener(view -> {
            try {

                if (mediaPlayer == null)
                    mediaPlayer = new MediaPlayer();

                holder.cardView.setCardBackgroundColor(Color.LTGRAY);

                notifyItemChanged(currentHolder);


                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Uri myUri = Uri.parse(item.getUri());
                mediaPlayer.reset();
                mediaPlayer.setDataSource(context, myUri);
                mediaPlayer.prepareAsync();
                currentHolder = position;

                mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                    mediaPlayer.start();
                    holder.play.setImageResource(R.drawable.ic_pause);
                    mEndTime = mediaPlayer.getDuration();
                    holder.cardView.setCardBackgroundColor(Color.LTGRAY);
                    String time = String.format("%02d min, %02d sec",
                            TimeUnit.MILLISECONDS.toMinutes(mEndTime),
                            TimeUnit.MILLISECONDS.toSeconds(mEndTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mEndTime))
                    );

                    holder.subtitle.setText(time);
                    holder.seekBar.setVisibility(View.VISIBLE);
                    holder.seekBar.setMax(mEndTime / 1000);

                    seekBarUpdater.playingHolder = holder;
                    holder.seekBar.post(seekBarUpdater);

                });


                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.stop();
                    holder.play.setImageResource(R.drawable.ic_play_circle);
                    holder.cardView.setCardBackgroundColor(Color.WHITE);
                    holder.seekBar.removeCallbacks(seekBarUpdater);
                    holder.seekBar.setProgress(0);
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    public int getItemCount() {
        if (musicItems == null)
            return 0;
        return musicItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public ImageView play;
        public SeekBar seekBar;
        public MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            subtitle= itemView.findViewById(R.id.tvSubTitle);
            play = itemView.findViewById(R.id.imvPlay);
            seekBar = itemView.findViewById(R.id.skBar);
            cardView = itemView.findViewById(R.id.cvMain);


        }
    }

    private class SeekBarUpdater implements Runnable {
        ViewHolder playingHolder;

        @Override
        public void run() {
            if (null != mediaPlayer && playingHolder.getAdapterPosition() == currentHolder) {
                playingHolder.seekBar.setMax(mediaPlayer.getDuration());
                playingHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                playingHolder.seekBar.postDelayed(this, 100);
            } else {
                playingHolder.seekBar.removeCallbacks(seekBarUpdater);
            }
        }
    }
}
