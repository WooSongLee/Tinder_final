package com.wosong.tinder_final

import Autentication.IntroActivity
import Utils.FirebaseAuthenticationUtils
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class SplashActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
           Log.e("token : " , token.toString())
        })


        //val uid = auth.currentUser?.uid.toString()
        val uid = FirebaseAuthenticationUtils.getUid()

        if(uid == "null"){
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
        //사용자의 uid값을 통해서 null일 경우 비로그인으로 판단, null이 아닐 경우에는 로그인 되있는 것으로 판단함.







    }
}