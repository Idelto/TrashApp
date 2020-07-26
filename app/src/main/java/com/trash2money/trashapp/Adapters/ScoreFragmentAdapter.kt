package com.trash2money.trashapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.KClasses.ScoreList
import com.trash2money.trashapp.R

class ScoreFragmentAdapter(val scoreList: ArrayList<ScoreList>, val btn : Int): RecyclerView.Adapter<ScoreFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_score,parent,false)
        Log.d("OnCreate Parent Context",parent.context.toString())
        Log.d("On Create Parent",parent.toString())

        val btn = btn
        return ViewHolder(v,btn)
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val scoreList: ScoreList = scoreList[position]

        holder.nome.text = scoreList.name

        //tst

        when(btn){
            1 ->{
                holder.score.text = scoreList.tcoins.toString()
                holder.pos.text = (position +1).toString()
            }
            2->{
                holder.score.text = scoreList.weight.toString()
                holder.pos.text = (position +1).toString()
            }
            3->{
                holder.score.text = scoreList.num.toString()
                holder.pos.text = (position +1).toString()
            }
            4->{
                holder.score.text = scoreList.itens.toString()
                holder.pos.text = (position +1).toString()
            }

        }



    }

    class ViewHolder(v : View, btn: Int) : RecyclerView.ViewHolder(v){

        val nome = v.findViewById<TextView>(R.id.txtNome)
        val score = v.findViewById<TextView>(R.id.txt_score)
        val pos = v.findViewById<TextView>(R.id.txt_pos)
        val btn = btn

    }

}


