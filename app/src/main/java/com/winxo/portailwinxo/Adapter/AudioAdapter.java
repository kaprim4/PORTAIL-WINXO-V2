package com.winxo.portailwinxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.winxo.portailwinxo.Model.Audio;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Util;
import java.io.File;
import java.util.ArrayList;

public class AudioAdapter extends BaseAdapter {

    private ArrayList<Audio> audioList;
    private LayoutInflater layoutInflater;
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar _seekBar;
    private boolean wasPlaying = false;
    private Thread thread;

    public AudioAdapter(Context context, ArrayList<Audio> audioList) {
        this.context = context;
        this.audioList = audioList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return audioList.size();
    }

    @Override
    public Object getItem(int i) {
        return audioList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.audio_row, null);
            holder = new ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.audio_title);
            _seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
            holder.timer = (TextView) convertView.findViewById(R.id.timer);
            holder.play_audio_btn = (ImageButton) convertView.findViewById(R.id.play_audio_btn);
            holder.delete_audio_btn = (ImageButton) convertView.findViewById(R.id.delete_audio_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Audio audio = this.audioList.get(position);
        holder.number.setText(audio.getNumber());

        _seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                holder.timer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                holder.timer.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);
                if (x != 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();
                    holder.play_audio_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_24));
                    seekBar.setProgress(0);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        holder.play_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAudioFile(audio.getTitle(), holder);
            }
        });

        holder.delete_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(audio.getTitle());
            }
        });

        holder.seekBar = this._seekBar;
        return convertView;
    }

    static class ViewHolder {
        TextView number, timer;
        SeekBar seekBar;
        ImageButton play_audio_btn, delete_audio_btn;
    }

    public void PlayAudioFile(String filename, ViewHolder holder) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                holder.seekBar.setProgress(0);
                wasPlaying = true;
                holder.play_audio_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_24));
            }
            if (!wasPlaying) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                filename = context.getExternalCacheDir().getAbsolutePath() + "/" + filename;
                holder.play_audio_btn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_pause_24));
                Log.e("FILES", "filename: " + filename);
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepare();
                mediaPlayer.setVolume(0.5f, 0.5f);
                mediaPlayer.setLooping(false);
                String duration = Util.convertSeekBarDuration(mediaPlayer.getDuration());
                Log.e("FILES", "getDuration: " + duration);
                holder.seekBar.setMax(mediaPlayer.getDuration());
                holder.timer.setText(duration);
                mediaPlayer.start();

                thread = new Thread() {
                    @Override
                    public void run() {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        int total = mediaPlayer.getDuration();
                        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
                            try {
                                Thread.sleep(1000);
                                currentPosition = mediaPlayer.getCurrentPosition();
                            } catch (Exception e) {
                                return;
                            }
                            holder.seekBar.setProgress(currentPosition);
                        }
                    }
                };
            }
            wasPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void deleteFile(String filename) {
        File directory = new File(context.getExternalCacheDir().getAbsolutePath());
        File[] files = directory.listFiles();
        if (files.length > 0) {
            for (File file : files) {
                if (file.getName().equals(filename)) {
                    file.delete();
                    Toast.makeText(context, "Fichier supprimé.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        try {
            if (thread.isAlive())
                thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}