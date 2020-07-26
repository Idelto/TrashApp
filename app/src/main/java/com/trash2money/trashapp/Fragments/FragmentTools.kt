package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentTools : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var vCelular: EditText
    lateinit var vCep: EditText
    lateinit var vRua: EditText
    lateinit var vNumero: EditText
    lateinit var vComplemento: EditText
    lateinit var vCidade: EditText
    lateinit var vBairro: EditText
    lateinit var vAtualizaInformacoes: Button
    lateinit var vResetarSenha: Button

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View
        v = inflater.inflate(R.layout.fragment_tools, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(currentUser?.uid.toString())

        vAtualizaInformacoes = v.findViewById(R.id.btnAtualizaInformacoes)
        vResetarSenha = v.findViewById(R.id.btnReseteSuaSenha)
        vCelular = v.findViewById(R.id.txtCelular)
        vCep = v.findViewById(R.id.txtCep)
        vRua = v.findViewById(R.id.txtRua)
        vNumero = v.findViewById(R.id.txtNumero)
        vComplemento = v.findViewById(R.id.txtComplemento)
        vBairro = v.findViewById(R.id.txtBairro)
        vCidade = v.findViewById(R.id.txtCidade)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        vAtualizaInformacoes.setOnClickListener {

            val mCelular = vCelular.text.toString()
            val mCep = vCep.text.toString()
            val mRua = vRua.text.toString()
            val mNumero = vNumero.text.toString()
            val mComplemento = vComplemento.text.toString()
            val mBairro = vBairro.text.toString()
            val mCidade = vCidade.text.toString()
            docRef.update("celular", mCelular)
            docRef.update("cep", mCep)
            docRef.update("rua", mRua)
            docRef.update("numero", mNumero)
            docRef.update("complemento", mComplemento)
            docRef.update("bairro", mBairro)
            docRef.update("cidade", mCidade)

            val message = "Dados Atualizados"
            Log.d("dados Atualizados", message)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

            vCidade.setText("")
            vCelular.setText("")
            vCep.setText("")
            vRua.setText("")
            vNumero.setText("")
            vComplemento.setText("")
            vBairro.setText("")

        }

        vResetarSenha.setOnClickListener {

            val mEmail = currentUser?.email.toString()

            auth!!.sendPasswordResetEmail(mEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val message = "Email Enviado"
                    Log.d("email enviado", message)
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

                }
            }
        }

        return v
    }
}
