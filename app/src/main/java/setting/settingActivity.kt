package setting

import Message.MyLikeListActivity
import Message.MyMessageActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.wosong.tinder_final.R

class settingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val myBtn = findViewById<Button>(R.id.myPageBtn)
        myBtn.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

        val myLikeListBtn = findViewById<Button>(R.id.myLikeListBtn)
        myLikeListBtn.setOnClickListener {
            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }

        val myMessageBtn = findViewById<Button>(R.id.myMessageArea)
        myMessageBtn.setOnClickListener {
            val intent = Intent(this, MyMessageActivity::class.java)
            startActivity(intent)
        }
    }
}