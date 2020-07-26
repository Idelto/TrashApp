package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode

class GetTrashScore {

    interface MyCallBackTrashScore {
        fun onCallBack(trashcoins: Double, peso: Double, numero: Int, numItem: Int)
    }

    interface MyCallBackSingleTrashScore {
        fun onCallBack(tcoin: Double, weight: Double, num: Int, nitens: Int)
    }

    fun getTrashScore(myCallback: MyCallBackSingleTrashScore, trashid: String, user: String) {

        val trashid = trashid
        val user: String = user

        Log.d(TAG + " SingleTrashScore", "${user} ${trashid}")

        val ref = FirebaseFirestore.getInstance().collection("user_activity").document(user)
            .collection("trashcans")
        var tcoins = 0.0
        var weight = 0.0
        val num = 0
        var nitens = 0

        ref.document(trashid).collection("trash_item").get().addOnSuccessListener { trash_itens ->
            for (item in trash_itens) {
                val teste = 0
                Log.d(TAG + " trash_item", item.id.toString())
                tcoins += item["trashcoins"].toString().toBigDecimal()
                    .setScale(2, RoundingMode.HALF_EVEN).toDouble()
                nitens += 1
                weight += item["weight"].toString().toBigDecimal()
                    .setScale(2, RoundingMode.HALF_EVEN).toDouble()
                Log.d(TAG, tcoins.toString())
                Log.d(TAG, nitens.toString())
                Log.d(TAG, weight.toString())
                Log.d(TAG, num.toString())

                myCallback.onCallBack(tcoins, weight, num, nitens)

            }
        }
    }

    fun getScore(myCallback: GetTrashScore.MyCallBackTrashScore, user: String) {

        val user: String = user
        Log.d(TAG + " TrashScore", "${user}")

        val ref = FirebaseFirestore.getInstance().collection("user_activity").document(user)
            .collection("trashcans")

        ref.get().addOnSuccessListener { trashs ->

            var trascoins = 0.0
            var peso = 0.0
            var numero = 0
            var numItem = 0

            for (trash in trashs) {

                GetTrashScore().getTrashScore(object : GetTrashScore.MyCallBackSingleTrashScore {
                    override fun onCallBack(tcoin: Double, weight: Double, num: Int, nitens: Int) {
                        val tcoin = tcoin
                        val weight = weight
                        val num = num
                        val nitens = nitens

                        trascoins += tcoin
                        peso += weight
                        numero += numero
                        numItem += nitens

                        myCallback.onCallBack(trascoins, peso, numero, numItem)
                    }
                }, trash.id.toString(), user)
            }
        }
    }


}