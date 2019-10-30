package cyril.cieslak.codelabsnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cyril.cieslak.codelabsnotification.MainActivity.NotificationReceiver


class MainActivity : AppCompatActivity() {

    lateinit var button_notifity: Button
    lateinit var button_update: Button
    lateinit var button_cancel: Button


    lateinit var mNotifyManager: NotificationManager
    lateinit var mReceiver: NotificationReceiver


    val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    val NOTIFICATION_ID = 0
    val ACTION_UPDATE_NOTIFICATION =
        "com.example.android.codelabsnotification.ACTION_UPDATE_NOTIFICATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_notifity = findViewById(R.id.notify)
        button_update = findViewById(R.id.update)
        button_cancel = findViewById(R.id.cancel)

        setNotificationButtonState(true, false, false)

        mReceiver = NotificationReceiver()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))


        createNotificationChannel()

        button_notifity.setOnClickListener {
            sendNotification()
        }

        button_update.setOnClickListener() {

            updateNotification()
        }

        button_cancel.setOnClickListener() {

            cancelNotification()
        }


    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }


    fun sendNotification() {

        setNotificationButtonState(false, true, true)

        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            updateIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

     //   notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent)


        var notifyBuilder: Notification.Builder = getNotificationBuilder()
        notifyBuilder.addAction(R.drawable.ic_action_name, "Update Notification", updatePendingIntent)
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())

    }

    fun createNotificationChannel() {

        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED)
            notificationChannel.enableVibration(true)
            notificationChannel.setDescription("Notification from Mascot")
            mNotifyManager.createNotificationChannel(notificationChannel)

        }


    }

    private fun getNotificationBuilder(): Notification.Builder {

        val notificationIntent = Intent(this, MainActivity::class.java)

        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        var notifyBuilder: Notification.Builder = Notification.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)


        return notifyBuilder
    }

    private fun updateNotification() {

        setNotificationButtonState(false, false, true)

        var androidImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.mascot_11)

        var notifyBuilder = getNotificationBuilder()

        notifyBuilder.setStyle(
            Notification.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!")
        )
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())

    }

    private fun cancelNotification() {

        setNotificationButtonState(true, false, false)

        mNotifyManager.cancel(NOTIFICATION_ID)

    }

    fun setNotificationButtonState(
        isNotifyEnabled: Boolean,
        isUpdateEnabled: Boolean,
        isCancelEnabled: Boolean
    ) {
        button_notifity.setEnabled(isNotifyEnabled)
        button_update.setEnabled(isUpdateEnabled)
        button_cancel.setEnabled(isCancelEnabled)
    }


    inner class NotificationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }


    }


}
