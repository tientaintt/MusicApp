package hcmute.vika.myapplication.Service;

import static hcmute.vika.myapplication.MyApplication.Chanel_ID;

import android.app.Notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.Model.Song;
import hcmute.vika.myapplication.MusicPlayerActivity;
import hcmute.vika.myapplication.R;
import hcmute.vika.myapplication.Receiver.MyReceiver;

public class MyService extends Service {
    public static final int ACTION_PAUSE=1;
    public static final int ACTION_RESUME=2;
    public static final int ACTION_CLEAR=3;
    public static final int ACTION_START=4;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;//false
    private Song gsong;
    private ArrayList<Song> listSong;
    private int position;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getExtras();


        if(bundle!=null){
            Song song= (Song) bundle.get("Song");
//            Log.e("Song1", String.valueOf(song.getMp3()));
            if(song!= null){
                gsong=song;
                startMusic(song);



                Log.e("Test", String.valueOf(ACTION_START));
            }

        }
        int actionMusic=bundle.getInt("action_music_service",0);
        handlerActionMusic(actionMusic);
        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        isPlaying = true;

        sendNotification(song);
        //sendActionToActivity(ACTION_START);
    }
    private void handlerActionMusic(int action){
        switch (action)
        {
            case ACTION_START:
               // startMusic();
                //startMusic(gsong);
                sendActionToActivity(ACTION_START);
                break;
            case ACTION_CLEAR:

                stopSelf();
                break;
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
                
        }

    }

    private void resumeMusic() {
//        if(isPlaying==false)
//        {
            isPlaying = true;
            sendNotification(gsong);
            sendActionToActivity(ACTION_RESUME);
//        }


    }

    private void pauseMusic() {
//        if(isPlaying)
//        {
            isPlaying = false;
            sendNotification(gsong);
            sendActionToActivity(ACTION_PAUSE);
//        }

    }
    


    private void sendNotification(Song song) {
        Intent intent =new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Bitmap bitmap= BitmapFactory.decodeResource(getResources(),song.getImage());

        RemoteViews remoteViews=new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_title_song,song.getName());
        remoteViews.setTextViewText(R.id.tv_single_song,song.getArtist());
        //remoteViews.setImageViewBitmap(R.id.img_song,bitmap);
        //Sử dụng setImageViewResource dành cho những icon nhỏ
        remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.icons8pause50);
        //Bắt sự kiện cho từng thành phần view
        if(isPlaying)
        {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this,ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.icons8pause50);

        }
        else{
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this,ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.icons8play50);
        }
        remoteViews.setOnClickPendingIntent(R.id.img_close,getPendingIntent(this,ACTION_CLEAR));

        Notification notification=new NotificationCompat.Builder(this,Chanel_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)//Không setting tiếng mặc định của notification dành cho hệ điều hành 8.0 trở xuống
                .build();
        startForeground(1,notification);
    }
    private PendingIntent getPendingIntent(Context context,int action){
        Intent intent=new Intent(this, MyReceiver.class);
        intent.putExtra("action_music",action);
        return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void sendActionToActivity(int action)
    {
        Intent intent=new Intent("send_data_to_MusicPlayer");
        Bundle bundle=new Bundle();
        bundle.putSerializable("Song",gsong);
        bundle.putBoolean("status_player",isPlaying);
        Log.e("trangthai2", String.valueOf(isPlaying));
        bundle.putInt("action_music",action);

        intent.putExtras(bundle);

        //startActivity(intent);


        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Hủy media Player

    }
}
