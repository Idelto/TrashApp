package com.trash2money.trashapp.KClasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TCoinBalance {

    interface MyCallBackTcoins {
        fun onCallBackTCoin(tCoin: Double)
    }

    fun getTCoin(callbackTCoins: MyCallBackTcoins) {
        val currUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("trashcoins")

        var balance = 0.0

        db.get().addOnSuccessListener { docs ->

            for (doc in docs) {

                db.document(doc.id).get().addOnSuccessListener { doc ->

                    val str = doc["value"].toString().replace(",", ".")

                    val value = str.toDouble()

                    balance += value

                    callbackTCoins.onCallBackTCoin(balance.toDouble())

                }

            }

        }

    }

}