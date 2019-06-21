package by.yakivan.joke;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class MainService extends IntentService {
    public static final String SERVICE_NAME = "MainService";
    public static final String EXTRA_COMMAND_TYPE = "CommandType";
    public static final int NOTIFICATION_ID = 5453;

    public MainService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "err desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("31423", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        CommandType command = CommandType.valueOf(intent.getStringExtra(EXTRA_COMMAND_TYPE));
        switch (command) {
            case Music:
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gachibass);
                mediaPlayer.start();
                break;
            case LogMessage:
                showText(getString(R.string.response));
                break;
            case Notification:
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "31423")
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(getString(R.string.log_question))
                        .setContentText(getString(R.string.response))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{0, 1000})
                        .setAutoCancel(true);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
                break;
        }
    }

    private void showText(String text) {
        Log.e(SERVICE_NAME, "This a message: " + text);
    }

    public enum CommandType {
        Music,
        LogMessage,
        Notification
    }
}
