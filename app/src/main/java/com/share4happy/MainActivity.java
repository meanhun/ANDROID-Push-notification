package com.share4happy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "CHANEL_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

    }
    BroadcastReceiver broadcastReceiverWifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE
            );
            // nếu có internet thì thông báo đẩy "Bạn đã kết nối internet"
            if (connectivityManager.getActiveNetworkInfo()!=null){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(context,MainActivity.CHANNEL_ID)
                        .setContentTitle("Có thông báo")
                        .setContentText("Đã có version mới, mời bạn nhấn vào để cập nhật")
                        .setSmallIcon(R.drawable.ic_iconc);
                Intent intent2 = new Intent(context,MainActivity.class);
                PendingIntent resuilIntent = PendingIntent.getActivity(
                        context,0,
                        intent2,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                notification.setContentIntent(resuilIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        Context.NOTIFICATION_SERVICE);
                notificationManager.notify(getNotificanionID(),notification.build());
            }

        }
    };
    private int getNotificanionID() {
        return (int) new Date().getTime();
    }
    //Đăng kí broadcastReceiver


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiverWifi,intentFilter);
    }
    //Hủy Đăng kí BroadcastReciveer


    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiverWifi!=null){
            unregisterReceiver(broadcastReceiverWifi);
        }
    }



    private void addControls() {
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}