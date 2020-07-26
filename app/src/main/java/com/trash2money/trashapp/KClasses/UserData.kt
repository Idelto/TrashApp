package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserData() {

    val auth = FirebaseAuth.getInstance()
    val currUser = auth.currentUser
    val db = FirebaseFirestore.getInstance().collection("user_activity").document(currUser!!.uid)

    interface MyCallbackUserData {

        fun onCallBack(tCoin: Float)

    }

    fun getTrashCoins(myCallbackTrashCoin: MyCallbackUserData, status: String) {

        var tCoin = 0F
        val docRef = db.collection("trashcans")
        val docs = docRef.whereEqualTo("status", status)

        docs.get().addOnSuccessListener { query1 ->

            for (rootDoc in query1) {
                docRef.document(rootDoc.id).collection("trash_item").get()
                    .addOnSuccessListener { query ->

                        for (doc in query) {
                            docRef.document(rootDoc.id).collection("trash_item").document(doc.id)
                                .get().addOnSuccessListener {

                                val obj = doc.toObject<Trash>(Trash::class.java)
                                Log.d("OBH TRASHCOIN", obj.description)
                                tCoin += obj.trashcoins
                                Log.d("OBH TRASHCOIN", obj.trashcoins.toString())
                                Log.d("OBH TRASHCOIN", tCoin.toString())

                                myCallbackTrashCoin.onCallBack(tCoin)

                            }
                        }
                    }
            }
        }
    }

    interface MyCallBackTcoins {

        fun onCallBackTCoin(tCoin: Float)

    }

    fun getTCoins(callbackTCoins: MyCallBackTcoins) {

        db.get().addOnSuccessListener { doc ->

            val tCoin = doc.get("trashcoins").toString().toFloat()

            callbackTCoins.onCallBackTCoin(tCoin)

        }

    }

    interface MyCallBackCoupons {

        fun onCallBackCoupons(coupon: CouponItem)

    }

    fun getCouponItem(myCallbackCoupons: MyCallBackCoupons) {

        Log.d("LOG CALL BACK", "ENTREI NA FUNÇÃO")

        val docRef = FirebaseFirestore.getInstance().collection("coupons_kart")
            .document(currUser?.uid.toString()).collection("coupon_item")
        Log.d("REFERENCIA DO FIRESTORE", "ENTREI NA FUNÇÃO")

        docRef.get().addOnSuccessListener { docs ->
            Log.d("DOCUMENT SNAPSHOT", "ENTREI NA FUNÇÃO")

            for (d in docs) {

                docRef.document(d.id).get().addOnSuccessListener { doc ->

                    val coupon = doc.toObject<CouponItem>(CouponItem::class.java)

                    Log.d("DOCUMMENT TO OBJECT", coupon?.description.toString())

                    if (coupon != null) {
                        myCallbackCoupons.onCallBackCoupons(coupon)
                        Log.d("CALLBACK REALIZADO", "ENTREI NA FUNÇÃO")
                    }

                }

            }

        }

    }

    interface MyCallBackMyCoupons {

        fun onCallBackCoupons(coupon: CouponItem)

    }

    fun getMyCouponItem(myCallbackMyCoupons: MyCallBackMyCoupons) {

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

                    if (coupon != null) {
                        myCallbackMyCoupons.onCallBackCoupons(coupon)
                        Log.d("CALLBACK REALIZADO", "ENTREI NA FUNÇÃO")
                    }

                }

            }

        }

    }
}