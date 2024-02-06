package Message

import Autentication.UserDataModel
import Message.FCM.RetrofitInstance
import Message.FCM.notiModel
import Message.FCM.pushNotification
import Utils.FirebaseAuthenticationUtils
import Utils.FirebaseReference
import Utils.MyInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wosong.tinder_final.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher


class MyLikeListActivity : AppCompatActivity() {
    private val LikeUserUidLlist = mutableListOf<String>()
    private val LikeUserLlist = mutableListOf<UserDataModel>()
    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthenticationUtils.getUid()
    lateinit var listViewAdapter: LIstViewAdapter
    lateinit var getterUid : String
    lateinit var getterToken : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        //리스트뷰 연결
        val userListView = findViewById<ListView>(R.id.userListView)
        listViewAdapter = LIstViewAdapter(this, LikeUserLlist)
        userListView.adapter = listViewAdapter

        //내가 좋아요 한 사람의 List
        getMyLikeList()

        //listView click listener
        //리스트뷰 클릭 시에 매칭 된 회원인지 판단 -> 비동기적 처리
        userListView.setOnItemClickListener { parent, view, position, id ->

            getterUid = LikeUserLlist[position].uid.toString()
            checkMatching(LikeUserLlist[position].uid.toString())
            getterToken = LikeUserLlist[position].token.toString()





        }






    }

    private fun checkMatching(otherUid : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isMatching = false

                for(dataModel in dataSnapshot.children){
                    val likeUserKey = dataModel.key.toString()
                    //user key 값에 내 uid가 있는지 확인
                    if(likeUserKey.contains(uid)){
                        isMatching = true //찾으면 break
                        break
                    }
                }
                if(isMatching){ //매칭되었으면 dialog띄우기
                    showDialog()
                    Toast.makeText(baseContext,"matching success", Toast.LENGTH_SHORT).show()


                }else{
                    Toast.makeText(baseContext,"matching failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        FirebaseReference.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }

    private fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //여기 리스트에서 나의 UID가 있는지 확인해주면 댐
                for(dataModel in dataSnapshot.children ){
                    //내가 좋아요 한 사람의 uid가 list에 들어있음
                    LikeUserUidLlist.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseReference.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    //내가 좋아요 한 사람의 정보를 리스트에 저장시킴
    private fun getUserDataList(){
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children ){

                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(LikeUserUidLlist.contains(user?.uid)){
                        LikeUserLlist.add(user!!)
                    }

                }
                listViewAdapter.notifyDataSetChanged()
                Log.d(TAG, LikeUserLlist.toString())


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseReference.userInfoRef.addValueEventListener(postListener)
    }

    private fun testPush(notification : pushNotification) = CoroutineScope(Dispatchers.IO).launch {
        //pushNotification == notiModel + token
        RetrofitInstance.api.postNotification(notification)
    }


    private fun showDialog(){
        val mDialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBulider = AlertDialog.Builder(this)
            .setView(mDialog)
            .setTitle("send message")

        val mAlertDialog = mBulider.show()
        val textArea = mAlertDialog.findViewById<EditText>(R.id.sendTextArea)
        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtn)

        btn?.setOnClickListener {
            val msgModel = MsgModel(textArea!!.text.toString(),MyInfo.myNickname)
            FirebaseReference.userMsgRef.child(getterUid).push().setValue(msgModel)

            val notiModel = notiModel(MyInfo.myNickname,textArea.text.toString())

            val pushModel = pushNotification(notiModel,getterToken)

            testPush(pushModel)

            mAlertDialog.dismiss()
        }
    }

}