package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class GetCoupon() {

    interface MyCallbackCoupon {
        fun onCallBack(coupon: CouponItem)
    }

    fun readCoupon(myCallback: GetCoupon.MyCallbackCoupon, couponId: String) {
        Log.d("txt2", couponId)
        FirebaseFirestore.getInstance().collection("coupons").get().addOnSuccessListener { docs ->
            for (doc in docs) {
                Log.d("txt3id", doc.id.toString())
                Log.d("txt3cpid", doc.id.toString())
                Log.d("txt3brand", doc["brand"].toString())
                Log.d("txt3category", doc["category"].toString())
                Log.d("txt3cost", doc["cost"].toString())
                Log.d("txt3description", doc["description"].toString())
                Log.d("txt3disc", doc["disc"].toString())
                Log.d("txt3full_desc", doc["full_desc"].toString())
                Log.d("txt3id", doc["id"].toString())
                Log.d("txt3img", doc["img"].toString())
                Log.d("txt3impressions", doc["impressions"].toString())
                Log.d("txt3location", doc["location"].toString())
                Log.d("txt3qty", doc["qty"].toString())
                Log.d("txt3rating", doc["rating"].toString())
                Log.d("txt3status", doc["status"].toString())
                Log.d("txt3validate", doc["validate"].toString())
                Log.d("txt3value", doc["value"].toString())
                Log.d("txt3views", doc["views"].toString())
            }
        }
        val db = FirebaseFirestore.getInstance().collection("coupons").document(couponId)
        db.get().addOnSuccessListener { cp ->
            Log.d("txt2", "cheguei aqui")
            val coupon = cp.toObject<CouponItem>(CouponItem::class.java)

            Log.d("txtId", coupon!!.id)
            myCallback.onCallBack(coupon!!)
        }
    }

}

