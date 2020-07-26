package com.trash2money.trashapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.KClasses.GetCurrentUserData
import com.trash2money.trashapp.KClasses.GetCurrentUserData.MyCallbackUser
import com.trash2money.trashapp.KClasses.Trash
import com.trash2money.trashapp.KClasses.Users
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Math.floor
import kotlin.math.roundToInt

@Suppress("UNREACHABLE_CODE")
class FragmentHome : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var btnRecycle: Button
    lateinit var btnTrash: Button
    lateinit var btnCoin: Button
    lateinit var btnCoupon: Button
    lateinit var btnNews: Button
    lateinit var id: TextView
    lateinit var msgSaveTree: TextView
    lateinit var msgTotalRecycled: TextView
    lateinit var msgSaveEnergy: TextView
    lateinit var msgSaveWater: TextView
    lateinit var txtMsg: TextView
    lateinit var btnQRCode: Button

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_home, container, false)
        btnRecycle = v.findViewById(R.id.btnRecycle)
        btnTrash = v.findViewById(R.id.btnShoppingKart)
        btnCoin = v.findViewById(R.id.btnCoin)
        btnCoupon = v.findViewById(R.id.btnCoupon)
        btnQRCode = v.findViewById(R.id.btnQRCode)
        btnNews = v.findViewById(R.id.btnNews)
        id = v.findViewById(R.id.txtId)
        msgSaveEnergy = v.findViewById(R.id.txtSaveEnergy)
        msgSaveWater = v.findViewById(R.id.txtSaveWater)
        msgSaveTree = v.findViewById(R.id.txtSaveTree)
        msgTotalRecycled = v.findViewById(R.id.txtRecycle)
        txtMsg = v.findViewById(R.id.txtMsg)

        var loggedName = ""
        var loggedUid = ""
        var loggedEmail = ""


        msgTotalRecycled.setText("") // ao carregar a tela define o total reciclado como vazio

        // Busca do banco de dados o usuário que está conectado
        GetCurrentUserData().readDataUser(object : MyCallbackUser {
            override fun onCallBack(user: Users) {
                loggedName = user.name
                loggedEmail = user.email
                loggedUid = user.uId

                id.setText("Olá , " + loggedName)
            }
        })

        // instanciando o firebase com o usuário que está ativo
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
                                        floor((((paperWeight + plasticWeight + cardboardWeight + steelWeight + glassWeight + eletronicWeight + alWeight) / 1000).toDouble())).toFloat()
                                            .roundToInt()
                                    val mSaveTree =
                                        floor(((paperWeight / 91.0)) / 1000).toFloat().roundToInt()
                                    val mSaveEnergy =
                                        floor((alWeight * 15.0) / 1000).toFloat().roundToInt()
                                    val mSaveWater =
                                        floor(((paperWeight * 540.0 - 2) + plasticWeight * 180 + alWeight * 5) / 1000).toFloat()
                                            .roundToInt()

                                    msgSaveEnergy.setText(" + " + mSaveEnergy.toString() + " Kwh ")
                                    msgSaveTree.setText("+ " + mSaveTree.toString() + " Árvores")
                                    msgSaveWater.setText("+ " + mSaveWater.toString() + " L de àgua")
                                    msgTotalRecycled.setText("+ " + mTotalReciclado.toString() + " kg")

                                }
                            }
                    }
                }
            }
        }

        // Botão de ação dos menus inferiores
        btnRecycle.setOnClickListener() {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentRecycleMain")
            startActivity(intent)
        }

        btnCoin.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinBalance")
            startActivity(intent)

        }
        btnTrash.setOnClickListener() {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinsCan")
            startActivity(intent)
        }
        btnCoupon.setOnClickListener() {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentCoupons")
            startActivity(intent)
        }

        btnQRCode.setOnClickListener {

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentMyQRCode()).commit()

        }

        btnNews.setOnClickListener {

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecyclerScoreFragment()).commit()
        }

        return v
    }

}
