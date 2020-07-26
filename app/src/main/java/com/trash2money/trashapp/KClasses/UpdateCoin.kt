package com.trash2money.trashapp.KClasses

import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCoin {

    fun updateValues(activity: FragmentActivity) {

        val currUser = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("points_trash_can").document(currUser).collection("trash_item")

        docRef.get().addOnSuccessListener { docs ->


            if (docs.size() > 0) {

            }
        }
    }
}