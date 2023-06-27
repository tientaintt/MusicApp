package hcmute.vika.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Adapter.ListSongAdapter;
import hcmute.vika.myapplication.Model.Song;


public class MainActivity extends AppCompatActivity {
    //Model phần tử dữ liệu hiện
    EditText search_box;
    ListView listView;
    ImageView upload;
    List<Song> songs = new ArrayList<>();
    Button home_btn, playlist_btn,playmusic_btn,trutinh_btn,edm_btn,nhacviet_btn,khac_btn,usuk_btn;
    ListSongAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadMusic.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterSongs(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        playmusic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicPlayerActivity.iscreate){
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                }
            }
        });
        playlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                startActivity(intent);

            }
        });


        ///
        // Get a reference to the "Song" node in Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songRef = database.getReference("Song");


        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot genreSnapshot : snapshot.getChildren()){
                    for(DataSnapshot songSnapshot : genreSnapshot.getChildren()){
                        Song song = songSnapshot.getValue(Song.class);
                        if(song.getMp3()!=null){
                            songs.add(song);
                        }
                    }
                }
                arrayAdapter = new ListSongAdapter(MainActivity.this,  R.layout.music_layout ,songs);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error reading from Firebase Database", error.toException());
            }


        });
        nhacviet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opensong("VPop");
            }
        });
        usuk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensong("USUK");
            }
        });
        trutinh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensong("TruTinh");
            }
        });
        edm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensong("EDM");
            }
        });
        khac_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensong("Other");
            }
        });

    }

    private void filterSongs(String text) {
        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredSongs.add(song);
            }


        }
        arrayAdapter.notifyDataSetChanged();
        arrayAdapter= new ListSongAdapter(MainActivity.this,  R.layout.music_layout ,filteredSongs);
        listView.setAdapter(arrayAdapter);

    }
    private void Init() {
        search_box=findViewById(R.id.search_tool);
        listView = findViewById(R.id.listView); /// list danh sach
        home_btn = findViewById(R.id.home_btn);
        playlist_btn=findViewById(R.id.album_btn);
        playmusic_btn=findViewById(R.id.play_btn);
        upload=findViewById(R.id.playlist_icon);
        trutinh_btn=findViewById(R.id.button4);
        nhacviet_btn=findViewById(R.id.button1);
        usuk_btn=findViewById(R.id.button2);
        khac_btn=findViewById(R.id.button5);
        edm_btn=findViewById(R.id.button3);
    }

    public void opensong(String genre){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songRef = database.getReference("Song").child(genre);
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();

        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot genreSnapshot : snapshot.getChildren()){
                    Song song = genreSnapshot.getValue(Song.class);
                    songs.add(song);
                }
                arrayAdapter= new ListSongAdapter(MainActivity.this,  R.layout.music_layout ,songs);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error reading from Firebase Database", error.toException());
            }


        });
    }

}