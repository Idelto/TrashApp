package com.trash2money.trashapp.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.TrashItemDetailsActivity
import com.trash2money.trashapp.KClasses.OnGoingItem
import com.trash2money.trashapp.R

class MyRequestOnGoingAdapter(val onGoingList: ArrayList<OnGoingItem>) :
    RecyclerView.Adapter<MyRequestOnGoingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_my_request_tab_ongoing, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return onGoingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val onGoingItem: OnGoingItem = onGoingList[position]

        holder.txtDate.text = onGoingItem.date
        holder.txtweight.text = onGoingItem.weight
        holder.txttrashCoins.text = onGoingItem.trasCoins
        holder.txtWDay.text = onGoingItem.wDay
        holder.progressBar2.progress = onGoingItem.progress

        var status = "" as String
        when (onGoingItem.progress) {

            0 -> status = "Lixeira Vazia \uD83D\uDE11"
            30 -> status = "Aguardando Coletor \uD83D\uDE07"
            50 -> status = "Solicitação e Agendamento \uD83D\uDE07"
            70 -> status = "Agendamento Confirmado \uD83D\uDE07"
            80 -> status = "Coletor a Caminho \uD83D\uDE9C"
            100 -> status = "Coletado  \uD83C\uDFC6"
        }

        Log.d("TAG STATUS ", status)

        holder.txtStatus.text = status
        holder.btnDetails.setOnClickListener {

            val intent = Intent(holder.parent.context, TrashItemDetailsActivity::class.java)
            intent.putExtra("strPath", onGoingItem.trashID)
            Log.d("trashid2", "${onGoingItem.trashID}")
            holder.parent.context.startActivity(intent)
        }



    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var txtWDay = v.findViewById<TextView>(R.id.txtWDay) as TextView
        var txtDate = v.findViewById<TextView>(R.id.txtData2) as TextView
        var txtweight = v.findViewById<TextView>(R.id.txtPeso) as TextView
        var txttrashCoins = v.findViewById<TextView>(R.id.txtTCoin) as TextView
        var progressBar2 = v.findViewById<ProgressBar>(R.id.progressBar2) as ProgressBar
        var txtStatus = v.findViewById<ProgressBar>(R.id.txtStatus) as TextView
        var btnDetails = v.findViewById<Button>(R.id.btnDetails) as Button
        var parent = v.rootView
        var context = v.context
    }

}

