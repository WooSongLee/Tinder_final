package setting

import Autentication.IntroActivity
import Autentication.UserDataModel
import Utils.FirebaseAuthenticationUtils
import Utils.FirebaseReference
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wosong.tinder_final.R
class MypageActivity : AppCompatActivity() {
    private val TAG = MypageActivity::class.java
    private val uid = FirebaseAuthenticationUtils.getUid()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val logoutBtn = findViewById<Button>(R.id.logOutBtn)
        logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            Toast.makeText(this, "sign out successful", Toast.LENGTH_LONG).show()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
        getMyData()
    }


    private fun getMyData(){
        val myImage = findViewById<ImageView>(R.id.myImage)
        val mynickname = findViewById<TextView>(R.id.myNickname)
        val myuid = findViewById<TextView>(R.id.myUid)
        val mycity = findViewById<TextView>(R.id.myCity)
        val mygender = findViewById<TextView>(R.id.myGender)
        val myage = findViewById<TextView>(R.id.MyAge)

// Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                myuid.text = "my uid : " + data?.uid
                mynickname.text = "nickname : "+data?.nickname
                mycity.text = "city : "+data?.city
                mygender.text ="gender : "+ data?.gender
                myage.text ="age : "+ data?.age
                val storageRef = Firebase.storage.reference.child("image/$uid.png")

                storageRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(baseContext)
                            .load(task.result)
                            .placeholder(R.drawable.loading)
                            .into(myImage)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG.toString(), "loadPost:onCancelled", databaseError.toException())
            }
        }
            FirebaseReference.userInfoRef.child(uid).addValueEventListener(postListener)
         }
    }
