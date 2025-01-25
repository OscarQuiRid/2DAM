package com.example.reproductormusica;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MediaPlayerManager mediaPlayerManager;
    private RecyclerView recyclerView;
    private LinearLayout musicPlayerLayout;
    private ImageView songCover;
    private TextView songTitleTextView;
    private ImageButton playPauseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private SeekBar seekBar;
    private TextView songRemainingTime;
    private TextView songTotalTime;

    private List<Uri> songUris = new ArrayList<>();
    private boolean isPlaying = false;
    private int currentSongIndex = 0;
    private Uri currentSongUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayerManager = new MediaPlayerManager(this);

        recyclerView = findViewById(R.id.recyclerView);
        musicPlayerLayout = findViewById(R.id.musicPlayerLayout);
        songCover = findViewById(R.id.songCover);
        songTitleTextView = findViewById(R.id.songTitleTextView);
        playPauseButton = findViewById(R.id.playPauseButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        seekBar = findViewById(R.id.seekBar);
        songRemainingTime = findViewById(R.id.songRemainingTime);
        songTotalTime = findViewById(R.id.songTotalTime);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SongAdapter adapter = new SongAdapter(this, songUris, this::onSongSelected);
        recyclerView.setAdapter(adapter);

        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayerManager.pause();
                playPauseButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayerManager.resume();
                playPauseButton.setImageResource(R.drawable.pausa);
            }
            isPlaying = !isPlaying;
        });

        prevButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());

        songCover.setOnClickListener(v -> {
            if (currentSongUri != null) {
                MediaMetadataRetriever retriever = mediaPlayerManager.getMetadata(currentSongUri);
                Intent intent = new Intent(MainActivity.this, ActivityReproductor.class);
                intent.putExtra("songUri", currentSongUri.toString());
                intent.putExtra("title", songTitleTextView.getText().toString());
                intent.putExtra("artist", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                intent.putExtra("genre", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                intent.putExtra("duration", mediaPlayerManager.getDuration());
                intent.putExtra("currentPosition", mediaPlayerManager.getCurrentPosition());
                intent.putExtra("isPlaying", isPlaying);

                // Pausar la reproducción antes de iniciar ActivityReproductor
                mediaPlayerManager.pause();
                isPlaying = false;

                startActivity(intent);
            }
        });

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

        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityResultLauncher<String[]> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                        Boolean readAudioPermission = result.getOrDefault(Manifest.permission.READ_MEDIA_AUDIO, false);
                        if (readAudioPermission != null && readAudioPermission) {
                            loadSongs();
                        }
                    });
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_AUDIO});
        } else {
            ActivityResultLauncher<String[]> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                        Boolean readPermission = result.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false);
                        if (readPermission != null && readPermission) {
                            loadSongs();
                        }
                    });
            requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void loadSongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Uri songUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                songUris.add(songUri);
            }
            cursor.close();
        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void onSongSelected(Uri songUri) {
        currentSongIndex = songUris.indexOf(songUri);
        currentSongUri = songUri;
        playSong(songUri);
    }

    private void playSong(Uri songUri) {
        try {
            mediaPlayerManager.play(songUri);
            MediaMetadataRetriever retriever = mediaPlayerManager.getMetadata(songUri);
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            if (title == null || title.isEmpty()) {
                String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(songUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    cursor.close();
                }
            }

            songTitleTextView.setText(title);

            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                songCover.setImageBitmap(bitmap);
            } else {
                songCover.setImageResource(R.drawable.ic_music_note);
            }

            musicPlayerLayout.setVisibility(View.VISIBLE);
            playPauseButton.setImageResource(R.drawable.pausa);
            isPlaying = true;

            int duration = mediaPlayerManager.getDuration();
            seekBar.setMax(duration);
            songTotalTime.setText(formatDuration(duration));
            updateSeekBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            playSong(songUris.get(currentSongIndex));
        }
    }

    private void playNextSong() {
        if (currentSongIndex < songUris.size() - 1) {
            currentSongIndex++;
            playSong(songUris.get(currentSongIndex));
        }
    }

    private void updateSeekBar() {
        int currentPosition = mediaPlayerManager.getCurrentPosition();
        seekBar.setProgress(currentPosition);
        songRemainingTime.setText(formatDuration(mediaPlayerManager.getDuration() - currentPosition));
        songTotalTime.setText(formatDuration(mediaPlayerManager.getDuration()));
        seekBar.postDelayed(this::updateSeekBar, 1000);
    }

    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}