package com.trash2money.trashapp.KClasses

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal
import java.math.RoundingMode

class OpenDialog(context: Context, nome: String) {

    var context = context
    var nome = nome

    fun openDialog(view: View) {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_recycle_details, null)

        builder.setCancelable(false)
        builder.setView(dialogView)

        val btnCalculate = dialogView.findViewById<ImageButton>(R.id.btnCalc2)
        val btnDisponibilizar = dialogView.findViewById<Button>(R.id.btnRec2)
        val btnBack = dialogView.findViewById<ImageButton>(R.id.btn_back)
        val switch = dialogView.findViewById<Switch>(R.id.switch1)

        val title = dialogView.findViewById<TextView>(R.id.txtTitle)
        val desc = dialogView.findViewById<TextView>(R.id.txtDesc)
        val img = dialogView.findViewById<ImageView>(R.id.imgDesc)
        val txtMaterial = dialogView.findViewById<TextView>(R.id.txtMaterial)
        val hintVol = dialogView.findViewById<EditText>(R.id.txtVol2)
        val hintQty = dialogView.findViewById<EditText>(R.id.txtQty2)
        val hintQtyHolder = dialogView.findViewById<TextView>(R.id.txtQty2Holder)
        val hintQtyhide = dialogView.findViewById<TextView>(R.id.txtQty3Holder)
        val qtyHide = dialogView.findViewById<EditText>(R.id.txtQty3)
        val hintmass = dialogView.findViewById<TextView>(R.id.txtmass)
        val lblTcoin = dialogView.findViewById<TextView>(R.id.lblTCoins2)
        val imgElipse: ImageView = dialogView.findViewById<ImageView>(R.id.imgEllipse)
        var factor: Double = 0.0
        var btnDesabilitado = true

        Log.d("Material", nome)
        GetTrashData().readTrashData(object : GetTrashData.MyCallback_getTrashData {
            override fun onCallBack(trashData: TrashData) {
                val unidade = trashData.unity
                title.text = trashData.shortDesc.capitalize()
                desc.text = trashData.longDesc
                hintVol.hint = trashData.unity
                txtMaterial.text = trashData.material_desc.capitalize()
                factor = trashData.gperUnit
                hintQty.hint = "Un"

                val material = trashData.material_desc
                val nameIcon = trashData.name
                var scale = 1

                if (unidade == "g" || unidade == "kg" || unidade == "mg") {
                    hintmass.text = "Peso ?"
                    switch.text = "em g"
                } else if (unidade == "") {
                    hintmass.text = ""

                } else if (unidade == "un") {
                    val one = 1
                    hintmass.text = ""
                    hintmass.visibility = View.INVISIBLE
                    hintVol.setText(one.toString())
                    hintVol.visibility = View.INVISIBLE
                    hintQty.visibility = View.INVISIBLE
                    hintQtyHolder.visibility = View.INVISIBLE
                    hintQtyhide.visibility = View.VISIBLE
                    qtyHide.visibility = View.VISIBLE
                    switch.visibility = View.INVISIBLE

                } else if (unidade == "fl") {

                    hintmass.text = "Folhas ?"
                    hintVol.hint = "fl"
                    switch.visibility = View.INVISIBLE

                } else {
                    hintmass.text = "Volume ?"
                    switch.text = "em ml"
                }

                if (nome == nameIcon) {
                    Log.d("tag2", "$nameIcon")
                    Log.d("tag2", "$nome")
                    val strDrawable = "ic_b_${nameIcon}"
                    Log.d("tag1", "$strDrawable")
                    img.setImageResource(context!!.resources.getIdentifier(strDrawable,
                        "drawable",
                        context!!.packageName))
                }

                when (material) {
                    "alumínio" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_yellow)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.coloryellow))
                    }
                    "papel" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_blue)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.colorBlue))
                    }
                    "plastico" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_red)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.colorRed))
                    }
                    "isopor" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_red)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.colorRed))
                    }
                    "papelão" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_blue)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.colorBlue))
                    }
                    "aço" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_yellow)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.coloryellow))
                    }
                    "vidro" -> {
                        imgElipse.setImageResource(R.drawable.ellipse_green)
                        txtMaterial.setTextColor(context.resources.getColor(R.color.colorGreen))
                    }
                }


                val dialog = builder.create() as AlertDialog

                btnBack.setOnClickListener() {
                    dialog.cancel()
                }

                switch.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        scale = 1000

                        if (unidade == "g" || unidade == "gr") {
                            switch.text = "em kg"
                            hintVol.hint = "Kg"
                        } else if (unidade == "ml") {
                            switch.text = "em Lt"
                            hintVol.hint = "Lt"
                        } else {
                            switch.visibility = View.INVISIBLE
                        }

                        if (!hintVol.text.isNullOrEmpty()) {
                            var volume = hintVol.text.toString().toDouble()
                            volume = volume / 1000
                            hintVol.setText(volume.toString())
                        }

                    } else {

                        scale = 1

                        if (unidade == "g" || unidade == "gr") {
                            switch.text = "em g"
                            hintVol.hint = "g"
                        } else if (unidade == "ml") {
                            switch.text = "em ml"
                            hintVol.hint = "ml"
                        } else {
                            switch.visibility = View.INVISIBLE
                        }

                        if (!hintVol.text.isNullOrEmpty()) {
                            var volume = hintVol.text.toString().toDouble()
                            volume = volume * 1000
                            hintVol.setText(volume.toString())
                        }

                    }
                }

                btnCalculate.setOnClickListener() {

                    GetTrashData().readTrashValues(object : GetTrashData.MyCallback_getTrashValues {
                        override fun onCallBack(material: Material) {

                            val cost = material.custo

                            if (unidade == "un") {
                                if (qtyHide.text.isNullOrEmpty()) {
                                    Toast.makeText(context,
                                        "Você precisa preencher os campos primeiro",
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Calculando...", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("tag5_gperUnit", factor.toString())
                                    Log.d("tag5", hintVol.toString())
                                    Log.d("tag5", qtyHide.toString())
                                    Log.d("tag5", cost.toString())
                                    Log.d("tag5", material.toString())
                                    val tcoin = calculate(factor,
                                        hintVol.text.toString().toDouble(),
                                        qtyHide.text.toString().toInt(),
                                        cost,
                                        trashData.material_desc)
                                    lblTcoin.text = tcoin.toString()
                                    btnDesabilitado = false
                                    Log.d("Tag2", "$btnDesabilitado")
                                }
                            } else {

                                val vol2 = 0.0

                                if (hintQty.text.isNullOrEmpty() || hintVol.text.isNullOrEmpty()) {
                                    Toast.makeText(context,
                                        "Você precisa preencher os campos primeiro",
                                        Toast.LENGTH_LONG).show()
                                } else {
                                    var vol = hintVol.text.toString().toDouble()
                                    Log.d("VOL_SCALE", scale.toString())
                                    vol = vol * scale

                                    //Toast.makeText(context, "Calculando...", Toast.LENGTH_LONG).show()
                                    Log.d("LOG_VOLw", vol2.toString())
                                    val tcoin = calculate(factor,
                                        vol,
                                        hintQty.text.toString().toInt(),
                                        cost,
                                        trashData.material_desc)
                                    lblTcoin.text = tcoin.toString()
                                    Log.d("TAG_TRASH", factor.toString())
                                    Log.d("TAG_TRASH", hintVol.toString())
                                    Log.d("TAG_TRASH", qtyHide.toString())
                                    Log.d("TAG_TRASH", cost.toString())
                                    Log.d("TAG_TRASH", material.toString())
                                    btnDesabilitado = false
                                    Log.d("Tag2", "$btnDesabilitado")
                                }

                            }

                        }

                    }, trashData.material)
                }

                btnDisponibilizar.setOnClickListener() {
                    if (btnDesabilitado == true) {
                        Log.d("Tag2", "$btnDesabilitado")
                    } else {
                        var qty = 0f

                        var vol = hintVol.text.toString().toFloat()
                        if (unidade == "un") {
                            qty = qtyHide.text.toString().toFloat()
                        } else {
                            qty = hintQty.text.toString().toFloat()
                        }

                        val tcoin = lblTcoin.text.toString().toFloat()

                        //Toast.makeText(context, "Gravando ...", Toast.LENGTH_LONG).show()
                        vol = vol * scale
                        vol = (factor * vol * qty).toFloat()
                        store(vol, qty, tcoin.toFloat(), material, trashData.shortDesc)

                        dialog.cancel()

                    }
                }

                dialog.show()
            }

            private fun calculate(
                factor: Double,
                vol: Double,
                qty: Int,
                cost: Double,
                material: String
            ): BigDecimal {
                var tcoin = BigDecimal((0.01 + (factor * vol * qty * cost))).setScale(2,
                    RoundingMode.HALF_DOWN)

                when (material) {
                    "alumínio" -> tcoin = tcoin.times(1.05.toBigDecimal())
                    "plastico" -> tcoin = tcoin.times(1.12.toBigDecimal())
                    "isopor" -> tcoin = tcoin.times(1.22.toBigDecimal())
                    "aço" -> tcoin = tcoin.times(1.1.toBigDecimal())
                    "papel" -> tcoin = tcoin.times(1.15.toBigDecimal())
                    "papelao" -> tcoin = tcoin.times(1.17.toBigDecimal())
                }

                return (tcoin)
            }

            private fun store(
                peso: Float,
                qty: Float,
                tc: Float,
                material: String,
                shortDesc: String
            ) {

                val auth = FirebaseAuth.getInstance() // busca o usuário que está autenticado
                val db = FirebaseFirestore.getInstance() // define o caminho raiz do db
                val currentUser = auth.currentUser // traz os dados do usuário autenticado

                // criação do objeto a ser inserido no firebase
                val data =
                    Trash(material, peso, qty, 0F, tc, shortDesc, "", System.currentTimeMillis())

                //Adicionar o Trash ao firebase
                db.collection("points_trash_can").document(currentUser?.uid.toString())
                    .collection("trash_item").add(data).addOnSuccessListener { doc ->
                        db.collection("points_trash_can").document(currentUser?.uid.toString())
                            .collection("trash_item").document(doc.id).update("trashid", doc.id)
                        val ref =
                            db.collection("points_trash_can").document(currentUser?.uid.toString())
                        val obj = hashMapOf("id" to currentUser?.uid)
                        ref.set(obj).addOnSuccessListener {
                            Log.d("points_trash_can", "adicionado com sucesso")
                        }
                        Toast.makeText(context, "Seu resíduo está na lixeira", Toast.LENGTH_LONG)
                            .show()

                    }.addOnFailureListener { e ->

                        Log.d("TAG ERRO ::", e.message!!)

                    }

            }

        }, nome)

    }
}