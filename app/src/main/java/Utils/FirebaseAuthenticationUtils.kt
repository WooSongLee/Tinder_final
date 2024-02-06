package Utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthenticationUtils {

    companion object{
        private lateinit var auth : FirebaseAuth
        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
    }
}