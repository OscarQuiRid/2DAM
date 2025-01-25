package com.example.reproductormusica;

            import android.content.Context;
            import android.media.MediaMetadataRetriever;
            import android.media.MediaPlayer;
            import android.net.Uri;

            import java.io.IOException;

            public class MediaPlayerManager {
                private MediaPlayer mediaPlayer;
                private Context context;

                public MediaPlayerManager(Context context) {
                    this.context = context;
                    mediaPlayer = new MediaPlayer();
                }

                public void play(Uri uri) throws IOException {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(context, uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }

                public void pause() {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }

                public void resume() {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }

                public void stop() {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }

                public void seekTo(int position) {
                    mediaPlayer.seekTo(position);
                }

                public int getCurrentPosition() {
                    return mediaPlayer.getCurrentPosition();
                }

                public int getDuration() {
                    return mediaPlayer.getDuration();
                }

                public MediaMetadataRetriever getMetadata(Uri uri) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(context, uri);
                    return retriever;
                }

                public void release() {
                    mediaPlayer.release();
                }
            }