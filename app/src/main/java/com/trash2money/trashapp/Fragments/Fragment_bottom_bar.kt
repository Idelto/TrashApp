package com.trash2money.trashapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.R

class Fragment_bottom_bar : Fragment() {

    lateinit var btnHome: LinearLayout
    lateinit var btnRecicle: LinearLayout
    lateinit var btnTrashcoin: LinearLayout
    lateinit var btnDelete: LinearLayout
    lateinit var btnHome2: ImageButton
    lateinit var btnRecicle2: ImageButton
    lateinit var btnTrashcoin2: ImageButton
    lateinit var btnDelete2: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_bottom_bar, container, false)

        btnHome = v!!.findViewById(R.id.lyt_home)
        btnRecicle = v!!.findViewById(R.id.lyt_recicle)
        btnTrashcoin = v!!.findViewById(R.id.lyt_trashcoins)
        btnDelete = v!!.findViewById(R.id.lyt_lixeira)
        btnHome2 = v!!.findViewById(R.id.bb_btn_home)
        btnRecicle2 = v!!.findViewById(R.id.bb_btn_recycle)
        btnTrashcoin2 = v!!.findViewById(R.id.bb_btn_trashCoin)
        btnDelete2 = v!!.findViewById(R.id.bb_btn_lixeira)

        btnHome.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)

        }

        btnDelete.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinsCan")
            startActivity(intent)

        }

        btnTrashcoin.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinBalance")
            startActivity(intent)

        }

        btnRecicle.setOnClickListener() {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentRecycleMain")
            startActivity(intent)

        }

        btnHome2.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)

        }

        btnDelete2.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinsCan")
            startActivity(intent)

        }

        btnTrashcoin2.setOnClickListener() {

            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyTrashCoinBalance")
            startActivity(intent)

        }

        btnRecicle2.setOnClickListener() {

            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentRecycleMain")
            startActivity(intent)

        }

        return v
    }


}