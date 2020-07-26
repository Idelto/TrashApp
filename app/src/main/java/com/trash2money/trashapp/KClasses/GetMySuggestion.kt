package com.trash2money.trashapp.KClasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetMySuggestion {

    interface MyCallbackSuggestion {
        fun onCallBack(suggestion: ArrayList<String>)
    }

    fun readSuggestion(myCallback: GetMySuggestion.MyCallbackSuggestion) {

        val currUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("coupons")
        db.get().addOnSuccessListener { cp ->
            val suggestions = ArrayList<String>()
            for (doc in cp) {

                FirebaseFirestore.getInstance().collection("user_activity")
                    .document(currUser?.uid.toString()).collection("coupons").document(doc.id).get()
                    .addOnSuccessListener { d ->

                        val coupon = d.toObject<CouponItem>(CouponItem::class.java)
                        suggestions.add(coupon!!.brand)
                        suggestions.add(coupon!!.marca)
                        suggestions.add(coupon!!.description)
                        suggestions.add(coupon!!.ratingprice.toString())
                        suggestions.add(coupon!!.category)

                        myCallback.onCallBack(suggestions)
                    }
            }
        }
    }
}