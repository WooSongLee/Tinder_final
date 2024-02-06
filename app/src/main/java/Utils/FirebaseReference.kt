package Utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseReference {

    companion object{
        val database = Firebase.database
        val userInfoRef = database.getReference("userInfoRef")
        val userLikeRef = database.getReference("userLikeRef")
        val userMsgRef = database.getReference("userMsg")

    }
}