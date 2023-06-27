package hcmute.vika.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.vika.myapplication.Adapter.ListAdapter;
import hcmute.vika.myapplication.R;

public class CreateNewPlaylistActivity extends AppCompatActivity {
    Button create;
    EditText input_namelist;
    ListAdapter listAdapter;
    Button home,player,list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_playlist);
        create=findViewById(R.id.button_create_playlist);
        input_namelist=findViewById(R.id.edit_playlist_name);
        home = findViewById(R.id.home_btn);
        player = findViewById(R.id.play_btn);
        list=findViewById(R.id.album_btn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_album = input_namelist.getText().toString();
                Intent intent=new Intent(CreateNewPlaylistActivity.this,PlayListActivity.class);
                intent.putExtra("Name album", name_album);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewPlaylistActivity.this, MainActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MusicPlayerActivity.iscreate){
                    Intent intent = new Intent(CreateNewPlaylistActivity.this, MusicPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    Log.e("Clicked","musicplayer");
                }
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewPlaylistActivity.this, PlayListActivity.class);
                startActivity(intent);
                Log.e("Clicked","home");
            }
        });

    }
}
