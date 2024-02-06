package Autentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wosong.tinder_final.MainActivity
import com.wosong.tinder_final.R


private lateinit var auth: FirebaseAuth



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val email = findViewById<TextInputEditText>(R.id.emailArea)
        val password = findViewById<TextInputEditText>(R.id.passwordArea)

        val loginBtn = findViewById<Button>(R.id.LoginButton)
        loginBtn.setOnClickListener{
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       Toast.makeText(this, "login successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "sign in fails", Toast.LENGTH_LONG).show()

                    }
                }
        }
    }
}