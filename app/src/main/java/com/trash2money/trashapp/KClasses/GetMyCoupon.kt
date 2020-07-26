package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetMyCoupon() {

    interface MyCallbackCoupon {
        fun onCallBack(coupon: CouponItem)
    }

    fun readCoupon(myCallback: GetMyCoupon.MyCallbackCoupon, couponId: String) {
        Log.d("txt2", couponId)
        val currUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("coupons").whereEqualTo("id", couponId)
        db.get().addOnSuccessListener { cp ->

            for (doc in cp) {

                FirebaseFirestore.getInstance().collection("user_activity")
                    .document(currUser?.uid.toString()).collection("coupons").document(doc.id).get()
                    .addOnSuccessListener { d ->

                        val coupon = d.toObject<CouponItem>(CouponItem::class.java)

                        myCallback.onCallBack(coupon!!)
                    }
            }
        }
    }
}

