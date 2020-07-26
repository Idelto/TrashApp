package com.trash2money.trashapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.R
import com.google.firebase.storage.FirebaseStorage

class CoupomKartAdapter(val couponKartList: ArrayList<CouponItem>) :
    RecyclerView.Adapter<CoupomKartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_coupon_kart, parent, false)
        val v2 = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_coupon_kart, parent, false)

        return ViewHolder(v, v2)
    }

    override fun getItemCount(): Int {
        return couponKartList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val kartItem: CouponItem = couponKartList[position]
        holder.txtCouponKartBrand.text = kartItem.brand
        holder.txtCouponKartDescription.text = kartItem.description
        holder.txtCouponKartCost.text = kartItem.cost.toString()
        Log.d("TAG", kartItem.img.toString())

        //Setando a imagem para o Coupon

        val storage = FirebaseStorage.getInstance().reference
        val targetUrl = storage.toString() + "coupon_logo/" + kartItem.img
        val storageRef = storage.storage.getReferenceFromUrl(targetUrl)

        storageRef.downloadUrl.addOnSuccessListener { url ->

            val imgUrl = url
            Glide.with(holder.parent).load(imgUrl).into(holder.imgCouponKartBrand)

        }
    }

    class ViewHolder(v: View, v2: View) : RecyclerView.ViewHolder(v) {

        var parent = v.rootView

        var imgCouponKartBrand = v.findViewById<ImageView>(R.id.imgMyCouponBrand) as ImageView
        var txtCouponKartBrand = v.findViewById<TextView>(R.id.txtTCoin) as TextView
        var txtCouponKartDescription = v.findViewById<TextView>(R.id.txtPeso) as TextView
        var txtCouponKartCost = v.findViewById<TextView>(R.id.txtWDay) as TextView
        var activity = v.context
    }
}
