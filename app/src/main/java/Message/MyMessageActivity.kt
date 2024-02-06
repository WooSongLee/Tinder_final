package Message

import Autentication.UserDataModel
import Utils.FirebaseAuthenticationUtils
import Utils.FirebaseReference
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wosong.tinder_final.R

class MyMessageActivity : AppCompatActivity() {

    lateinit var ListViewAdapter: MsgAdapter
    val msgList = mutableListOf<MsgModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_message)

        val listView = findViewById<ListView>(R.id.msgListView)
        ListViewAdapter = MsgAdapter(this, msgList)
        listView.adapter = ListViewAdapter
        getMyMessage()

    }

    //get msg from database
    private fun getMyMessage(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                msgList.clear()

                for(dataModel in dataSnapshot.children ){
                    //msgmodel 형태로 받아온다.
                    val msgModel = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msgModel!!)
                }

                //메세지 순서 최신순으로
                msgList.reverse()

                ListViewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FirebaseReference.userMsgRef.child(FirebaseAuthenticationUtils.getUid()).addValueEventListener(postListener)
    }
}