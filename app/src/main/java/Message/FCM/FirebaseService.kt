package Message.FCM

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wosong.tinder_final.R


//유저의 토큰정보를 받아와서, firebase 서버로 메세지 명령
//서버에서 메세지 보내주면
//앱에서 메세지를 받는다 -> 앱에서 알림
class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data["title"].toString()
        val body = message.data["body"].toString()

        createNotificationChannel()
        sendNotification(title,body)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "desciption"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("testing", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    private fun sendNotification(title : String, body : String){
        var builder = NotificationCompat.Builder(this, "testing")
            .setSmallIcon(R.drawable.user)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(456,builder.build())
        }

    }

}