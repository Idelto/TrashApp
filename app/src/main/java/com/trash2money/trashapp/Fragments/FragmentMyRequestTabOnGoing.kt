package com.trash2money.trashapp.Fragments

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Adapters.MyRequestOnGoingAdapter
import com.trash2money.trashapp.KClasses.OnGoingItem
import com.trash2money.trashapp.KClasses.Trash
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentMyRequestTabOnGoing : Fragment() {

    lateinit var recycler_onGoing: RecyclerView
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var simpleCallback: ItemTouchHelper.SimpleCallback

    @Nullable
    @Override

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_my_request_tab_ongoing, container, false) as View

        recycler_onGoing = v.findViewById(R.id.recycler_onGoing)

        recycler_onGoing.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        val docRef = db.collection("user_activity").document(currentUser?.uid.toString())
            .collection("trashcans")

        val docs = docRef.whereLessThan("status", 5)

        docs.get().addOnSuccessListener {

            val onGoingList = ArrayList<OnGoingItem>()

            for (d in it) {

                docRef.document(d.id).collection("trash_item").get()
                    .addOnSuccessListener { querySnapshot ->

                        var qtyTotal = 0F
                        var weightTotal = 0F
                        var tCoinTotal = 0F

                        val status = d.get("status").toString().toInt()
                        Log.d("Status", status.toString())

                        var progress = 0

                        when (status) {
                            0 -> progress = 0
                            1 -> progress = 30
                            2 -> progress = 50
                            3 -> progress = 70
                            4 -> progress = 80
                            5 -> progress = 100
                        }
                        Log.d("Progress", progress.toString())

                        for (x in querySnapshot) {

                            val documentPath = x.id
                            Log.d("DOCUMENT_PATH", documentPath)

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

                        val trashid = d.id
                        val pathStr =
                            "user_activity/${currentUser?.uid.toString()}/trashcans/${d.id}"

                        Log.d("TAG DOCS", trashid)
                        Log.d("TAG DOCS", strDate)
                        Log.d("TAG DOCS", tCoinTotal.toString())
                        Log.d("TAG DOCS", weightTotal.toString())

                        onGoingList.add(OnGoingItem(strDate,
                            weekday,
                            tCoinTotal.toString(),
                            weightTotal.toString(),
                            progress,
                            pathStr))

                        val adapter = MyRequestOnGoingAdapter(onGoingList)
                        recycler_onGoing.adapter = adapter
                        sortArrayList(onGoingList)

                        simpleCallback = (object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                                TODO("Not yet implemented")
                            }

                            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                                val position = viewHolder.adapterPosition

                                val id = onGoingList[position].trashID

                                val db2 = FirebaseFirestore.getInstance()
                                val currUser = FirebaseAuth.getInstance().currentUser

                                val docRef = db2.document(id)

                                docRef.get().addOnSuccessListener { doc->
                                    val status = doc["status"].toString().toInt()
                                    Log.d("DEL_DOC", status.toString())
                                    if(status < 3){
                                        docRef.delete()
                                        Toast.makeText(context, "Documento Deletado com sucesso", Toast.LENGTH_LONG).show()
                                        recycler_onGoing.adapter!!.notifyItemRemoved(position)
                                        val intent = Intent(context, HomeActivity::class.java)
                                        intent.putExtra("fragment", "FragmentMyRequests")
                                        startActivity(intent)
                                    }else{
                                        Toast.makeText(context, "Não é Possível deletar um documento agendado. Favor entre em contato com suporte na guia 'AJUDA' do menu lateral", Toast.LENGTH_LONG).show()
                                        val intent = Intent(context, HomeActivity::class.java)
                                        intent.putExtra("fragment", "FragmentMyRequests")
                                        startActivity(intent)
                                    }

                                }

                            }

                            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                                RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive).addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!, R.color.colormarsalla)).addSwipeLeftActionIcon(R.drawable.ic_delete_white_50dp).create().decorate()

                                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            }

                    })
                        val itemTouchHelper = ItemTouchHelper(simpleCallback)
                        itemTouchHelper.attachToRecyclerView(recycler_onGoing)

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

