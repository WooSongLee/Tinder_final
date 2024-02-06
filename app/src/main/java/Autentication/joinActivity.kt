package Autentication


import Utils.FirebaseReference
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.wosong.tinder_final.MainActivity
import com.wosong.tinder_final.R
import java.io.ByteArrayOutputStream

private lateinit var auth: FirebaseAuth

lateinit var profileImage : ImageView

private val TAG = "tag"
private var nickname = ""
private var gender = ""
private var uid = ""
private var city = ""
private var age = ""


class joinActivity : AppCompatActivity() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth
        profileImage = findViewById(R.id.imageArea)

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)
            }
        )

        profileImage.setOnClickListener{
            getAction.launch("image/*")
        }

        var email = findViewById<TextInputEditText>(R.id.emailArea)
        var password = findViewById<TextInputEditText>(R.id.passwordArea)

        var nickname = findViewById<TextInputEditText>(R.id.nicknameArea)
        var gendercheck = findViewById<ToggleButton>(R.id.genderArea)
        var city = findViewById<TextInputEditText>(R.id.cityArea)
        var age = findViewById<TextInputEditText>(R.id.ageArea)



        var joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->

                    val user = auth.currentUser
                    uid = user?.uid.toString()

                    if(gendercheck.isChecked){
                        gender = "male"
                    }else{
                        gender = "female"
                    }

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = task.result
                                Log.e(TAG, token.toString())

                                val userModel = UserDataModel(
                                    uid,
                                    nickname.text.toString(),
                                    gender,
                                    city.text.toString(),
                                    age.text.toString(),
                                    email.text.toString(),
                                    password.text.toString(),
                                    token
                                )

                                FirebaseReference.userInfoRef.child(uid).setValue(userModel)

                                upLoadImage()


                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)

                            })


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "join failed", Toast.LENGTH_LONG).show()

                    }
                }
        }//end of click listener

    }
    private fun upLoadImage(){
        val storage = Firebase.storage
        val storageRef = storage.reference.child("image/$uid.png")
        // Get the data from an ImageView as bytes
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}