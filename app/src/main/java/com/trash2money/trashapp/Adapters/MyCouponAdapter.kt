package com.trash2money.trashapp.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.R
import com.google.firebase.storage.FirebaseStorage

class MyCouponAdapter (val myCouponList:ArrayList<CouponItem>):RecyclerView.Adapter<MyCouponAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_my_coupon,parent,false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {

        return myCouponList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val myCouponItem = myCouponList[position]

        holder.txtDescription.text = myCouponItem.description
        holder.txtValidate.text = myCouponItem.validate
        holder.txtBrand.text = myCouponItem.brand
        //holder.imgBrand.setImageResource(myCouponItem.img)

        val storage = FirebaseStorage.getInstance().reference
        val targetUrl = storage.toString()+"coupon_logo/"+myCouponItem.img
        val storageRef = storage.storage.getReferenceFromUrl(targetUrl)

        storageRef.downloadUrl.addOnSuccessListener {url->

            val imgUrl = url
            Glide.with(holder.parent).load(imgUrl).into(holder.imgBrand)

        }

        holder.imgBrand.setOnClickListener(){


            val cupomId = myCouponItem.id
            Log.d("txtlog3"," $cupomId ")
            val intent = Intent(holder.parent.context,HomeActivity::class.java)
            intent.putExtra("CouponId",cupomId)
            intent.putExtra("fragment", "FragmentSelectedCouponMain")
            holder.parent.context.startActivity(intent)
        }


    }

    class ViewHolder (v:View):RecyclerView.ViewHolder(v) {

        var txtDescription = v.findViewById<TextView>(R.id.txtWDay)
        var txtBrand = v.findViewById<TextView>(R.id.txtTCoin)
        var txtValidate = v.findViewById<TextView>(R.id.txtPeso)
        var imgBrand = v.findViewById<ImageView>(R.id.imgMyCouponBrand)
        //var imgInfo = v.findViewById<ImageView>(R.id.imgCouponInfo)

        var parent = v.rootView

    }
}
