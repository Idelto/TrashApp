package com.trash2money.trashapp.KClasses

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class ShareDialog(context: Context) {

    var vContext = context

    fun openShareDialog() {
        val builder = android.app.AlertDialog.Builder(vContext)
        val dialogView = LayoutInflater.from(vContext).inflate(R.layout.dialog_share, null)
        builder.setCancelable(true)
        builder.setView(dialogView)

        val btnCompartilhar = dialogView.findViewById<Button>(R.id.btnShare)
        val btnNaoCompartilhar = dialogView.findViewById<Button>(R.id.btnNotShare)
        var textShare = dialogView.findViewById<TextView>(R.id.txtLink)
        val textShareNome = dialogView.findViewById<TextView>(R.id.txtApresentacao)
        val textShareReciclei = dialogView.findViewById<TextView>(R.id.txtreciclei)
        val textShareLink = dialogView.findViewById<TextView>(R.id.txtLink)



        Firebase.firestore.collection("link_app").document("trashapp").get()
            .addOnSuccessListener { doc ->
                val text = doc.getString("link")
                textShareLink.setText(text)
            }

        GetCurrentUserData().readDataUser(object : GetCurrentUserData.MyCallbackUser {
            override fun onCallBack(user: Users) {
                val loggedName = user.name

                textShareNome.setText("Olá , Eu " + loggedName + " já:")
                textShareReciclei.setText("♻ Reciclei " + 0 + " Kg de materiais," + "\n\n" + " 💧 Economizei " + 0 + " Litros de água"+ "\n\n" + " 💡 " + 0 + " Kw de Energia" + "\n\n" + "Utilizando o trashApp." + "\n\n" + "Venha reciclar também, você ajuda o meio ambiente e troca por benefícios incríveis 💚💚💚")
            }
        })

        val currUser = FirebaseAuth.getInstance().currentUser?.uid

        // definição do caminho raiz do firebase
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser.toString()).collection("trashcans")

        // Lendo a coleção
        val docRef = db.get()

        var totalWeight = 0F
        var totalQty = 0F
        var paperQty = 0F
        var paperWeight = 0F
        var plasticQty = 0F
        var plasticWeight = 0F
        var cardboardQty = 0F
        var cardboardWeight = 0F
        var steelQty = 0F
        var steelWeight = 0F
        var glassQty = 0F
        var glassWeight = 0F
        var eletronicQty = 0F
        var eletronicWeight = 0F
        var oilQty = 0F
        var oilWeight = 0F
        var alQty = 0F
        var alWeight = 0F
        var otherQty = 0F
        var otherWeight = 0F
        var mTotalReciclado = 0

        docRef.addOnSuccessListener { query ->

            //tratar todos os trashitens dentro da lista query
            for (trash_item in query) {
                val status = trash_item["status"]!!.toString().toInt()
                Log.d("Trash_item_status", trash_item.id + " - Status: " + status.toString())
                if (status == 5) {
                    db.document(trash_item.id).get().addOnSuccessListener { doc ->
                        Log.d("DOCID", doc.id.toString())
                        //Log.d("Trash_item_status", trash_item["status"].toString())
                        // acessei o segundo níuvel do documento
                        Log.d("Trash_item_status", trash_item["status"].toString())
                        db.document(trash_item.id).collection("trash_item").get()
                            .addOnSuccessListener { docs ->

                                for (doc in docs) {

                                    Log.d("DOCID", doc.id.toString())
                                    val trash = doc.toObject<Trash>(Trash::class.java)

                                    Log.d("TRASHID", trash!!.type)

                                    totalWeight += trash!!.weight.toFloat()
                                    totalQty += trash!!.weight.toFloat()


                                    // verifica a quantidade de lixo que ja foi reciclado
                                    when (trash!!.type) {
                                        "papel" -> {
                                            paperQty += trash.quantity
                                            paperWeight += trash.weight
                                        }
                                        "plastico" -> {
                                            plasticQty += trash.quantity
                                            plasticWeight += trash.weight
                                        }
                                        "isopor" -> {
                                            plasticQty += trash.quantity
                                            plasticWeight += trash.weight
                                        }
                                        "papelão" -> {
                                            cardboardQty += trash.quantity
                                            cardboardWeight += trash.weight
                                        }
                                        "aco" -> {
                                            steelQty += trash.quantity
                                            steelWeight += trash.weight
                                        }
                                        "vidro" -> {
                                            glassQty += trash.quantity
                                            glassWeight += trash.weight
                                        }
                                        "oleo" -> {
                                            oilQty += trash.quantity
                                            oilWeight += trash.weight
                                        }
                                        "eletronico" -> {
                                            eletronicQty += trash.quantity
                                            eletronicWeight += trash.weight
                                        }
                                        "outro" -> {
                                            otherQty += trash.quantity
                                            otherWeight += trash.weight
                                        }
                                        "alumínio" -> {
                                            alQty += trash.quantity
                                            alWeight += trash.weight
                                        }
                                    }

                                    Log.d("TOTAL PAPER", paperWeight.toString())
                                    Log.d("TOTAL PLASTIC", plasticWeight.toString())
                                    Log.d("TOTAL CARDBOARD", cardboardWeight.toString())
                                    Log.d("TOTAL STEEL", steelWeight.toString())
                                    Log.d("TOTAL GLASS", glassWeight.toString())
                                    Log.d("TOTAL ELETRONIC", eletronicWeight.toString())
                                    Log.d("TOTAL ALUMINIUM", alWeight.toString())

                                    // Alterando os labels com as contas que foram feitas acima
                                    mTotalReciclado =
                                        Math.floor((((paperWeight + plasticWeight + cardboardWeight + steelWeight + glassWeight + eletronicWeight + alWeight) / 1000).toDouble()))
                                            .toFloat().roundToInt()
                                    var mSaveTree =
                                        Math.floor(((paperWeight / 91.0)) / 1000).toFloat()
                                            .roundToInt()
                                    val mSaveEnergy =
                                        Math.floor((alWeight * 15.0) / 1000).toFloat().roundToInt()
                                    val mSaveWater =
                                        Math.floor(((paperWeight * 540.0 - 2) + plasticWeight * 180 + alWeight * 5) / 1000)
                                            .toFloat().roundToInt()

                                    textShareReciclei.setText("♻ Reciclei " + mTotalReciclado + " Kg de materiais," + "\n\n" + " 💧 Economizei " + mSaveWater + " Litros de água"+ "\n\n" + " 💡 " + mSaveEnergy + " Kw de Energia" + "\n\n" + "Utilizando o trashApp." + "\n\n" + "Venha reciclar também, você ajuda o meio ambiente e troca por benefícios incríveis 💚💚💚")

                                }
                            }
                    }
                }
            }
        }

        val dialog = builder.create() as android.app.AlertDialog

        btnCompartilhar.setOnClickListener {

            val message = textShareNome.text.toString()
            val message2 = textShareReciclei.text.toString()
            val message3 = textShareLink.text.toString()
            val messageCompleta = (message + "\n\n" + " " + message2 + "\n\n" + message3)
            message.get(1)
            Log.d("taskmsg", message)
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, messageCompleta)
            intent.type = "text/plain"
            vContext.startActivity(Intent.createChooser(intent, "share to :"))

            dialog.dismiss()
        }

        btnNaoCompartilhar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}


