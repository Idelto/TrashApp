package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.R

class FragmentRecycleMain : Fragment() {

    lateinit var imgBtnslidedownAluminium: ImageView
    lateinit var fragmentAluminio: FrameLayout
    lateinit var aluminiumSlideUp: LinearLayout
    lateinit var imgBtnslideupAluminium: ImageView
    lateinit var imgBtnslidedownPapel: ImageView
    lateinit var fragmentPapel: FrameLayout
    lateinit var papelSlideUp: LinearLayout
    lateinit var imgBtnslideupPapel: ImageView
    lateinit var imgBtnslidedownPlastico: ImageView
    lateinit var fragmentPlastico: FrameLayout
    lateinit var plasticoSlideUp: LinearLayout
    lateinit var imgBtnslideupPlastico: ImageView
    lateinit var imgBtnslidedownPapelao: ImageView
    lateinit var fragmentPapelao: FrameLayout
    lateinit var papelaoSlideUp: LinearLayout
    lateinit var imgBtnslideupPapelao: ImageView
    lateinit var imgBtnslidedownVidro: ImageView
    lateinit var fragmentVidro: FrameLayout
    lateinit var vidroSlideUp: LinearLayout
    lateinit var imgBtnslideupVidro: ImageView
    lateinit var imgBtnslidedownEletronico: ImageView
    lateinit var fragmentEletronico: FrameLayout
    lateinit var eletronicoSlideUp: LinearLayout
    lateinit var imgBtnslideupEletronico: ImageView
    lateinit var imgBtnslidedownOleo: ImageView
    lateinit var fragmentOleo: FrameLayout
    lateinit var oleoSlideUp: LinearLayout
    lateinit var imgBtnslideupOleo: ImageView
    lateinit var imgBtnslidedownMetais: ImageView
    lateinit var imgBtnslidedownOutros: ImageView

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recycle_main, container, false) as View

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        //Aluminio

        fragmentAluminio = v.findViewById(R.id.container_aluminio)
        imgBtnslidedownAluminium = v.findViewById(R.id.img_slidedownAluminio)
        aluminiumSlideUp = v.findViewById(R.id.linearlayout_aluminium_slideup)
        imgBtnslideupAluminium = v.findViewById(R.id.img_slideupAluminio)

        //Papel

        fragmentPapel = v.findViewById(R.id.container_papeis)
        imgBtnslidedownPapel = v.findViewById(R.id.img_slidedownPapeis)
        papelSlideUp = v.findViewById(R.id.linearlayout_papeis_slideup)
        imgBtnslideupPapel = v.findViewById(R.id.img_slideupPapeis)

        //Plastico

        fragmentPlastico = v.findViewById(R.id.container_plastico)
        imgBtnslidedownPlastico = v.findViewById(R.id.img_slidedownPlastico)
        plasticoSlideUp = v.findViewById(R.id.linearlayout_plastico_slideup)
        imgBtnslideupPlastico = v.findViewById(R.id.img_slideupPlastico)

        //Papelao

        //Vidro

        fragmentVidro = v.findViewById(R.id.container_vidro)
        imgBtnslidedownVidro = v.findViewById(R.id.img_slidedownVidro)
        vidroSlideUp = v.findViewById(R.id.linearlayout_vidro_slideup)
        imgBtnslideupVidro = v.findViewById(R.id.img_slideupVidro)

        //Eletronico

        fragmentEletronico = v.findViewById(R.id.container_eletronicos)
        imgBtnslidedownEletronico = v.findViewById(R.id.img_slidedownEletronicos)
        eletronicoSlideUp = v.findViewById(R.id.linearlayout_eletronicos_slideup)
        imgBtnslideupEletronico = v.findViewById(R.id.img_slideupEletronicos)

        //Oleo

        fragmentOleo = v.findViewById(R.id.container_oleo)
        imgBtnslidedownOleo = v.findViewById(R.id.img_slidedownOleo)
        oleoSlideUp = v.findViewById(R.id.linearlayout_oleo_slideup)
        imgBtnslideupOleo = v.findViewById(R.id.img_slideupOleo)

        //Metais

        imgBtnslidedownMetais = v.findViewById(R.id.img_slidedownMetais)

        //Metais

        //Botoes Aluminio

        imgBtnslidedownAluminium.setOnClickListener() {

            Log.d("tag0 ", "clicado")
            slidedownOnClick(fragmentAluminio,
                aluminiumSlideUp,
                imgBtnslidedownAluminium,
                FragmentConteinerReciclarAluminio(),
                "aluminio")
        }

        imgBtnslideupAluminium.setOnClickListener() {

            slideupOnClick(fragmentAluminio, aluminiumSlideUp, imgBtnslidedownAluminium)

        }

        //Botoes Papel

        imgBtnslidedownPapel.setOnClickListener() {

            Log.d("tag0 ", "clicado")
            slidedownOnClick(fragmentPapel,
                papelSlideUp,
                imgBtnslidedownPapel,
                FragmentConteinerReciclarPapel(),
                "papel")
        }

        imgBtnslideupPapel.setOnClickListener() {

            slideupOnClick(fragmentPapel, papelSlideUp, imgBtnslidedownPapel)
        }

        //Botoes Plastico

        imgBtnslidedownPlastico.setOnClickListener() {

            Log.d("tag0 ", "clicado")
            slidedownOnClick(fragmentPlastico,
                plasticoSlideUp,
                imgBtnslidedownPlastico,
                FragmentConteinerReciclarPlastico(),
                "plastico")
        }

        imgBtnslideupPlastico.setOnClickListener() {

            slideupOnClick(fragmentPlastico, plasticoSlideUp, imgBtnslidedownPlastico)
        }

        //Botoes Papelao

        //Botoes Vidro

        imgBtnslidedownVidro.setOnClickListener() {
            Log.d("tag0 ", "clicado")
            slidedownOnClick(fragmentVidro,
                vidroSlideUp,
                imgBtnslidedownVidro,
                FragmentConteinerReciclarVidro(),
                "vidro")
        }

        imgBtnslideupVidro.setOnClickListener() {
            slideupOnClick(fragmentVidro, vidroSlideUp, imgBtnslidedownVidro)
        }

        //Botoes Eletronico

        imgBtnslidedownEletronico.setOnClickListener() {
            Log.d("tag0 ", "clicado")
            //slidedownOnClick(fragmentEletronico,eletronicoSlideUp,imgBtnslidedownEletronico,FragmentConteinerReciclarEletronico(),"eletronico")
            Log.d("tag0 ", "clicado")
            Toast.makeText(context,
                "Em breve essa funcionalidade estará disponível",
                Toast.LENGTH_SHORT).show()
        }

        imgBtnslideupEletronico.setOnClickListener() {
            slideupOnClick(fragmentEletronico, eletronicoSlideUp, imgBtnslidedownEletronico)
        }

        //Botoes Eletronico

        imgBtnslidedownOleo.setOnClickListener() {
            Toast.makeText(context,
                "Em breve essa funcionalidade estará disponível",
                Toast.LENGTH_SHORT).show()
        }

        //Botoes Metais

        imgBtnslidedownMetais.setOnClickListener() {
            Log.d("tag0 ", "clicado")
            Toast.makeText(context,
                "Em breve essa funcionalidade estará disponível",
                Toast.LENGTH_SHORT).show()
        }

        //Botoes Outros

        return v

    }

    private fun slideupOnClick(fa: FrameLayout, aSU: LinearLayout, btnaSD: ImageView) {
        fa.visibility = View.GONE
        aSU.visibility = View.GONE
        btnaSD.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_colortext_24dp)
    }

    private fun slidedownOnClick(
        fa: FrameLayout,
        aSU: LinearLayout,
        btnaSD: ImageView,
        fr: Fragment,
        conteinerDecisao: String
    ) {
        if (fa.visibility == View.GONE) {
            Log.d("tag 1 ", "layoutgone clicado")
            changeFragment(fr, conteinerDecisao)
            fa.slideDown()
            aSU.visibility = View.VISIBLE
            btnaSD.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_colortext_24dp)

        } else {
            fa.slideUp()
            aSU.visibility = View.GONE
            btnaSD.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_colortext_24dp)
        }
    }

    fun changeFragment(fr: Fragment, conteinerDecisao: String) {
        Log.d("tag 1 ", "fragment clicado")
        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        //var fr : Fragment = FragmentConteinerReciclarPapel()
        when (conteinerDecisao) {

            "aluminio" -> ft.replace(R.id.container_aluminio, fr).commit()

            "papel" -> ft.replace(R.id.container_papeis, fr).commit()

            "plastico" -> ft.replace(R.id.container_plastico, fr).commit()

            //"papelao"-> ft.replace(R.id.container_papelao, fr).commit()

            "vidro" -> ft.replace(R.id.container_vidro, fr).commit()

            "eletronico" -> ft.replace(R.id.container_eletronicos, fr).commit()

        }

    }

    override fun onResume() {
        super.onResume()
        //UpdateCoin().updateValues(activity!!)
    }
}

private fun FrameLayout.slideDown(duration: Int = 500) {

    visibility = FrameLayout.VISIBLE
    Log.d("visibility", visibility.toString())

}

private fun FrameLayout.slideUp(duration: Int = 500) {
    visibility = FrameLayout.GONE

}


