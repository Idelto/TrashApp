package com.trash2money.trashapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.KClasses.InfoIntro
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.dreierf.materialintroscreen.SlideFragment
import kotlinx.android.synthetic.main.fragment_termos_condicoes_uso.*

class FragmentTermosCondicoesUso : SlideFragment() {

    val auth = FirebaseAuth.getInstance() // define qual o usuário está autenticado
    val db = FirebaseFirestore.getInstance() // define caminho raiz do firebase
    val currentUser = auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View
        v = inflater.inflate(R.layout.fragment_termos_condicoes_uso, container, false)

        return v
    }

    override fun canMoveFurther(): Boolean {
        if (cbx_termosecondicoes.isChecked) {
            InfoIntro(activity!!).updateIntroStatus(true)
            val intent = Intent(activity, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity!!.finish()
        }

        return cbx_termosecondicoes.isChecked
    }

    override fun cantMoveFurtherErrorMessage(): String {

        return "Ainda não concordou com nossa Política de Privacidade e Termos de Uso"
    }

    override fun backgroundColor(): Int {

        return R.color.colorText
    }

    override fun buttonsColor(): Int {

        return R.color.quantum_black_secondary_text
    }
}