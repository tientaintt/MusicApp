package hcmute.vika.myapplication;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.service.controls.ControlsProviderService.TAG;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import android.Manifest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hcmute.vika.myapplication.Adapter.ListSongAdapter;
import hcmute.vika.myapplication.Model.Song;
import hcmute.vika.myapplication.Service.MyService;


public class MusicPlayerActivity extends AppCompatActivity {

    Button dowload, back, play_pause, next, random, home, play, list,repeat;
    ImageView imageView;
    TextView status, songname, artistname,txttimesong,txttotaltimesong;
    SeekBar seek;
    Song pSong ;
    Boolean isPlaying= false;
    MediaPlayer mediaPlayer=new MediaPlayer();
    List<Song> songs=new ArrayList<>();;
    Song music1;
    Integer position;
    private  static final  int REQUEST_PERMISSION_CODE = 10;
    Boolean checkRandom=false;
    Boolean checkRepeat=false;
    static boolean iscreate=false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songRef = database.getReference("Song");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;
            pSong = (Song) bundle.get("Song");
            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action_music");
            handlerMusic(actionMusic);

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout);
        Init();
        iscreate=true;
    }
    private void Init() {
        dowload = findViewById(R.id.btndowload);
        back = findViewById(R.id.back);
        play_pause = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        random= findViewById(R.id.btnrandom);
        repeat=findViewById(R.id.btnrepeat);
        home = findViewById(R.id.home_btn);
        list = findViewById(R.id.album_btn);
        play = findViewById(R.id.play_btn);
        imageView = findViewById(R.id.image);
        status = findViewById(R.id.status_song);
        songname = findViewById(R.id.song_name);
        artistname = findViewById(R.id.artist_name);
        seek = findViewById(R.id.seekbarsong);
        txttimesong=findViewById(R.id.txttimesong);
        txttotaltimesong=findViewById(R.id.txttotaltimesong);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
                mediaPlayer.release();
                mediaPlayer=null;
    }

    private void handlerMusic(int action) {
        switch (action) {
            case MyService.ACTION_START:
                startMusic(pSong);
                break;
            case MyService.ACTION_CLEAR:
                play_pause.setBackgroundResource(R.drawable.icons8play50);
                mediaPlayer.pause();
                home.callOnClick();
                break;
            case MyService.ACTION_PAUSE:
                pauseMusic();
                break;
            case MyService.ACTION_RESUME:
                resumeMusic();
                break;
        }

    }
    private void pauseMusic() {

        if(mediaPlayer!=null&& isPlaying==false)
        {

            play_pause.setBackgroundResource(R.drawable.icons8play50);
            mediaPlayer.pause();
        }
    }
    private void resumeMusic() {
        if(mediaPlayer!=null&& isPlaying)
        {
            play_pause.setBackgroundResource(R.drawable.icons8pause50);
            mediaPlayer.start();
        }
    }
    private void startMusic(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(song.getMp3().toString());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pSong!=null)
        {
            showInforSong();
            setStatusPauseOrPlay();
            updateTime();
            setTimeTotalSong();
        }

    }
    private void showInforSong()
    {
        songname.setText(pSong.getName());
        artistname.setText(pSong.getArtist());
        Picasso.get().load(pSong.getImage()).into(imageView);
        //xoay img
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotationAnimator.setDuration(8000);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.start();
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying)
                {
                    pauseMusic();
                    sendActionToService(MyService.ACTION_PAUSE);
                    updateTime();
                    setTimeTotalSong();
                    rotationAnimator.pause();

                }
                else {
                    resumeMusic();
                    sendActionToService(MyService.ACTION_RESUME);
                    updateTime();
                    setTimeTotalSong();
                    rotationAnimator.resume();
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Position next", String.valueOf(position));
                if(songs.size()> position )
                {
                    position = position +1;
                    if(checkRepeat==true)
                    {
                        if(position==0){
                            position=songs.size();
                        }
                        position-=1;
                    }
                    if(checkRandom){
                        Random random1=new Random();
                        int index=random1.nextInt(songs.size());
                        if(index==position)
                        {
                            position=index-1;
                        }
                        position=index;
                    }
                    if(position>(songs.size()-1)){
                        position=0;
                    }
                    openSong(position);
                }
                next.setClickable(false);
                back.setClickable(false);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        back.setClickable(true);
                        next.setClickable(true);
                    }
                },5000);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Position back", String.valueOf(position));
                if(songs.size()> position )
                {

                    position = position -1;
                    if(position<0){
                        position=songs.size()-1;
                    }
                    if(checkRepeat==true)
                    {

                        position+=1;
                    }
                    if(checkRandom){
                        Random random1=new Random();
                        int index=random1.nextInt(songs.size());
                        if(index==position)
                        {
                            position=index-1;
                        }
                        position=index;
                    }

                    openSong(position);
                }
                next.setClickable(false);
                back.setClickable(false);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        back.setClickable(true);
                        next.setClickable(true);
                    }
                },5000);

            }
        });

        dowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission();
            }
        });


    }
    private void setStatusPauseOrPlay()
    {
        if(isPlaying)
        {
            play_pause.setBackgroundResource(R.drawable.icons8pause50);
        }
        else {
            play_pause.setBackgroundResource(R.drawable.icons8play50);
        }
    }
    private void sendActionToService(int action){
        Intent intent=new Intent(this,MyService.class);
        Bundle bundle=new Bundle();

        bundle.putInt("action_music_service",action);

        intent.putExtras(bundle);
        startService(intent);
    }
    private void setTimeTotalSong()
    {
        SimpleDateFormat dinhGiaGio = new SimpleDateFormat("mm:ss");
        txttotaltimesong.setText(String.valueOf(dinhGiaGio.format(mediaPlayer.getDuration())));
        seek.setMax(mediaPlayer.getDuration());
    }
    private void updateTime()
    {
        if(mediaPlayer!=null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat dinhdang = new SimpleDateFormat("mm:ss");
                    txttimesong.setText(dinhdang.format(mediaPlayer.getCurrentPosition()));
                    seek.setProgress(mediaPlayer.getCurrentPosition());
               // kiểm tra thời gian bài hát nếu kết thúc --> chuyển bài
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        next.callOnClick();
                    }
                });
                    handler.postDelayed(this, 500);
                }
            }, 100);
        }
    }
    public void openSong(int position) {
        Log.e("Position", String.valueOf(position));
        Song song = songs.get(position);
        startMusic(song);
        Intent intent1 = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song", song);
        intent1.putExtras(bundle);
        startService(intent1);
    }


    public void checkPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){

                String permission=(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissions(new String[]{permission},REQUEST_PERMISSION_CODE);
            } else {
                startDownloadFile();
            }
        }else {
            startDownloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startDownloadFile();
            }else {
                Toast.makeText(this, "Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownloadFile(){
        String urlFile=pSong.getMp3().toString().trim();
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(pSong.getName());
        request.setDescription("Download mp3...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if(downloadManager!=null){
            downloadManager.enqueue(request);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance( this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_MusicPlayer"));
        Intent intent=getIntent();
        position = intent.getIntExtra("positionSong", 0);
        if(!mediaPlayer.isPlaying())
            sendActionToService(MyService.ACTION_START);
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot genreSnapshot : snapshot.getChildren()){
                    for(DataSnapshot songSnapshot : genreSnapshot.getChildren()){
                        Song song = songSnapshot.getValue(Song.class);
                        songs.add(song);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, PlayListActivity.class);
                startActivity(intent);
            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seek.getProgress());
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRepeat==false){
                    if(checkRandom==true){
                        checkRandom=false;
                        repeat.setBackgroundResource(R.drawable.icons8repeat48pressed);
                        random.setBackgroundResource(R.drawable.icons8random64);
                    }
                    repeat.setBackgroundResource(R.drawable.icons8repeat48pressed);
                    checkRepeat=true;
                }
                else {
                    repeat.setBackgroundResource(R.drawable.icons8repeat48);
                    checkRepeat=false;
                }
            }
        });
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRandom==false){
                    if(checkRepeat==true){
                        checkRepeat=false;
                        repeat.setBackgroundResource(R.drawable.icons8repeat48);
                        random.setBackgroundResource(R.drawable.icons8random64pressed);
                    }
                    random.setBackgroundResource(R.drawable.icons8random64pressed);
                    checkRandom=true;
                }
                else {
                    random.setBackgroundResource(R.drawable.icons8random64);
                    checkRandom=false;
                }
            }
        });
    }
}