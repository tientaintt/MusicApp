package hcmute.vika.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Adapter.ListSongAdapter;
import hcmute.vika.myapplication.Model.Song;


public class MainActivity extends AppCompatActivity {
    //Model phần tử dữ liệu hiện
    ListView listView;
    String[] items;
    List<Song> songs = new ArrayList<>();
    Song music1;
    Button home_btn, playlist_btn,playmusic_btn;
    ListSongAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView); /// list danh sach
        home_btn = findViewById(R.id.home_btn);
        playlist_btn=findViewById(R.id.album_btn);
        playmusic_btn=findViewById(R.id.play_btn);





        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });
        playmusic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
                Log.e("Clicked","musicplayer");
            }
        });
        playlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });


        ///
        // Get a reference to the "Song" node in Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songRef = database.getReference("Song");
        ///
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot songSnapshot : snapshot.getChildren()){

                    Song song = songSnapshot.getValue(Song.class);
//                  Log.e("checked",song.getName());
                    music1 = new Song();
                    music1.setName(song.getName());
                    music1.setArtist(song.getArtist());
                    music1.setMp3(song.getMp3());
                    music1.setImage(song.getImage());
                    songs.add(music1);
                }

                arrayAdapter = new ListSongAdapter(MainActivity.this,  R.layout.music_layout ,songs);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error reading from Firebase Database", error.toException());
            }


        });







    }

}