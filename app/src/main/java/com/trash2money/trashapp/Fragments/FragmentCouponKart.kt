package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Adapters.CoupomKartAdapter
import com.trash2money.trashapp.KClasses.ConvertTo
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.KClasses.TCoinBalance
import com.trash2money.trashapp.KClasses.UserData
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.math.BigDecimal
import java.math.RoundingMode

class FragmentCouponKart : Fragment() {

    lateinit var recyclerCouponKart: RecyclerView
    lateinit var txtCouponValue: TextView
    lateinit var txtsaldoAtualValue: TextView
    lateinit var txtSaldoFinalValue: TextView
    lateinit var btnCouponKartRescue: Button
    lateinit var simpleCallback: ItemTouchHelper.SimpleCallback

    fun updateValues(v: View) {

        val view = v
        Log.d("VIEW ID FUN", view.toString())

        txtCouponValue = view.findViewById(R.id.txtCuponsValue)
        txtsaldoAtualValue = view.findViewById(R.id.txtsaldoAtualValue)
        txtSaldoFinalValue = view.findViewById(R.id.txtSaldoFinalValue)
        btnCouponKartRescue = view.findViewById<Button>(R.id.btnCouponKartRescue)

        txtSaldoFinalValue.text = "KKKKKK"
        txtSaldoFinalValue.text = "KKKKKK"

    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_coupon_kart, container, false) as View

        txtCouponValue = v.findViewById(R.id.txtCuponsValue)
        txtsaldoAtualValue = v.findViewById(R.id.txtsaldoAtualValue)
        txtSaldoFinalValue = v.findViewById(R.id.txtSaldoFinalValue)
        btnCouponKartRescue = v.findViewById<Button>(R.id.btnCouponKartRescue)

        Log.d("VIEW ID ROOTS", v.toString())

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        recyclerCouponKart = v.findViewById(R.id.recyclerCouponKart)
        recyclerCouponKart.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val currUser = FirebaseAuth.getInstance().currentUser

        val couponKartList = ArrayList<CouponItem>()
        var tCost = 0.0

        TCoinBalance().getTCoin(object : TCoinBalance.MyCallBackTcoins {

            override fun onCallBackTCoin(tCoin: Double) {

                val saldoatual =  ConvertTo().convert2casasTrashCoins(tCoin)
                txtsaldoAtualValue.setText(saldoatual)

            }

        })

        UserData().getCouponItem(object : UserData.MyCallBackCoupons {

            override fun onCallBackCoupons(coupon: CouponItem) {

                couponKartList.add(CouponItem(coupon.id,
                    coupon.img,
                    coupon.brand,
                    coupon.category,
                    coupon.cost,
                    coupon.description,
                    coupon.disc,
                    coupon.qty,
                    coupon.rating,
                    coupon.validate,
                    coupon.full_desc,
                    coupon.value,
                    coupon.views,
                    coupon.impressions,
                    coupon.status,
                    coupon.idkey,
                    coupon.prio,
                    coupon.rules,
                    coupon.how,
                    coupon.codcoupon,
                    coupon.coupontrash,
                    coupon.marca,
                    coupon.ratingprice))
                Log.d("TagCouponid", coupon.id)
                tCost += coupon.cost

                TCoinBalance().getTCoin(object : TCoinBalance.MyCallBackTcoins {

                    override fun onCallBackTCoin(tCoin: Double) {

                        var tCoin = tCoin.toString().toDouble()
                        tCoin = BigDecimal(tCoin).setScale(2, RoundingMode.HALF_EVEN).toDouble()

                        txtsaldoAtualValue.setText(ConvertTo().convert2casasTrashCoins(tCoin))
                        txtCouponValue.setText(ConvertTo().convert2casasTrashCoins(tCost))

                        var saldoFinal = tCoin - tCost
                        Log.d("SALDOFINAL", saldoFinal.toString())

                        txtSaldoFinalValue.setText(ConvertTo().convert2casasTrashCoins(saldoFinal))

                        saldoFinal = txtSaldoFinalValue.text.toString().toDouble()

                        if (couponKartList.size <= 0) {

                            btnCouponKartRescue.setText("Escolha seus Cupons")

                        } else if (saldoFinal < 0 && couponKartList.size > 0) {
                            Log.d("SaldoFinal", "${saldoFinal < 0}")
                            Log.d("SaldoFinal", "$saldoFinal")
                            btnCouponKartRescue.setText("T Coins Insuficientes")
                            btnCouponKartRescue.isEnabled = false
                        } else {
                            btnCouponKartRescue.setText("Resgatar já")
                            btnCouponKartRescue.isEnabled = true
                        }

                    }

                })

                Log.d("tag11", "${couponKartList.size}")

                val adapter = CoupomKartAdapter(couponKartList)
                recyclerCouponKart.adapter = adapter
            }

        })

        val saldoFinal = txtSaldoFinalValue.text.toString().toDouble()

        if (couponKartList.size <= 0) {

            btnCouponKartRescue.setText("Escolha seus Cupons")

        } else if (saldoFinal < 0 && couponKartList.size > 0) {
            Log.d("SaldoFinal", "${saldoFinal < 0}")
            Log.d("SaldoFinal", "$saldoFinal")
            btnCouponKartRescue.setText("T Coins Insuficientes")
            btnCouponKartRescue.isEnabled = false
        } else {
            btnCouponKartRescue.setText("Resgatar já")
        }

        simpleCallback = (object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val id = couponKartList[position].id

                val db2 = FirebaseFirestore.getInstance()
                val currUser = FirebaseAuth.getInstance().currentUser!!.uid

                val db =
                    FirebaseFirestore.getInstance().collection("coupons_kart").document(currUser)
                        .collection("coupon_item").document(id).delete()

                Toast.makeText(context, "Documento Deletado com sucesso", Toast.LENGTH_LONG).show()
                recyclerCouponKart.adapter!!.notifyItemRemoved(position)
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("fragment", "FragmentCouponKart")
                startActivity(intent)

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(context,
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive).addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!,
                    R.color.colormarsalla)).addSwipeLeftActionIcon(R.drawable.ic_delete_white_50dp)
                    .create().decorate()

                super.onChildDraw(c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive)
            }

        })

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerCouponKart)

        btnCouponKartRescue.setOnClickListener() {
            val saldoFinalbtn = txtSaldoFinalValue.text.toString().toDouble()
            Log.d("tag12", "$saldoFinalbtn")
            if (saldoFinalbtn >= 0.0 && couponKartList.size > 0) {

                Log.d("COUPON KART LIST ", couponKartList.size.toString())

                val db = FirebaseFirestore.getInstance().collection("user_activity")
                    .document(currUser?.uid.toString())

                val dbCoupon = db.collection("coupons")
                val dbBalance = db.collection("trashcoins")

                for (item in couponKartList) {

                    if (item.qty <= 0) {
                        Toast.makeText(activity,
                            "O cupom ${item.description} não está disponivel no momento",
                            Toast.LENGTH_LONG)
                    } else {

                        Log.d("tag12", item.idkey)
                        val coupoulist =
                            couponKartList.groupingBy { it }.eachCount().filter { it.value > 1 }
                        Log.d("objeto", "${coupoulist.toString()}")
                        val curruser = FirebaseAuth.getInstance().currentUser!!.uid
                        updateqtyfirebase(item, curruser)

                        val value = -1 * item.cost

                        if (item.coupontrash == true) {
                            dbCoupon.add(item)
                            val alphabet: List<Char> = /*('a'..'z') +*/ ('A'..'Z') + ('0'..'9')
                            val randomCodCoupon = List(20) { alphabet.random() }.joinToString("")
                            dbCoupon.whereEqualTo("id", item.id).get()
                                .addOnSuccessListener { docs ->
                                    for (doc in docs) {
                                        dbCoupon.document(doc.id)
                                            .update("codcoupon", randomCodCoupon)
                                    }
                                }
                        } else {
                            dbCoupon.add(item)
                        }

                        Log.d("txtlog4", "${item.id}")
                        dbBalance.document(System.currentTimeMillis().toString())
                            .set(hashMapOf("value" to value, "date" to System.currentTimeMillis()))
                        Log.d("ADICIONADO", "COUPONS ADICIONADO COM SUCESSOS")
                        val db2 = FirebaseFirestore.getInstance()
                        val docRef =
                            db2.collection("coupons_kart").document(currUser?.uid.toString())

                        docRef.collection("coupon_item").get().addOnSuccessListener { query ->
                            docRef.collection("coupon_item").document(item.id).delete()
                            Log.d("DOC DELETADO", "DOCUMENTO ${item.id.toString()} DELETADO")
                            Toast.makeText(context,
                                "Cupom adicionado com sucesso !!!",
                                Toast.LENGTH_LONG).show()

                            val intent = Intent(activity, HomeActivity::class.java)
                            intent.putExtra("fragment", "FragmentCouponKart")
                            startActivity(intent)
                        }
                    }
                }

            } else if (couponKartList.size <= 0) {
                Log.d("tag13", "falta cupons")
                Toast.makeText(activity, "Seu carrinho não possui cupons", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("fragment", "FragmentCoupons")
                startActivity(intent)
            }
        }

        return v
    }

    private fun updateqtyfirebase(item: CouponItem, curruser: String) {
        Log.d("quantidadeDeLoops", "lops")
        Log.d("couponID", item.id)
        var rpt = 0
        FirebaseFirestore.getInstance().collection("coupons_kart").document(curruser)
            .collection("coupon_item").get().addOnSuccessListener { docs ->
            for (doc in docs) {
                if (doc["idkey"] == item.idkey) {
                    rpt += 1
                }
            }
            Log.d("tag33rpt", rpt.toString())
            val quantity = item.qty - rpt
            FirebaseFirestore.getInstance().collection("coupons").document(item.idkey)
                .update("qty", quantity).addOnSuccessListener {
                Log.d("tag33alterado", "alterado !")
            }

        }

    }

    override fun onResume() {

        super.onResume()
    }

}


