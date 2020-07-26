package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.trash2money.trashapp.KClasses.OpenDialog
import com.trash2money.trashapp.R

class FragmentConteinerReciclarVidro : Fragment() {

    lateinit var btnAzeite: ImageButton
    lateinit var btnGarrafas: ImageButton
    lateinit var btnPapinhas: ImageButton


    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_conteiner_reciclar_vidro, container, false)

        btnAzeite = v.findViewById(R.id.btn_vid_azeites)
        btnGarrafas = v.findViewById(R.id.btn_vid_garrafas)
        btnPapinhas = v.findViewById(R.id.btn_vid_papinhas)


        btnAzeite.setOnClickListener() {

            OpenDialog(context!!, "vid_azeite").openDialog(v)
        }

        btnGarrafas.setOnClickListener() {

            OpenDialog(context!!, "vid_garrafas").openDialog(v)
        }

        btnPapinhas.setOnClickListener() {

            OpenDialog(context!!, "vid_papinhas").openDialog(v)
        }

        return v
    }

}
