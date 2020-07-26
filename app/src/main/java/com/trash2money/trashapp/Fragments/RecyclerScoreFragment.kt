package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Adapters.ScoreFragmentAdapter
import com.trash2money.trashapp.KClasses.ScoreList
import com.trash2money.trashapp.R
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class RecyclerScoreFragment : Fragment() {

    val TAG = "RVC"
    lateinit var recyclerScore : RecyclerView
    lateinit var btn_tcoin : Button
    lateinit var btn_peso : Button
    lateinit var btn_coleta : Button
    lateinit var btn_n_item : Button
    lateinit var txt_desc_rank: TextView

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val v: View
        v = inflater.inflate(R.layout.fragment_recycler_score,container,false)
        recyclerScore = v.findViewById(R.id.recycler_score)
        recyclerScore.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        btn_tcoin = v.findViewById<Button>(R.id.btn_trashCoins)
        btn_peso = v.findViewById<Button>(R.id.btn_peso)
        btn_coleta = v.findViewById<Button>(R.id.btn_coletas)
        btn_n_item = v.findViewById<Button>(R.id.btn_itens)
        txt_desc_rank = v.findViewById(R.id.txt_desc_rank)

        val fm : FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container,Fragment_bottom_bar()).commit()

        updateList(2)
        txt_desc_rank.setText("Veja aqui um Rank das pessoas que mais reciclaram em Peso (Kg)")

        btn_peso.setOnClickListener{
            updateList(2)
            txt_desc_rank.setText("Veja aqui um Rank das pessoas que mais reciclaram em Peso (Kg)")
            colorButton(btn_peso)
        }

        btn_tcoin.setOnClickListener{
            updateList(1)
            txt_desc_rank.setText("Veja aqui um Rank das pessoas que mais receberam TrashCoins")
            colorButton(btn_tcoin)
        }

        btn_coleta.setOnClickListener{
            updateList(3)
            txt_desc_rank.setText("Veja aqui um Rank dos usuários que tiveram mais coletas")
            colorButton(btn_coleta)
        }

        btn_n_item.setOnClickListener{
            updateList(4)
            txt_desc_rank.setText("Veja aqui um Rank dos usuários que reciclaram mais itens")
            colorButton(btn_n_item)
        }

        return v
    }

    @SuppressLint("ResourceAsColor")
    private fun colorButton(clicked: Button) {

        btn_tcoin.setBackgroundResource(R.drawable.border_button_white_green)
        btn_peso.setBackgroundResource(R.drawable.border_button_white_green)
        btn_coleta.setBackgroundResource(R.drawable.border_button_white_green)
        btn_n_item.setBackgroundResource(R.drawable.border_button_white_green)

        btn_tcoin.setTextColor(R.color.colorPrimaryDark)
        btn_peso.setTextColor(R.color.colorPrimaryDark)
        btn_coleta.setTextColor(R.color.colorPrimaryDark)
        btn_n_item.setTextColor(R.color.colorPrimaryDark)

        val button = clicked
        button.setBackgroundResource(R.drawable.border_button_green_white)
        button.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun updateList (btn: Int){
        val btn = btn

        val scoreList = ArrayList<ScoreList>()

        val ref = FirebaseFirestore.getInstance().collection("user_activity")
        Log.d(TAG, "REF")

        ref.get().addOnSuccessListener {users->

            for (user in users){
                Log.d(TAG+" user", user.id)

                val tcoin = user["tcoins"].toString().toBigDecimal().setScale(1,RoundingMode.HALF_EVEN).toDouble()
                var peso = user["peso"].toString().toBigDecimal().setScale(1,RoundingMode.HALF_EVEN).toDouble()
                peso = peso / 1000.0
                peso = peso.toBigDecimal().setScale(1,RoundingMode.HALF_EVEN).toDouble()
                val nRec = user["n_recy"].toString().toDouble().toInt()
                val nItem = user["n_item"].toString().toDouble().toInt()

                FirebaseFirestore.getInstance().collection("users").document(user.id).get().addOnSuccessListener { user->

                    val userName = user["name"].toString()
                    scoreList.add(ScoreList(userName, tcoin, peso, nRec,nItem))
                    val adapter =
                        ScoreFragmentAdapter(
                            scoreList,btn)
                    recyclerScore.adapter = adapter
                    sortArrayList(scoreList,btn)
                }

            }
        }

    }

    private fun sortArrayList(arrayList: ArrayList<ScoreList>, type : Int){
        val arrayList = arrayList
        Collections.sort(arrayList, object: Comparator<ScoreList>{
            override fun compare(p0: ScoreList?, p1: ScoreList?): Int {
                var comp = 0

                when(type){
                    1->{
                        comp = (p1!!.tcoins.compareTo(p0!!.tcoins))
                    }
                    2->{
                        comp = p1!!.weight.compareTo(p0!!.weight)
                    }
                    3->{
                        comp = p1!!.num.compareTo(p0!!.num)
                    }
                    4->{
                        comp = p1!!.itens.compareTo(p0!!.itens)
                    }
                }

                return comp
            }
        })
        recyclerScore.adapter?.notifyDataSetChanged()
    }
}

