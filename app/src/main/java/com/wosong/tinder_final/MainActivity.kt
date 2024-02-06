package com.wosong.tinder_final

import Autentication.IntroActivity
import Autentication.UserDataModel
import Slide.Adapter
import Utils.FirebaseAuthenticationUtils
import Utils.FirebaseReference
import Utils.MyInfo
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import setting.settingActivity

lateinit var cardStackAdapter: Adapter
lateinit var manager : CardStackLayoutManager
private var TAG = "MainActivity"

private val userDataLlist = mutableListOf<UserDataModel>()
private var userCount = 0;
private var uid = FirebaseAuthenticationUtils.getUid()
private var currentUserGender = ""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val LogOutIcon = findViewById<ImageView>(R.id.logOutIcon)
        LogOutIcon.setOnClickListener{

          //  val auth = Firebase.auth
            //auth.signOut()

            val intent = Intent(this,settingActivity::class.java)
            startActivity(intent)
        }//log out

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        manager = CardStackLayoutManager(baseContext, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Right){

                }
                if(direction == Direction.Left){
                    //saving other uid
                    //userDataLlist[userCount].uid.toString() -> current user's uid
                    userLikeOtherUser(uid, userDataLlist[userCount].uid.toString())

                }
                userCount += 1
                if(userCount == userDataLlist.count()){
                    getMyUserData()
                    userCount = 0
                    Toast.makeText(baseContext,"userChanged",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        cardStackAdapter = Adapter(baseContext, userDataLlist)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getMyUserData()
    }//end of Oncreate()

    private fun getMyUserData(){
        val postListener = object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(UserDataModel::class.java)
                currentUserGender = data?.gender.toString()
                //내 닉네임 저장시킴
                MyInfo.myNickname = data?.nickname.toString()
                getUserDataList(currentUserGender)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseReference.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList(currentUserGender : String){
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children ){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user?.gender.equals(currentUserGender)|| userDataLlist.contains(user)){

                    }else{
                        userDataLlist.add(user!!)
                    }
                }

                cardStackAdapter.notifyDataSetChanged()



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseReference.userInfoRef.addValueEventListener(postListener)
    }

    private fun userLikeOtherUser(myUid : String, otherUid : String){

        FirebaseReference.userLikeRef.child(myUid).child(otherUid).setValue("true")

        getOtherUserLikeList(otherUid)

    }

    private fun getOtherUserLikeList(otherUid : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //여기 리스트에서 나의 UID가 있는지 확인해주면 댐
                for(dataModel in dataSnapshot.children ){
                        val LikeUserKey = dataModel.key.toString()
                        //상대 리스트에 내 UID가 존재한다면
                        if(LikeUserKey.equals(uid)){
                            Toast.makeText(this@MainActivity, "matching success",Toast.LENGTH_LONG).show()
                            createNotificationChannel()
                            sendNotification()
                        }
                    }
                }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseReference.userLikeRef.child(otherUid).addValueEventListener(postListener)

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
    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "testing")
            .setSmallIcon(R.drawable.user)
            .setContentTitle("matching success")
            .setContentText("your matching is success")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(456,builder.build())
        }

    }


}