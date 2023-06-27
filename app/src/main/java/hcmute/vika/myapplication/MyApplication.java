package hcmute.vika.myapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public static final String Chanel_ID="Chanel_Play_Song";
    @Override
    public void onCreate() {
        super.onCreate();
        createChanelNotification();
    }

    private void createChanelNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(Chanel_ID,
                    "Chanel Service", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);//Hệ điều hành hơn 8.0
            NotificationManager manager=getSystemService(NotificationManager.class);
            if(manager!=null)
            {
                manager.createNotificationChannel(channel);

            }
        }
    }
}
