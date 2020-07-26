package com.trash2money.trashapp.Adapters

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.KClasses.ConvertTo
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.KClasses.GetCoupon
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.math.BigDecimal
import java.math.RoundingMode


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CouponAdapter(val couponList: ArrayList<CouponItem>) :
    RecyclerView.Adapter<CouponAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_coupon, parent, false)
        Log.d("OnCreate Parent Context", parent.context.toString())
        Log.d("On Create Parent", parent.toString())
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return couponList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val couponItem: CouponItem = couponList[position]

        holder.txtCouponDescription.text = couponItem.brand
        holder.txtCouponShortDescription.text = couponItem.description
        //holder.txtCouponValidate.text = couponItem.validate
        holder.txtCouponValue.text = couponItem.cost.toString()
        holder.txt_disc_cp.text = couponItem.disc + "%"
        val discout = couponItem.disc.toDouble() / 100
        val init_value = couponItem.value?.toDouble()
        var final_value = init_value * (1.0 - discout)

        final_value = BigDecimal(final_value).setScale(2, RoundingMode.HALF_DOWN).toDouble()
        var discount_value = init_value - final_value
        Log.d("Valores------", couponItem.description)
        Log.d("Valores Desconto", discout.toString())
        Log.d("Valores Valor Inicial", init_value.toString())
        Log.d("Valores Final", final_value.toString())

        holder.txt_discount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        Log.d("Valores value", couponItem.value.toString())

        if (init_value == 0.0) {
            Log.d("Valores value", "cheguei aqui")
            holder.txt_discount.visibility = View.INVISIBLE
            holder.txt_new_value.visibility = View.INVISIBLE
            holder.txt_introValue1.visibility = View.INVISIBLE
            holder.txt_introValue2.visibility = View.INVISIBLE
        } else {
            holder.txt_discount.setText(ConvertTo().convert2casas(init_value))
            holder.txt_new_value.setText(ConvertTo().convert2casas(final_value))
        }

        if (couponItem.validate == "") {
            holder.txt_validate.visibility = View.INVISIBLE
            holder.txt_introValidate.visibility = View.INVISIBLE
        } else {
            holder.txt_validate.setText(couponItem.validate)
        }

        Log.d("txt", couponItem.id)

        GetCoupon().readCoupon(object : GetCoupon.MyCallbackCoupon {
            override fun onCallBack(coupon: CouponItem) {
                var impressions = coupon.impressions
                impressions += 1
                Log.d("impressions", impressions.toString())
                FirebaseFirestore.getInstance().collection("coupons").document(coupon.id)
                    .update("impressions", impressions)
            }
        }, couponItem.id)

        val myStorage = FirebaseStorage.getInstance().reference
        Log.d("INSTANCE", myStorage.toString())
        Log.d("CAMINHO", couponItem.img.toString())
        Log.d("OTHER", couponItem.disc.toString())

        val targetUrl = myStorage.toString() + "coupon_logo/" + couponItem.img
        val storageRef = myStorage.storage.getReferenceFromUrl(targetUrl)
        Log.d("INSTANCE REF", storageRef.toString())
        storageRef.downloadUrl.addOnSuccessListener { url ->
            Log.d("URL", url.toString())
            val imageUrl = url
            Glide.with(holder.parent).load(imageUrl).into(holder.imgCouponImg)

        }.addOnFailureListener { e ->
                Log.e("ERRO", e.message.toString())
            }

        holder.imgCouponImg.setOnClickListener() {

            val intent = Intent(holder.parent.context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentCouponDetails")
            intent.putExtra("couponTarget", couponItem.id)
            intent.putExtra("coupontBrand", couponItem.brand)

            holder.parent.context.startActivity(intent)

            Log.d("TAGID", "cheguei aqui")

            GetCoupon().readCoupon(object : GetCoupon.MyCallbackCoupon {
                override fun onCallBack(coupon: CouponItem) {
                    val views = coupon.views
                    FirebaseFirestore.getInstance().collection("coupons").document(coupon.id)
                        .update("views", views + 1)
                    val currUser = FirebaseAuth.getInstance().currentUser!!.uid
                    val docRef = FirebaseFirestore.getInstance().collection("user_activity")
                        .document(currUser).collection("viewed_coupons")
                    Log.d("TAGID", coupon.id)
                    docRef.document(coupon.id).get().addOnSuccessListener { doc ->
                        if (doc["nViews"] != null) {
                            Log.d("nViews", doc["nViews"].toString())
                            val nViews = doc["nViews"].toString().toInt()
                            val v_views = nViews + 1
                            val obj = hashMapOf("nViews" to v_views)
                            docRef.document(coupon.id).set(obj)
                        } else {
                            val v_views = 1
                            val obj = hashMapOf("nViews" to v_views)
                            docRef.document(coupon.id).set(obj)
                        }
                    }.addOnFailureListener { e ->
                        Log.e("ERRO", e.message)
                    }
                }
            }, couponItem.id)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val txtCouponValue = v.findViewById(R.id.txtCouponValue) as TextView
        val txtCouponDescription = v.findViewById(R.id.Nome) as TextView
        val imgCouponImg = v.findViewById(R.id.imgCouponImg) as ImageView

        //val txtCouponValidate = v.findViewById(R.id.txtCouponValidate) as TextView
        val txtCouponShortDescription = v.findViewById(R.id.txtCouponShortDescription) as TextView

        //val btnCouponRescue = v.findViewById(R.id.btnCouponRescue) as Button
        val txt_disc_cp = v.findViewById(R.id.txt_disc_cp) as TextView
        val txt_discount = v.findViewById(R.id.txt_discount) as TextView
        val txt_new_value = v.findViewById(R.id.txt_new_value) as TextView
        val txt_validate = v.findViewById(R.id.txt_validate) as TextView
        val txt_introValue1 = v.findViewById(R.id.txt_new_value3) as TextView
        val txt_introValue2 = v.findViewById(R.id.txt_new_value2) as TextView
        val txt_introValidate = v.findViewById(R.id.txt_validate_desc) as TextView

        var alert = AlertDialog.Builder(v.rootView.context)
        var parent = v.rootView

    }
}