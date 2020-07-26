package com.trash2money.trashapp.Adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.KClasses.TCoinsBalanceItem
import com.trash2money.trashapp.R
import java.text.DecimalFormat

class TCoinsBalanceAdapter(val TCoinsBalanceList: ArrayList<TCoinsBalanceItem>) :
    RecyclerView.Adapter<TCoinsBalanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_trash_coins_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return TCoinsBalanceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val TCoinsBalanceList: TCoinsBalanceItem = TCoinsBalanceList[position]

        holder.txtData.text = TCoinsBalanceList.date
        holder.txtSaldo.text = TCoinsBalanceList.value

        Log.d("Valor Saldo", TCoinsBalanceList.value.toString())

        val saldo = holder.txtSaldo.text as String
        Log.d("TAG", saldo)

        var nf = DecimalFormat("#.00")

        val saldo2 = saldo.replace(",", ".")
        Log.d("TAG2", saldo2)

        if (saldo2.toFloat() < 0F) {//holder.txtSaldo.text.toString().toFloat() < 0){

            holder.txtSaldo.setTextColor(Color.parseColor("#DF2C56"))
            holder.txtStatus.setTextColor(Color.parseColor("#DF2C56"))
            holder.txtStatus.text = "Retirado"
            Log.d("Valor Saldo", holder.txtSaldo.toString())

        } else {

            holder.txtStatus.text = "Recebido"
            holder.txtSaldo.setTextColor(Color.parseColor("#CDDC39"))
            holder.txtStatus.setTextColor(Color.parseColor("#CDDC39"))

        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var txtData = v.findViewById(R.id.txtWDay) as TextView
        var txtSaldo = v.findViewById(R.id.txtSaldo) as TextView
        var txtStatus = v.findViewById(R.id.txtStatus) as TextView

    }

}