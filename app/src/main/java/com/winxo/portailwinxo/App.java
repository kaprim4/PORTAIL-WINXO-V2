package com.winxo.portailwinxo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.winxo.portailwinxo.Utilities.Constants;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createNotificationChannels();
    }

    public static App getInstance() {
        return instance;
    }

    private void createNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(Constants.APP_NAME, Constants.NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        channel1.setDescription(Constants.APP_NAME);
        channel1.setShowBadge(true);
        channel1.enableLights(true);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
