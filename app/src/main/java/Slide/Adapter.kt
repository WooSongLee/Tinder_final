package Slide

import Autentication.UserDataModel
import Utils.FirebaseAuthenticationUtils
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import com.wosong.tinder_final.R

class Adapter (val context : Context, val items : List<UserDataModel>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val image = itemView.findViewById<ImageView>(R.id.profileImageArea)
        val nickname = itemView.findViewById<TextView>(R.id.itemNickname)
        val age = itemView.findViewById<TextView>(R.id.itemAge)
        val city = itemView.findViewById<TextView>(R.id.itemCity)
        val gender = itemView.findViewById<TextView>(R.id.itemGender)
        fun binding(data: UserDataModel){
            val uid = data.uid.toString()

            val storageRef = Firebase.storage.reference.child("image/$uid.png")

            storageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .placeholder(R.drawable.loading)
                        .into(image)
                }
            }
            gender.text = "gender:   " + data.gender
            nickname.text = "name:   " + data.nickname
            age.text = "age:   " + data.age
            city.text = "city:   " + data.city
        }
    }
}