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

class FragmentConteinerReciclarAluminio : Fragment() {

    lateinit var btnLatinha: ImageButton
    lateinit var btnDesodorante: ImageButton
    lateinit var btnPanelas: ImageButton
    lateinit var btnOutros: ImageButton
    lateinit var btnAzeite: ImageButton
    lateinit var btnAchocolatado: ImageButton
    lateinit var btnEnlatados: ImageButton
    lateinit var btnSpray: ImageButton
    lateinit var btnTinta: ImageButton
    lateinit var btnLataLeite : ImageButton
    lateinit var btn: ImageButton

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_conteiner_reciclar_aluminio, container, false)

        btnLatinha = v.findViewById(R.id.btn_lat_aluminium)
        btnDesodorante = v.findViewById(R.id.btn_desodorante)
        btnPanelas = v.findViewById(R.id.btn_panelas)
        btnAchocolatado = v.findViewById(R.id.btn_achocolatado)
        btnOutros = v.findViewById(R.id.btn_diversos)
        btnSpray = v.findViewById(R.id.btn_spray_lata)
        btnTinta = v.findViewById(R.id.btn_lata_tinta)
        btnEnlatados = v.findViewById(R.id.btn_enlatados)
        btnAzeite = v.findViewById(R.id.btn_azeite_lata)
        btnLataLeite = v.findViewById(R.id.btn_lata_leite)

        btnLatinha.setOnClickListener() {
            OpenDialog(context!!, "al_latinhas_aluminio").openDialog(v)
        }

        btnDesodorante.setOnClickListener() {
            OpenDialog(context!!, "al_desodorante").openDialog(v)
        }

        btnPanelas.setOnClickListener() {
            OpenDialog(context!!, "al_panelas").openDialog(v)
        }

        btnEnlatados.setOnClickListener() {
            OpenDialog(context!!, "al_enlatados").openDialog(v)
        }


        btnSpray.setOnClickListener() {
            OpenDialog(context!!, "al_lata_spray").openDialog(v)
        }

        btnOutros.setOnClickListener() {
            OpenDialog(context!!, "al_diversos").openDialog(v)
        }

        btnAchocolatado.setOnClickListener() {
            OpenDialog(context!!, "al_achocolatado_lata").openDialog(v)
        }

        btnAzeite.setOnClickListener() {
            OpenDialog(context!!, "al_azeite_lata").openDialog(v)
        }

        btnTinta.setOnClickListener() {
            OpenDialog(context!!, "al_lata_tinta").openDialog(v)
        }

        btnLataLeite.setOnClickListener() {
            OpenDialog(context!!, "al_lata_leite").openDialog(v)
        }

        return v
    }

}
