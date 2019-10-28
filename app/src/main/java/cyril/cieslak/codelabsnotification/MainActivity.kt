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


class MainActivity : AppCompatActivity() {

    lateinit var button_notifity: Button
    lateinit var mNotifyManager: NotificationManager


    val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_notifity = findViewById(R.id.notify)

        createNotificationChannel()

        button_notifity.setOnClickListener {
            sendNotification()
        }


    }

    fun sendNotification() {


        var notifyBuilder: Notification.Builder = getNotificationBuilder()
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

}
