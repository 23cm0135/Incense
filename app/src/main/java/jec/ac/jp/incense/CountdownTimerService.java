package jec.ac.jp.incense;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class CountdownTimerService extends Service {
    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "incense_timer_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        startForeground(1, getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int musicResId = intent.getIntExtra("MUSIC_RES_ID", R.raw.music1);

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, musicResId);
        if (mediaPlayer == null) {
            Log.e("CountdownTimerService", "无法播放音乐！音乐资源 ID：" + musicResId);
            return START_NOT_STICKY;
        }

        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "倒计时音乐服务",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("倒计时运行中")
                .setContentText("音乐正在播放")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }
}
