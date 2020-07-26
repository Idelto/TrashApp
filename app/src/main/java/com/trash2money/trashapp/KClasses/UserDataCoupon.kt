package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserDataCoupon() {

    val auth = FirebaseAuth.getInstance()
    val currUser = auth.currentUser
    val db = FirebaseFirestore.getInstance().collection("user_activity").document(currUser!!.uid)

    interface MyCallBackMyCoupons {

        fun onCallBackCoupons(coupon: CouponItem)

    }

    fun getMyCouponItem(myCallbackMyCoupons: MyCallBackMyCoupons, status: String) {

        Log.d("LOG CALL BACK", "ENTREI NA FUNÇÃO")

        val docRef = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("coupons")
        Log.d("REFERENCIA DO FIRESTORE", "ENTREI NA FUNÇÃO")

        docRef.get().addOnSuccessListener { docs ->
            Log.d("DOCUMENT SNAPSHOT", "ENTREI NA FUNÇÃO")

            for (d in docs) {

                docRef.document(d.id).get().addOnSuccessListener { doc ->

                    val coupon = doc.toObject<CouponItem>(CouponItem::class.java)

                    Log.d("DOCUMMENT TO OBJECT", coupon?.description.toString())

                    if (coupon?.status == status) {
                        Log.d("DOCUMMENT TO OBJECT", coupon?.description.toString())

                        if (coupon != null) {
                            myCallbackMyCoupons.onCallBackCoupons(coupon)
                            Log.d("CALLBACK REALIZADO", "ENTREI NA FUNÇÃO")
                        }

                    }

                }

            }

        }

    }
}