package hcmute.vika.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Adapter.ListSongAdapter;
import hcmute.vika.myapplication.Model.Song;

public class MusicPlayList extends AppCompatActivity {
    TextView title, playlist_name;
    ImageView album;
    ListView listsongs;
    Button home,player,list;
    List<String> keys = new ArrayList<>();
    Song song;
    List<Song> songs =new ArrayList<>();
    ListSongAdapter arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_playlist);
        title=findViewById(R.id.title);
        playlist_name=findViewById(R.id.playlist_name);
        album=findViewById(R.id.album);
        listsongs =findViewById(R.id.listsongs);
        home = findViewById(R.id.home_btn);
        player = findViewById(R.id.play_btn);
        list=findViewById(R.id.album_btn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayList.this, MainActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicPlayerActivity.iscreate){
                    Intent intent = new Intent(MusicPlayList.this, MusicPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    Log.e("Clicked","musicplayer");
                }
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayList.this, PlayListActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });

        Intent intent =getIntent();
        String keyAlbum=intent.getStringExtra("key_music");

        playlist_name.setText(keyAlbum);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songRef = database.getReference("Song").child("Album").child(keyAlbum);

        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String item = dataSnapshot.getKey();
                    Log.e("key",item);
                    keys.add(item);
                }
                for(String key :keys){
                    DatabaseReference roofRef=database.getReference("Song");
                    roofRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot genreSnapshot : snapshot.getChildren()){
                                for(DataSnapshot songSnapshot : genreSnapshot.getChildren()){
                                    String keya = songSnapshot.getKey();
                                    if(keya.equals(key)){
                                         Song song = songSnapshot.getValue(Song.class);
                                        // Log.e("valurew",song.getName().toString());
                                        if(song.getMp3()!=null){
                                            songs.add(song);
                                        }

                                    }
                                }
                            }
                            arrayAdapter= new ListSongAdapter(MusicPlayList.this,  R.layout.music_layout ,songs);
                            listsongs.setAdapter(arrayAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Err","lỗi database");
            }
        });

    }

}
