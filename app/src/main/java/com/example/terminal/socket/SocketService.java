package com.example.terminal.socket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.terminal.socket.Socket;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;

@Singleton
@AndroidEntryPoint
public class SocketService extends Service {

    private final int FOREGROUND_ID = 1223;
    private final String NOTIFICATION_CHANNEL_ID = "com.hellenic.iscan", CHANNEL_NAME = "ClientSocket";
    private Thread socketThread;

    @Inject
    Socket socket;

    @Inject
    public SocketService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SocketService", "Running..");

        buildForegroundNotification();
        //startSocket();

    }

    public void restartSocket(){

        Log.i("SocketService", "Restarting..");
        stopSocket();
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            Log.e("Service",  e.toString());
        }
        startSocket();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSocket();

    }

    public void startSocket(){
        Log.i("SocketService", "Starting socket..");
        if (socketThread != null)
            socketThread.interrupt();
        socketThread = new Thread(socket);
        socketThread.start();
    }

    public void stopSocket(){
        Log.i("SocketService", "Stopping socket..");
        if(socketThread != null){
            if(!socketThread.isInterrupted()){
                socketThread.interrupt();
                socketThread = null;
            } else Log.e("SocketService", " Has already been interrupted.");
        } else Log.e("SocketService",   " Is not running.");
    }

    private void buildForegroundNotification() {
        //Build notification channel
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running")
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(FOREGROUND_ID, notification);
    }
}
