package com.example.reproductormusica;

            import android.content.Context;
            import android.database.Cursor;
            import android.graphics.Bitmap;
            import android.graphics.BitmapFactory;
            import android.media.MediaMetadataRetriever;
            import android.net.Uri;
            import android.provider.MediaStore;
            import android.view.LayoutInflater;
            import android.view.View;
            import android.view.ViewGroup;
            import android.widget.ImageView;
            import android.widget.TextView;

            import androidx.annotation.NonNull;
            import androidx.recyclerview.widget.RecyclerView;

            import java.util.List;

            public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
                private Context context;
                private List<Uri> songUris;
                private OnSongSelectedListener listener;

                public interface OnSongSelectedListener {
                    void onSongSelected(Uri songUri);
                }

                public SongAdapter(Context context, List<Uri> songUris, OnSongSelectedListener listener) {
                    this.context = context;
                    this.songUris = songUris;
                    this.listener = listener;
                }

                @NonNull
                @Override
                public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
                    return new SongViewHolder(view);
                }

                @Override
                public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
                    Uri songUri = songUris.get(position);

                    String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME};
                    Cursor cursor = context.getContentResolver().query(songUri, projection, null, null, null);
                    String fileName = "";
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                        cursor.close();
                    }

                    holder.songTitle.setText(fileName);

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(context, songUri);

                    byte[] art = retriever.getEmbeddedPicture();
                    if (art != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                        holder.coverImage.setImageBitmap(bitmap);
                    } else {
                        holder.coverImage.setImageResource(R.drawable.ic_music_note);
                    }

                    holder.itemView.setOnClickListener(v -> listener.onSongSelected(songUri));
                }

                @Override
                public int getItemCount() {
                    return songUris.size();
                }

                public static class SongViewHolder extends RecyclerView.ViewHolder {
                    ImageView coverImage;
                    TextView songTitle;

                    public SongViewHolder(@NonNull View itemView) {
                        super(itemView);
                        coverImage = itemView.findViewById(R.id.coverImage);
                        songTitle = itemView.findViewById(R.id.songTitle);
                    }
                }
            }