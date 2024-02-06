package Autentication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wosong.tinder_final.MainActivity
import com.wosong.tinder_final.R

private lateinit var auth : FirebaseAuth

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val isTiramisuOrHigher = Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU
        val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

        var hasNotificationPermission =
            if (isTiramisuOrHigher)
                ContextCompat.checkSelfPermission(this, notificationPermission) == PackageManager.PERMISSION_GRANTED
            else true

        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            hasNotificationPermission = it

        }
        if (!hasNotificationPermission) {
            launcher.launch(notificationPermission)
        }
        auth = Firebase.auth

        val joinBtn = findViewById<Button>(R.id.JoinBtn)
        joinBtn.setOnClickListener{
            val intent = Intent(baseContext, joinActivity::class.java)
            startActivity(intent)
        }//join btn을 누르면 joinActivity로 이동
        val LoginBtn = findViewById<Button>(R.id.LoginBtn)
        LoginBtn.setOnClickListener {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
        val adminBtn = findViewById<Button>(R.id.developeBtn)
        adminBtn.setOnClickListener{
            val intent = Intent(baseContext,MainActivity::class.java)
            startActivity(intent)
        }


    }
}