package com.winxo.portailwinxo.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.View.ApagActivity;
import com.winxo.portailwinxo.View.ClaimActivity;
import com.winxo.portailwinxo.View.OrderActivity;

public class PortailWinxoService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String station_name = intent.getStringExtra("station_name");
        String message = intent.getStringExtra("message");
        int drawable = intent.getIntExtra("drawable", R.drawable.ic_view_icon_24);
        String module_name = intent.getStringExtra("module_name");
        @SuppressLint("UseCompatLoadingForDrawables")
        Bitmap left_icon_bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(), drawable);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(left_icon_bmp);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = null;
        switch (module_name){
            case "APAG":
                notificationIntent = new Intent(getApplicationContext(), ApagActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case "ORDER":
                notificationIntent = new Intent(getApplicationContext(), OrderActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case "CLAIM":
                notificationIntent = new Intent(getApplicationContext(), ClaimActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(PortailWinxoService.this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Bienvenue " + station_name)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(left_icon_bmp)
                .addAction(R.drawable.ic_view_icon_24, "Continuer", pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setGroup(Constants.UPLOAD_GROUP_ID)
                .setGroupSummary(true)
                .build();
        startForeground(1, notification);

        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}