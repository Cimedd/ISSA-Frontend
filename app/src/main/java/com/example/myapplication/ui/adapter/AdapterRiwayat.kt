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
import com.example.myapplication.*
import com.example.myapplication.network.TransactionsItem
import com.example.myapplication.ui.CheckOutActivity
import com.example.myapplication.ui.DetailTopupActivity
import com.example.myapplication.ui.DetailTransferActivity
import com.example.myapplication.ui.DetailWithdrawActivity
import com.example.myapplication.ui.SessionManager
import java.text.DecimalFormat

@SuppressLint("RecyclerView")

class AdapterRiwayat(val context: Context, val riwayatList: List<TransactionsItem?>, val id: String): RecyclerView.Adapter<AdapterRiwayat.MyViewHolder>() {
    private lateinit var sessionManager : SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_riwayat_transaksi, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = riwayatList[position]
        val decimalFormat = DecimalFormat("#,###")

        // format date to dd-mm-yyyy HH:mm
        val date = currentItem?.createdAt;

        holder.txtType.text = currentItem?.type
        holder.txtAmount.text = "Rp " + decimalFormat.format(currentItem?.amount).toString()
        holder.txtDate.text = date.toString()
        holder.txtStatus.text = currentItem?.status

        // load icon
//        Picasso.get()
//            .load(currentItem.icon)
//            .into(holder.icon)

        // check if status is success
        when(currentItem?.status) {
            "success" -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.green2))
            "pending" -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.orange))
            else -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.red))
        }

        holder.itemView.setOnClickListener {
            when (currentItem?.type) {
                "Deposit" -> {
                    holder.icon.setImageResource(R.drawable.deposit2)
                    val intent = Intent(context, CheckOutActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "Transfer" -> {
                    if (currentItem.userId.toString() != id){
                        holder.icon.setImageResource(R.drawable.ic_uang_masuk)
                    } else{
                        holder.icon.setImageResource(R.drawable.ic_uang_keluar)
                    }

                    val intent = Intent(context, DetailTransferActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "Withdraw" -> {
                    holder.icon.setImageResource(R.drawable.withdraw2)
                    val intent = Intent(context, DetailWithdrawActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "Topup" -> {
                    holder.icon.setImageResource(R.drawable.voucher)
                    val intent = Intent(context, DetailTopupActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtType: TextView = itemView.findViewById(R.id.txtType)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}