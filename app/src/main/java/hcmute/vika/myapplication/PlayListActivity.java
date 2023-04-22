package hcmute.vika.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayListActivity extends AppCompatActivity {
    Button home_btn, playlist_btn,playmusic_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_list);

        home_btn = findViewById(R.id.home_btn);
        playlist_btn=findViewById(R.id.album_btn);
        playmusic_btn=findViewById(R.id.play_btn);

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
                Intent intent = new Intent(PlayListActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
                Log.e("Clicked","musicplayer");
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
    }
}