package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.DataContact
import com.example.myapplication.ui.SessionManager
import com.example.myapplication.ui.Transfer2Activity
import com.example.myapplication.dataclass.DataRecentUser
import com.example.myapplication.dataclass.DataUser
import com.squareup.picasso.Picasso

@SuppressLint("RecyclerView")

class AdapterRecentUser(val context: Context, val userList: List<DataContact>): RecyclerView.Adapter<AdapterRecentUser.MyViewHolder>() {
    private lateinit var sessionManager : SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_user_recent, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sessionManager = SessionManager(context)
        val currentItem = userList[position]

        holder.txtName.text = currentItem.name

        // load icon
        Picasso.get()
            .load("https://i.pravatar.cc/300")
            .into(holder.photoProfile)

        // on click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Transfer2Activity::class.java)
            intent.putExtra("nohp", currentItem.phoneNumber)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("id", currentItem.id);
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val photoProfile: ImageView = itemView.findViewById(R.id.photoProfile)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
    }
}