package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Adapters.MyRequestHistoryAdapter
import com.trash2money.trashapp.KClasses.OnGoingItem
import com.trash2money.trashapp.KClasses.Trash
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentMyRequestTabHistory : Fragment() {

    lateinit var recycler_onGoing: RecyclerView
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_my_request_tab_history, container, false) as View

        recycler_onGoing = v.findViewById(R.id.recycler_history)
        recycler_onGoing.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        val docRef = db.collection("user_activity").document(currentUser?.uid.toString())
            .collection("trashcans")

        val docs = docRef.whereEqualTo("status", 5)

        docs.get().addOnSuccessListener {

            val onGoingList = ArrayList<OnGoingItem>()

            for (d in it) {

                docRef.document(d.id).collection("trash_item").get()
                    .addOnSuccessListener { querySnapshot ->

                        var qtyTotal = 0F
                        var weightTotal = 0F
                        var tCoinTotal = 0F

                        val status = d.get("status")
                        var progress = 0

                        when (status) {
                            "null" -> progress = 0
                            "ready" -> progress = 33
                            "onGoing" -> progress = 66
                            "done" -> progress = 100
                        }

                        for (x in querySnapshot) {

                            val obj = x.toObject<Trash>(Trash::class.java)
                            tCoinTotal += obj.trashcoins
                            weightTotal += obj.weight

                        }

                        val date = Date(d.id.toLong())
                        val mils = d.id.toLong() as Long
                        val calendar = Calendar.getInstance()

                        calendar.setTimeInMillis(mils)
                        val day = calendar.get(Calendar.DAY_OF_WEEK)

                        var milis = d.id.toLong()
                        var weekday = ""
                        when (day) {
                            2 -> weekday = "Seg"
                            3 -> weekday = "Ter"
                            4 -> weekday = "Qua"
                            5 -> weekday = "Qui"
                            6 -> weekday = "Sex"
                            7 -> weekday = "Sáb"
                            1 -> weekday = "Dom"
                        }

                        Log.d("DAY", weekday.toString())
                        val fformat = SimpleDateFormat("dd/MM/yyyy")
                        val strDate = fformat.format(date)

                        Log.d("TAG DOCS", d.id)
                        Log.d("TAG DOCS", strDate)
                        Log.d("TAG DOCS", tCoinTotal.toString())
                        Log.d("TAG DOCS", weightTotal.toString())
                        val pathStr ="user_activity/${currentUser?.uid.toString()}/trashcans/${d.id}"

                        onGoingList.add(OnGoingItem(strDate,
                            weekday,
                            tCoinTotal.toString(),
                            weightTotal.toString(),
                            100,
                            pathStr))

                        val adapter = MyRequestHistoryAdapter(onGoingList)
                        recycler_onGoing.adapter = adapter
                        sortArrayList(onGoingList)
                    }

            }

        }

        return v
    }

    private fun sortArrayList(onGoingList: ArrayList<OnGoingItem>){
        val arrayList = onGoingList
        Collections.sort(arrayList, object: Comparator<OnGoingItem>{

            override fun compare(p0: OnGoingItem?, p1: OnGoingItem?): Int {
                return p1!!.date.compareTo(p0!!.date)
            }

        })
        recycler_onGoing.adapter?.notifyDataSetChanged()
    }

}