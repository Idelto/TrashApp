package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class GetTrashData {

    val db = FirebaseFirestore.getInstance().collection("trash_products")

    interface MyCallback_getTrashData {

        fun onCallBack(trashData: TrashData)

    }

    fun readTrashData(myCallback: MyCallback_getTrashData, name: String) {

        val name = name
        Log.d("tag3", name)
        db.document(name).get().addOnSuccessListener { doc ->
            Log.d("tag3", "Entrei no listener")
            val obj = doc.toObject<TrashData>(TrashData::class.java)
            myCallback.onCallBack(obj!!)

        }

    }

    interface MyCallback_getTrashValues {

        fun onCallBack(material: Material)

    }

    fun readTrashValues(myCallback: MyCallback_getTrashValues, material: String) {

        val db2 = FirebaseFirestore.getInstance().collection("custo_material")

        val material = material
        Log.d("tag3_material", material)
        db2.document("materiais").collection("200614").document(material).get()
            .addOnSuccessListener { doc ->
                Log.d("tag4_docid", doc.id)
                Log.d("tag4", "Entrei no listener")
                Log.d("tag4_custo", doc.get("custo").toString())

                val obj = doc.toObject<Material>(Material::class.java)
                myCallback.onCallBack(obj!!)

            }

    }

}



