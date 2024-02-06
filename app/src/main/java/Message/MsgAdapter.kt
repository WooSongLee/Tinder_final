package Message

import Autentication.UserDataModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wosong.tinder_final.R

class MsgAdapter(val context : Context, val items : MutableList<MsgModel>)  :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }
        val nicknameArea = convertView!!.findViewById<TextView>(R.id.listViewItemNicknameArea)
        val nickname = convertView!!.findViewById<TextView>(R.id.ListViewitemNickname)

        nickname.text = items[position].senderText
        nicknameArea.text = items[position].sendInfo

        return convertView!!
    }
}