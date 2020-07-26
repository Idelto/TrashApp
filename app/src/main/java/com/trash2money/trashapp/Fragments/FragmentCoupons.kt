package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Adapters.CouponAdapter
import com.trash2money.trashapp.KClasses.ConvertTo
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.KClasses.ProgressBarDialog
import com.trash2money.trashapp.KClasses.TCoinBalance
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentCoupons : Fragment() {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    lateinit var couponsRecycler: RecyclerView
    lateinit var txtTotalTCoins: TextView
    lateinit var btnShoppingKart: ImageButton
    lateinit var btnScrollTodos: Button
    lateinit var btnScrollAlimentacao: Button
    lateinit var btnScrollCosmeticos: Button
    lateinit var btnScrollJoiasebijouterias: Button
    lateinit var btnScrollFavoritos: Button
    lateinit var btnScrollSugestoes: Button

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_cupons, container, false) as View

        btnShoppingKart = v.findViewById(R.id.btnShoppingKart)

        couponsRecycler = v.findViewById(R.id.couponsRecycler)
        couponsRecycler.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        txtTotalTCoins = v.findViewById(R.id.txtTotalTCoins)
        btnScrollTodos = v.findViewById(R.id.btn_scl_Todos)
        btnScrollAlimentacao = v.findViewById(R.id.btn_scl_Alimentacao)
        btnScrollCosmeticos = v.findViewById(R.id.btn_scl_Cosmeticos)
        btnScrollJoiasebijouterias = v.findViewById(R.id.btn_scl_Joiasebijouterias)
        btnScrollFavoritos = v.findViewById(R.id.btn_scl_Favoritos)
        //btnScrollSugestoes = v.findViewById(R.id.btn_scl_Sugestoes)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        val dialog = ProgressBarDialog(context!!)
        dialog.openDialog()

        TCoinBalance().getTCoin(object : TCoinBalance.MyCallBackTcoins {

            override fun onCallBackTCoin(tCoin: Double) {

                Log.d("LOG TCOIN FINAL", tCoin.toString())
                txtTotalTCoins.text = ConvertTo().convert2casasTrashCoins(tCoin)

            }
        })

        db.collection("coupons").get().addOnSuccessListener { query ->
            val couponList = ArrayList<CouponItem>()

            for (doc in query) {

                val obj = doc.toObject<CouponItem>(CouponItem::class.java)
                if (obj.qty > 0) {
                    couponList.add(CouponItem(obj.id,
                        obj.img,
                        obj.brand,
                        obj.category,
                        obj.cost.toFloat(),
                        obj.description,
                        obj.disc.toString(),
                        obj.qty,
                        obj.rating,
                        obj.validate,
                        obj.full_desc,
                        obj.value,
                        obj.views,
                        obj.impressions,
                        obj.status,
                        obj.idkey,
                        obj.prio,
                        obj.rules,
                        obj.how,
                        obj.codcoupon,
                        obj.coupontrash,
                        obj.marca,
                        obj.ratingprice))
                }

            }

            couponList.shuffle()

            val adapter = CouponAdapter(couponList)

            couponsRecycler.adapter = adapter
            dialog.closeDialog()
        }

        botoes()
        btnShoppingKart.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentCouponKart")
            startActivity(intent)

        }

        return v
    }

    private fun botoes() {
        btnScrollTodos.setOnClickListener {
            colorButton(btnScrollTodos)

            couponsRecycler.setRecycledViewPool(couponsRecycler.recycledViewPool)

            db.collection("coupons").get().addOnSuccessListener { query ->
                val couponList = ArrayList<CouponItem>()

                for (doc in query) {

                    val obj = doc.toObject<CouponItem>(CouponItem::class.java)

                    couponList.add(CouponItem(obj.id,
                        obj.img,
                        obj.brand,
                        obj.category,
                        obj.cost.toFloat(),
                        obj.description,
                        obj.disc.toString(),
                        obj.qty,
                        obj.rating,
                        obj.validate,
                        obj.full_desc,
                        obj.value,
                        obj.views,
                        obj.impressions,
                        obj.status,
                        obj.idkey,
                        obj.prio,
                        obj.rules,
                        obj.how,
                        obj.codcoupon,
                        obj.coupontrash,
                        obj.marca,
                        obj.ratingprice))
                }

                couponList.shuffle()

                val adapter = CouponAdapter(couponList)

                couponsRecycler.adapter = adapter
            }

        }

        btnScrollAlimentacao.setOnClickListener {

            scrollSelecao("alimentação")
            colorButton(btnScrollAlimentacao)
        }
        btnScrollCosmeticos.setOnClickListener {

            scrollSelecao("cosméticos")
            colorButton(btnScrollCosmeticos)
        }
        btnScrollJoiasebijouterias.setOnClickListener {

            scrollSelecao("jóias e bijouterias")
            colorButton(btnScrollJoiasebijouterias)
        }
        btnScrollFavoritos.setOnClickListener {

            scrollSelecaoFavoritos()
            colorButton(btnScrollFavoritos)
        }

        /*btnScrollSugestoes.setOnClickListener {

            scrollSelecaoSugestao()
            colorButton(btnScrollSugestoes)
        }*/

    }

    private fun scrollSelecao(botao: String) {

        couponsRecycler.setRecycledViewPool(couponsRecycler.recycledViewPool)

        db.collection("coupons").get().addOnSuccessListener { query ->
            val couponList = ArrayList<CouponItem>()

            for (doc in query) {

                val obj = doc.toObject<CouponItem>(CouponItem::class.java)
                if (botao == obj.category) {
                    couponList.add(CouponItem(obj.id,
                        obj.img,
                        obj.brand,
                        obj.category,
                        obj.cost.toFloat(),
                        obj.description,
                        obj.disc.toString(),
                        obj.qty,
                        obj.rating,
                        obj.validate,
                        obj.full_desc,
                        obj.value,
                        obj.views,
                        obj.impressions,
                        obj.status,
                        obj.idkey,
                        obj.prio,
                        obj.rules,
                        obj.how,
                        obj.codcoupon,
                        obj.coupontrash,
                        obj.marca,
                        obj.ratingprice))
                }

            }

            val adapter = CouponAdapter(couponList)
            couponsRecycler.adapter = adapter

        }
    }

    private fun scrollSelecaoFavoritos() {

        couponsRecycler.setRecycledViewPool(couponsRecycler.recycledViewPool)

        Log.d("testecoupons", "Eita")
        db.collection("user_activity").document(currentUser!!.uid).collection("liked_coupons")
            .get().addOnSuccessListener { docs ->
            val couponList = ArrayList<CouponItem>()
            if (couponList.isNullOrEmpty()) {
                for (doc in docs) {
                    val fav = doc.getBoolean("isFav")
                    Log.d("testecoupons2", fav.toString())
                    val viewed = doc.id
                    if (fav == true){
                        Log.d("testecoupons", viewed.toString())
                        db.collection("coupons").document(viewed).get().addOnSuccessListener { doc2 ->
                            Log.d("testecoupons", "Eita2")
                            Log.d("testecoupons", "Eita3")
                            val obj = doc2.toObject<CouponItem>(CouponItem::class.java)
                            Log.d("testecoupons", obj?.id)
                            couponList.add(CouponItem(obj!!.id,
                                obj.img,
                                obj.brand,
                                obj.category,
                                obj.cost.toFloat(),
                                obj.description,
                                obj.disc.toString(),
                                obj.qty,
                                obj.rating,
                                obj.validate,
                                obj.full_desc,
                                obj.value,
                                obj.views,
                                obj.impressions,
                                obj.status,
                                obj.idkey,
                                obj.prio,
                                obj.rules,
                                obj.how,
                                obj.codcoupon,
                                obj.coupontrash,
                                obj.marca,
                                obj.ratingprice))

                            couponList.sortWith(compareByDescending({ it.views }))
                            val adapter = CouponAdapter(couponList)
                            Log.d("testecoupons", couponList.toString())
                            Log.d("testecoupons", "${couponList.size.toString()}")
                            couponsRecycler.adapter = adapter
                        }
                    }
                    }

            }
            val adapter = CouponAdapter(couponList)
            couponsRecycler.adapter = adapter
        }
    }

    /*private fun scrollSelecaoSugestao() {

        couponsRecycler.setRecycledViewPool(couponsRecycler.recycledViewPool)

        GetMySuggestion().readSuggestion(object : GetMySuggestion.MyCallbackSuggestion {
            override fun onCallBack(suggestion: ArrayList<String>) {
                var couponList = ArrayList<CouponItem>()
                var couponListdistinct = ArrayList<CouponItem>()
                var suggestioncomprime = suggestion.distinctBy { it.length }

                if (suggestioncomprime.isNullOrEmpty()) {
                    for (i in suggestioncomprime.indices) {
                        Log.d("suggcoupons", suggestioncomprime[i])
                        var sugg = suggestioncomprime[i]
                        FirebaseFirestore.getInstance().collection("coupons")
                            ./*whereEqualTo("brand",sugg).*/get().addOnSuccessListener { docs ->
                            for (doc in docs) {
                                Log.d("docincoupons", doc.toString())
                                FirebaseFirestore.getInstance().collection("coupons")
                                    .document(doc.id).get().addOnSuccessListener { docs2 ->
                                    var obj = docs2.toObject<CouponItem>(CouponItem::class.java)

                                    var existeOitem = false
                                    if (obj!!.brand == suggestioncomprime[i] || obj.description == suggestioncomprime[i] || obj.ratingprice.toString() == suggestioncomprime[i] || obj.marca == suggestioncomprime[i] || obj.category == suggestioncomprime[i]) {

                                        existeOitem = true

                                    } else {

                                        existeOitem = false

                                    }

                                    if (existeOitem == true) {
                                        Log.d("suggcoupons", obj?.id)
                                        couponList.add(CouponItem(obj!!.id,
                                            obj.img,
                                            obj.brand,
                                            obj.category,
                                            obj.cost.toFloat(),
                                            obj.description,
                                            obj.disc.toString(),
                                            obj.qty,
                                            obj.rating,
                                            obj.validate,
                                            obj.full_desc,
                                            obj.value,
                                            obj.views,
                                            obj.impressions,
                                            obj.status,
                                            obj.idkey,
                                            obj.prio,
                                            obj.rules,
                                            obj.how,
                                            obj.codcoupon,
                                            obj.coupontrash,
                                            obj.marca,
                                            obj.ratingprice))


                                        val adapter =
                                            CouponAdapter(couponList.distinctBy { it.idkey } as ArrayList<CouponItem>)
                                        Log.d("testecoupons", couponList.toString())
                                        Log.d("testecoupons", "${couponList.size.toString()}")
                                        couponsRecycler.adapter = adapter
                                    }
                                }

                            }

                        }

                    }
                }

            }

        })
    }*/


    @SuppressLint("ResourceAsColor")
    private fun colorButton(clicked: Button) {

        btnScrollTodos.setBackgroundResource(R.drawable.border_button_white_green)
        btnScrollAlimentacao.setBackgroundResource(R.drawable.border_button_white_green)
        btnScrollCosmeticos.setBackgroundResource(R.drawable.border_button_white_green)
        btnScrollJoiasebijouterias.setBackgroundResource(R.drawable.border_button_white_green)
        btnScrollFavoritos.setBackgroundResource(R.drawable.border_button_white_green)
        //btnScrollSugestoes.setBackgroundResource(R.drawable.border_button_white_green)

        btnScrollTodos.setTextColor(R.color.colorPrimaryDark)
        btnScrollAlimentacao.setTextColor(R.color.colorPrimaryDark)
        btnScrollCosmeticos.setTextColor(R.color.colorPrimaryDark)
        btnScrollJoiasebijouterias.setTextColor(R.color.colorPrimaryDark)
        btnScrollFavoritos.setTextColor(R.color.colorPrimaryDark)
        //btnScrollSugestoes.setTextColor(R.color.colorPrimaryDark)

        val button = clicked
        button.setBackgroundResource(R.drawable.border_button_green_white)
        button.setTextColor(Color.parseColor("#ffffff"))

    }

}

