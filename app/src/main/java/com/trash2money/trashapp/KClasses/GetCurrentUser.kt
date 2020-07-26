package com.trash2money.trashapp.KClasses

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

// classe para buscar dados de usuário no banco de dados.

class GetCurrentUserData {

    lateinit var user: Users
    lateinit var name: String

    val auth = FirebaseAuth.getInstance() // define qual o usuário está autenticado
    val db = FirebaseFirestore.getInstance() // define caminho raiz do firebase
    val currentUser = auth.currentUser // busca o usuário autenticado

    interface MyCallbackUser {

        fun onCallBack(user: Users)

    }

    fun readDataUser(myCallback: MyCallbackUser) {

        val docRef = db.collection("users").document(currentUser?.uid.toString())

        docRef.get() // função que faz o read do firebase
            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->

                // transformar o resultado do snapshot em objeto
                val user = documentSnapshot.toObject<Users>(Users::class.java!!)

                // Devolução para a classe de interface de callback
                myCallback.onCallBack(user!!)

            })
    }

    interface MyCallbackUserActivity {

        fun onCallBack(userActivity: UserActivity)

    }

    fun readDataUserActivity(myCallback: MyCallbackUserActivity) {
        Log.d("TAG", "Entrei na Classe")
        val docRef = db.collection("user_activity").document(currentUser?.uid.toString())
        Log.d("TAG", "Auth Conectado")

        docRef.get().addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                Log.d("TAG", "Entrei no AddOnSuccess")
                val userActivity =
                    documentSnapshot.toObject<UserActivity>(UserActivity::class.java!!)
                Log.d("TAG", "DocumentSnapshot Transformado")

                if (userActivity != null) {
                    myCallback.onCallBack(userActivity)
                }
                Log.d("TAG", "CallBackRealizado")
            })
    }


    interface MyCallbackTrashCan {

        fun onCallBack(trash: Trash)

    }

    fun readDataTrashCan(myCallback: MyCallbackTrashCan) {

        val docRef = db.collection("points_trash_can").document(currentUser?.uid.toString())
            .collection("trash_item")
        Log.d("TAG TRASH CAN", "Entrei no AddOnSuccess")

        docRef.get().addOnSuccessListener {
            var i = 1
            for (query in it) {
                Log.d("TAG TRASH CAN", "DocumentSnapshot Transformado")
                Log.d("TAG TRASH CAN", "DocumentSnapshot Transformado")
                docRef.document(query.id).get().addOnSuccessListener { doc ->
                    val obj = doc.toObject<Trash>(Trash::class.java)
                    Log.d("TAG TRASH CAN OBJ ${i} = ${query.id}",
                        "Tipo = ${obj?.type} Qty = ${obj?.quantity}")
                    i++
                    myCallback.onCallBack(obj!!)
                    Log.d("TAG TRASH CAN", "CallBackRealizado")
                }
            }

        }.addOnFailureListener { e ->
                Log.d("TAG TRASH CAN ERRO", e.toString())
            }
    }

    interface MyCallbackUserAdress{
        fun onCallBackUserAddress (location:Location)
    }

    fun getUserAddress(myCallback: MyCallbackUserAdress){

        val user =  FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseFirestore.getInstance().collection("users").document(user).collection("address")

        ref.get().addOnSuccessListener {docs->

            val doc = docs.last()
                val userLocation = doc.toObject<Location>(Location::class.java)
                myCallback.onCallBackUserAddress(userLocation)
        }
    }
}