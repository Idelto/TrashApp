package com.trash2money.trashapp.Adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.KClasses.OnGoingItem
import com.trash2money.trashapp.R
import com.google.firebase.firestore.FirebaseFirestore

class MyRequestHistoryAdapter(val onGoingList: ArrayList<OnGoingItem>) :
    RecyclerView.Adapter<MyRequestHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_my_request_tab_history, parent, false)
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
            33 -> status = "Aguardando Coletor \uD83D\uDE07"
            66 -> status = "Coletor a Caminho \uD83D\uDE9C"
            100 -> status = "Coletado  \uD83C\uDFC6"
        }

        Log.d("TAG STATUS ", status)
        val path = onGoingItem.trashID
        val ref = FirebaseFirestore.getInstance().document(path)

        ref.get().addOnSuccessListener {doc->
            if(doc["rating"]!= null){
                holder.rating_exp.visibility = View.VISIBLE
                holder.rating_exp.rating = doc["rating"].toString().toFloat()
                holder.btnAvaliar.visibility = View.INVISIBLE
            }
        }

        holder.txtStatus.text = status
        /*holder.btnAvaliar.visibility = View.VISIBLE*/

        holder.btnAvaliar.setOnClickListener {
            Log.d("Click", "Clicado")

            val builder = AlertDialog.Builder(holder.context)
            val dialogView = LayoutInflater.from(holder.context).inflate(R.layout.dialog_rating, null)
            builder.setCancelable(false)
            builder.setView(dialogView)

            val btnAvaliar = dialogView.findViewById<Button>(R.id.btnRatingAvaliar)
            val ra = dialogView.findViewById<RatingBar>(R.id.ratingBar)


            ra.setOnClickListener {
                Toast.makeText(holder.context, "Clicado", Toast.LENGTH_LONG).show()
                Log.d("path", path)
            }

            val dialog = builder.create() as AlertDialog

            btnAvaliar.setOnClickListener() {

                ref.update("rating", ra.rating).addOnSuccessListener {
                    Log.d("Updated", ra.rating.toString())
                    holder.rating_exp.rating = ra.rating
                    holder.rating_exp.visibility = View.VISIBLE
                }

                dialog.cancel()

            }

            dialog.show()

        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var txtWDay = v.findViewById<TextView>(R.id.txtWDay) as TextView
        var txtDate = v.findViewById<TextView>(R.id.txtData2) as TextView
        var txtweight = v.findViewById<TextView>(R.id.txtPeso) as TextView
        var txttrashCoins = v.findViewById<TextView>(R.id.txtTCoin) as TextView
        var progressBar2 = v.findViewById<ProgressBar>(R.id.progressBar2) as ProgressBar
        var txtStatus = v.findViewById<ProgressBar>(R.id.txtStatus) as TextView
        //var txt_label_rating =v.findViewById<TextView>(R.id.txt_label_rating)
        //var rating_exp = v.findViewById<RatingBar>(R.id.rating_exp)
        //var btn_rating = v.findViewById<Button>(R.id.btn_rating) as Button
        var btnAvaliar = v.findViewById<Button>(R.id.btn_avaliar)
        var rating_exp = v.findViewById<RatingBar>(R.id.rating_exp)
        var context = v.context

    }

}

