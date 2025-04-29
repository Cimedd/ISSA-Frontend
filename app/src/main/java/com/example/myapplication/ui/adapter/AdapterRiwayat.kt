package com.example.myapplication.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
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
import com.example.myapplication.util.Utility
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

        val date = Utility.formatDateOnly(currentItem?.createdAt ?: "")

        holder.txtType.text =  Utility.capitalizeFirstLetter(currentItem?.type ?: "")
        holder.txtDate.text = date
        holder.txtStatus.text = currentItem?.status

        // check if status is success
        when(currentItem?.status) {
            "success" -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.green2))
            "pending" -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.orange))
            else -> holder.txtStatus.setTextColor(context.resources.getColor(R.color.red))
        }
        holder.icon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)


        when(currentItem?.type){
            "deposit" -> {
                holder.txtAmount.text = "+ Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                holder.icon.setImageResource(R.drawable.deposit2)
            }
            "transfer" -> {
                if (currentItem.userId.toString() != id){
                    holder.txtAmount.text = "+ Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                    holder.icon.setImageResource(R.drawable.ic_uang_masuk)
                } else{
                    holder.txtAmount.text = "- Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                    holder.icon.setImageResource(R.drawable.ic_uang_keluar)
                }
            }
            "withdraw" -> {
                holder.txtAmount.text = "- Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                holder.icon.setImageResource(R.drawable.withdraw2)
            }
            "topup" -> {
                holder.icon.setImageResource(R.drawable.voucher)
                holder.txtAmount.text = "-Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
            }
        }

        holder.itemView.setOnClickListener {
            when (currentItem?.type) {
                "deposit" -> {
                    holder.txtAmount.text = "+ Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                    holder.icon.setImageResource(R.drawable.deposit2)
                    val intent = Intent(context, CheckOutActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "transfer" -> {
                    if (currentItem.userId.toString() != id){
                        holder.txtAmount.text = "+ Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                        holder.icon.setImageResource(R.drawable.ic_uang_masuk)
                    } else{
                        holder.txtAmount.text = "- Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                        holder.icon.setImageResource(R.drawable.ic_uang_keluar)
                    }

                    val intent = Intent(context, DetailTransferActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "withdraw" -> {
                    holder.txtAmount.text = "- Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
                    holder.icon.setImageResource(R.drawable.withdraw2)
                    val intent = Intent(context, DetailWithdrawActivity::class.java)
                    intent.putExtra("transaction", currentItem)
                    startActivity(context, intent, null)
                }
                "topup" -> {
                    holder.icon.setImageResource(R.drawable.voucher)
                    holder.txtAmount.text = "-Rp " + Utility.moneyFormat(currentItem?.amount ?: 0)
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