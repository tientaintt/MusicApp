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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Adapter.ListSongAdapter;
import hcmute.vika.myapplication.Model.Song;
import hcmute.vika.myapplication.Service.MyService;


public class MusicPlayerActivity extends AppCompatActivity {

    Button dowload, back, play_pause, next, addlist, home, play, list;
    ImageView imageView;
    TextView status, songname, artistname,txttimesong,txttotaltimesong;
    SeekBar seek;
    Song pSong ;
    Boolean isPlaying;
    MediaPlayer mediaPlayer;
    List<Song> songs=new ArrayList<>();;
    Song music1;
    Integer position;
    private  static final  int REQUEST_PERMISSION_CODE = 10;
    int duration;
    int currentposition;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference songRef = database.getReference("Song");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();


            if (bundle == null)
                return;
            Log.e("Err","RUnn");
            pSong = (Song) bundle.get("Song");
            isPlaying = bundle.getBoolean("status_player");
            Log.e("trangthai1", String.valueOf(isPlaying));
            int actionMusic = bundle.getInt("action_music");


            Log.e("errr1",Integer.toString(actionMusic));
            handlerMusic(actionMusic);

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.music_player_layout);
        Init();
        LocalBroadcastManager.getInstance( this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_MusicPlayer"));


        Intent intent=getIntent();
//        Bundle bundle = intent.getExtras();
//        Log.e("bundle", String.valueOf(bundle));
//        if (bundle == null)
//            return;
//        //Log.e("Err","RUnn");
//        pSong = (Song) bundle.get("Song");
//        Log.e("songname",pSong.getName());
        position = intent.getIntExtra("positionSong", 0);
//        startMusic(pSong);
        sendActionToService(MyService.ACTION_START);
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot songSnapshot : snapshot.getChildren()) {

                    Song song = songSnapshot.getValue(Song.class);
//                  Log.e("checked",song.getName());
                    music1 = new Song();
                    music1.setName(song.getName());
                    music1.setArtist(song.getArtist());
                    music1.setMp3(song.getMp3());
                    music1.setImage(song.getImage());
                    songs.add(music1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Intent Filter lọc intent
        //sendActionToService(MyService.ACTION_START);


//        songname.setText(getIntent().getStringExtra("SongName"));
//        artistname.setText(getIntent().getStringExtra("ArtistName"));
//
//        play_pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                swapButton();
//            }
//        });
//        showInforSong();
//        setStatusPauseOrPlay();
//        songRef.addValueEventListener(new ValueEventListener() {
//                                          @Override
//                                          public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                              for (DataSnapshot songSnapshot : snapshot.getChildren()) {
//
//                                                  Song song = songSnapshot.getValue(Song.class);
////                  Log.e("checked",song.getName());
//                                                  Song music1 = new Song();
//                                                  music1.setName(song.getName());
//                                                  music1.setArtist(song.getArtist());
//                                                  music1.setMp3(song.getMp3());
//                                                  music1.setImage(song.getImage());
//                                                  songs.add(music1);
//                                              }
//
//                                          }
//
//                                          @Override
//                                          public void onCancelled(@NonNull DatabaseError error) {
//
//                                          }
//                                      });
//
//            Intent intent=getIntent();
//            position = intent.getIntExtra("positionSong", 0);
//       next.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               if(songs.size()==(position +1))
//               {
//                   position =0;
//                   openSong(position);
//               }
//               else {
//                   position = position +1;
//                   openSong(position);
//               }
//
//           }
//       });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (position == 0) {
//                    position = songs.size() - 1;
//                    openSong(position);
//                }
//                else {
//                    position = position - 1;
//                    openSong(position);
//
//                }
//
//            }
//        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                Log.e("Clicked", "home");
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, MusicPlayerActivity.class);
                startActivity(intent);
                Log.e("Clicked", "musicplayer");
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, PlayListActivity.class);
                startActivity(intent);
                Log.e("Clicked", "home");
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

    }

    private void swapButton() {
        if(play_pause.getBackground().equals(R.drawable.icons8play50))
        {
            play_pause.setBackgroundResource(R.drawable.icons8pause50);
        }
        else
            play_pause.setBackgroundResource(R.drawable.icons8play50);
    }

    private void Init() {
        dowload = findViewById(R.id.btndowload);
        back = findViewById(R.id.back);
        play_pause = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        addlist = findViewById(R.id.addplaylist);
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
            //Hủy media Player
            if(mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
            }


    }

    private void handlerMusic(int action) {


        switch (action) {
            case MyService.ACTION_START:
                startMusic(pSong);


                break;
            case MyService.ACTION_CLEAR:

                break;
            case MyService.ACTION_PAUSE:
                pauseMusic();
                Log.e("PAsue","da vao");
                //setStatusPauseOrPlay();
                break;
            case MyService.ACTION_RESUME:
                resumeMusic();
                //setStatusPauseOrPlay();
                break;
        }

    }
    private void pauseMusic() {
        Log.e("trangthai", String.valueOf(isPlaying));
        if(mediaPlayer!=null&& isPlaying==false)
        {
            Log.e("PAsue","da vao");
            play_pause.setBackgroundResource(R.drawable.icons8play50);

            //setStatusPauseOrPlay();
            mediaPlayer.pause();
            //isPlaying=false;


        }
    }
    private void resumeMusic() {
        if(mediaPlayer!=null&& isPlaying)
        {
            play_pause.setBackgroundResource(R.drawable.icons8pause50);
           // setStatusPauseOrPlay();
            mediaPlayer.start();
            //isPlaying=true;
            //Update view

            //sendActionToService(MyService.ACTION_RESUME);

        }
    }
    private void startMusic(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            Log.e("Xoamedia","daxoa");
        }
        mediaPlayer = new MediaPlayer();
//        mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.parse(song.getMp3()));
        try {
            mediaPlayer.setDataSource(song.getMp3().toString());
            Log.e("Mp3",song.getMp3().toString());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //mediaPlayer.start();
        //isPlaying = true;
        showInforSong();
        setStatusPauseOrPlay();
        updateTime();
        setTimeTotalSong();
        //sendActionToService(MyService.ACTION_START);
    }
    private void showInforSong()
    {
//        if(pSong==null)
//            return;
        //imageView.setImageResource(pSong.getImage());
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
                    //play_pause.setBackgroundResource(R.drawable.icons8pause50);
                }
                else {

                    resumeMusic();
                    sendActionToService(MyService.ACTION_RESUME);
                    //play_pause.setBackgroundResource(R.drawable.icons8play50);
                    updateTime();
                    setTimeTotalSong();
                    rotationAnimator.resume();
                }

            }
        });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songs.size()== position +1)
                {
                    position =0;
                    openSong(position);
                }
                else {
                    position = position +1;
                    openSong(position);
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0) {
                    position = songs.size() - 1;
                    openSong(position);
                }
                else {
                    position = position - 1;
                    openSong(position);

                }

            }
        });

        dowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                downloadmusic();
//                FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
//                Log.i(TAG, "Bucket = " + opts.getStorageBucket();
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
        //bundle.putSerializable("Song",pSong);
        bundle.putInt("action_music_service",action);
        //intent.putExtra("status_player",isPlaying);
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

//                kiểm tra thời gian bài hát nếu kết thúc --> chuyển bài
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        index++;
//                        if (index > arrSong.size() - 1)
//                        {
//                            index = 0;
//                        }
//                        if (mediaPlayer.isPlaying())
//                        {
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                        }
//
//                        mediaPlayer.start();
//
//                        setTimeTotalSong();
//                        updateTime();
//                    }
//                });
                    handler.postDelayed(this, 500);
                }


            }, 100);
        }
    }
    public void openSong(int position) {
        Log.e("Position", String.valueOf(position));
        Song song = songs.get(position);
        startMusic(song);
//        Intent intent = new Intent(context, MusicPlayerActivity.class);
////        intent.putExtra("SongName", song.getName());
////        intent.putExtra("ArtistName", song.getArtist());
////        intent.putExtra("Mp3", song.getMp3());
//        context.startActivity(intent);
        Intent intent1 = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song", song);

       // intent1.putExtra("positionSong",position);
        intent1.putExtras(bundle);

        startService(intent1);
    }


    public void checkPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
//            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
//
//                String[] permission=(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                requestPermissions(permission,REQUEST_PERMISSION_CODE);
//            } else {
//                startDownloadFile();
//            }
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
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/applicationmusic-56667.appspot.com/o/Hoa%20N%E1%BB%9F%20Kh%C3%B4ng%20M%C3%A0u.mp3?alt=media&token=14d2115e-8d8c-4136-892d-87a1dc080ca7");
//        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                String url=uri.toString();
//                downloadfile(MusicPlayerActivity.this, "", ".mp3", DIRECTORY_DOWNLOADS,url);
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

    }
    private void downloadmusic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/applicationmusic-56667.appspot.com/o/Hoa%20N%E1%BB%9F%20Kh%C3%B4ng%20M%C3%A0u.mp3?alt=media&token=14d2115e-8d8c-4136-892d-87a1dc080ca7");
        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadfile(MusicPlayerActivity.this, "", ".mp3", DIRECTORY_DOWNLOADS,url);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void downloadfile(MusicPlayerActivity context, String fileName, String fileExtension, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);
    }
}