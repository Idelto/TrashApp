package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Adapters.TCoinsBalanceAdapter
import com.trash2money.trashapp.KClasses.ConvertTo
import com.trash2money.trashapp.KClasses.ProgressBarDialog
import com.trash2money.trashapp.R
import com.trash2money.trashapp.KClasses.TCoinsBalanceItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class FragmentMyTrashCoinsBalance : Fragment() {

    lateinit var tCoinsB: RecyclerView
    lateinit var txtCoins: TextView
    lateinit var btnTrocarCupons: Button

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_my_trash_coins_balance, container, false) as View
        val dialog = ProgressBarDialog(context!!)

        dialog.openDialog()

        tCoinsB = v.findViewById<RecyclerView>(R.id.couponsRecycler)
        txtCoins = v.findViewById(R.id.txtTotalTCoins)
        btnTrocarCupons = v.findViewById(R.id.ChangeCoupons)
        tCoinsB.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        val balanceList = ArrayList<TCoinsBalanceItem>()

        val currUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("trashcoins")
        val docRef = db.get()
        docRef.addOnSuccessListener { docs ->

            Log.d("DOCREF", docRef.toString())
            Log.d("DOCREF SIZE ", docs.size().toString())

            var totalBalance = 0.0

            for (doc in docs) {

                Log.d("DOC", doc.toString())
                Log.d("DOC", doc.id.toString())

                db.document(doc.id).get().addOnSuccessListener {

                    val value = doc["value"].toString().toDouble()

                    val decf = DecimalFormat("#.##")
                    decf.roundingMode = RoundingMode.CEILING

                    val dValue = ConvertTo().convert2casasTrashCoins(value) //decf.format(value)

                    totalBalance += value

                    val mills = doc["date"]
                    val df = SimpleDateFormat("dd-MMM-yyyy")
                    val date = df.format(mills).toString()

                    Log.d("Date", date)
                    Log.d("Value", value.toString())

                    balanceList.add(TCoinsBalanceItem(date, dValue.toString()))

                    balanceList.sortByDescending { it.date }
                    val adapter = TCoinsBalanceAdapter(balanceList)
                    tCoinsB.adapter = adapter

                    val tBalance = ConvertTo().convert2casasTrashCoins(totalBalance)

                    txtCoins.setText(tBalance.toString())

                }

            }

            dialog.closeDialog()
        }

        val adapter = TCoinsBalanceAdapter(balanceList)

        tCoinsB.adapter = adapter

        btnTrocarCupons.setOnClickListener {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentCoupons")
            startActivity(intent)

        }

        return v
    }

}
