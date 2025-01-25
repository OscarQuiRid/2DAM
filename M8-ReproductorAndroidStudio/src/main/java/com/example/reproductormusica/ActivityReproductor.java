package com.example.reproductormusica;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ActivityReproductor extends AppCompatActivity {
    private MediaPlayerManager mediaPlayerManager;
    private ImageView coverImageView;
    private TextView songTitleTextView;
    private TextView artistTextView;
    private TextView genreTextView;
    private TextView durationTextView;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        mediaPlayerManager = new MediaPlayerManager(this);

        coverImageView = findViewById(R.id.coverImageView);
        songTitleTextView = findViewById(R.id.songTitleTextView);
        artistTextView = findViewById(R.id.artistTextView);
        genreTextView = findViewById(R.id.genreTextView);
        durationTextView = findViewById(R.id.durationTextView);
        seekBar = findViewById(R.id.seekBar);

        Uri songUri = Uri.parse(getIntent().getStringExtra("songUri"));
        String title = getIntent().getStringExtra("title");
        String artist = getIntent().getStringExtra("artist");
        String genre = getIntent().getStringExtra("genre");
        int duration = getIntent().getIntExtra("duration", 0);
        int currentPosition = getIntent().getIntExtra("currentPosition", 0);
        boolean isPlaying = getIntent().getBooleanExtra("isPlaying", false);

        songTitleTextView.setText(title);
        artistTextView.setText(artist);
        genreTextView.setText(genre);
        durationTextView.setText(formatDuration(duration));
        seekBar.setMax(duration);
        seekBar.setProgress(currentPosition);

        MediaMetadataRetriever retriever = mediaPlayerManager.getMetadata(songUri);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            coverImageView.setImageBitmap(bitmap);
        } else {
            coverImageView.setImageResource(R.drawable.ic_music_note);
        }

        try {
            mediaPlayerManager.play(songUri);
            mediaPlayerManager.seekTo(currentPosition);
            if (!isPlaying) {
                mediaPlayerManager.pause();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.post(updateSeekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayerManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayerManager.getCurrentPosition());
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        mediaPlayerManager.release();
    }

    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}