package com.trash2money.trashapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.R
import com.trash2money.trashapp.KClasses.TCoinsKartItem

class TCoinsKartAdapter(val list: ArrayList<TCoinsKartItem>) :
    RecyclerView.Adapter<TCoinsKartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflar o layout do Recycler view Trash_cart
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_trash_cart, parent, false)
        return ViewHolder(v)
    }

    //Define o tamanho da lista
    override fun getItemCount(): Int {
        return list.size
    }

    // realiza as ações dentro do Recycler view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val shoplist: TCoinsKartItem = list[position]
        holder.category.text = shoplist.category
        holder.qty.text = shoplist.qty
        holder.tCoins.text = shoplist.tCoins
        holder.tara.text = shoplist.tara
        holder.image.setImageResource(holder.context!!.resources.getIdentifier(shoplist!!.image,
            "drawable",
            holder.context.packageName))

    }

    //Conexão do Recycler view com objetos do layout
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById(R.id.imageView) as ImageView
        val category = itemView.findViewById(R.id.txt1) as TextView
        val qty = itemView.findViewById(R.id.txt12) as TextView
        val tCoins = itemView.findViewById(R.id.txt13) as TextView
        val tara = itemView.findViewById(R.id.txt14) as TextView
        val context = itemView.context

    }

}

