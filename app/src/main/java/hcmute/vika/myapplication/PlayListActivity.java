package hcmute.vika.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Adapter.ListAdapter;
import hcmute.vika.myapplication.Model.MyItem;
import hcmute.vika.myapplication.Model.Song;

public class PlayListActivity extends AppCompatActivity {
    Button home_btn, playlist_btn,playmusic_btn,create_btn;
    ListView listView;
    MyItem itemList;
    List<MyItem> items =new ArrayList<>();
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_list);
        listView=findViewById(R.id.listview_playlist);
        home_btn = findViewById(R.id.home_btn);
        playlist_btn=findViewById(R.id.album_btn);
        playmusic_btn=findViewById(R.id.play_btn);
        create_btn=findViewById(R.id.createList);





        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayListActivity.this, MainActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });
        playmusic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicPlayerActivity.iscreate){
                    Intent intent = new Intent(PlayListActivity.this, MusicPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    Log.e("Clicked","musicplayer");
                }
            }
        });
        playlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayListActivity.this, PlayListActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayListActivity.this, CreateNewPlaylistActivity.class);
                startActivity(intent);
            }

        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference songRef = database.getReference("Song").child("Album");
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot albumSnapshot: snapshot.getChildren()){
                    String album= albumSnapshot.getKey();
                    itemList=new MyItem(R.drawable.album,album);
                    items.add(itemList);
                }
                ListAdapter listAdapter = new ListAdapter(PlayListActivity.this, items);
                listView.setAdapter(listAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}