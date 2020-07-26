package com.trash2money.trashapp.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.KClasses.GetCurrentUserData
import com.trash2money.trashapp.KClasses.ProgressBarDialog
import com.trash2money.trashapp.KClasses.Users
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.atualiza_dialog.view.*
import java.util.*

class FragmentProfile : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var nomePerfil: TextView
    lateinit var emailPerfil: EditText
    lateinit var celularPerfil: EditText
    lateinit var cepPerfil: EditText
    lateinit var ruaPerfil: EditText
    lateinit var numeroPerfil: EditText
    lateinit var complementoPerfil: EditText
    lateinit var bairroPerfil: EditText
    lateinit var cidadePerfil: EditText
    lateinit var btnFoto: Button
    lateinit var btnAtualizaInformacoes: Button
    lateinit var mSelecteduri: Uri
    lateinit var imgFoto: ImageView
    lateinit var fabTrocarFoto: View
    lateinit var imagePerfil: Uri

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        val dialog = ProgressBarDialog(context!!)

        nomePerfil = v.findViewById(R.id.editTextName)
        emailPerfil = v.findViewById(R.id.editTextEmailPerf)
        celularPerfil = v.findViewById(R.id.editTextCelularPerf)
        cepPerfil = v.findViewById(R.id.editTextCEPPerf)
        ruaPerfil = v.findViewById(R.id.editTextRuaPerf)
        numeroPerfil = v.findViewById(R.id.editTextNumeroPerf)
        complementoPerfil = v.findViewById(R.id.editTextComplementoPerf)
        bairroPerfil = v.findViewById(R.id.editTextBairroPerf)
        cidadePerfil = v.findViewById(R.id.editTextCidadePerf)
        imgFoto = v.findViewById(R.id.imgFoto)
        btnAtualizaInformacoes = v.findViewById(R.id.btnAtualizar_Informacoes)
        fabTrocarFoto = v.findViewById(R.id.fabTrocarFoto)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        dialog.openDialog()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(currentUser?.uid.toString())

        fabTrocarFoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, 42)
        }

        fun carregaFoto() {

        }

        var loggedName = "" as String
        var loggedEmail = "" as String
        var loggedCelular = "" as String
        val loggedCep = "" as String
        val loggedRua = "" as String
        val loggedNumero = "" as String
        val loggedComplemento = "" as String
        val loggedBairro = "" as String
        val loggedCidade = "" as String
        var loggedFoto = "" as String
        var uriFoto = "" as String

        GetCurrentUserData().readDataUser(object : GetCurrentUserData.MyCallbackUser {
            override fun onCallBack(user: Users) {
                loggedName = user.name
                loggedEmail = user.email
                loggedCelular = user.celular
                loggedFoto = user.fotoPerfil
                uriFoto = user.urifoto

                nomePerfil.setText(" " + loggedName)
                nomePerfil.isEnabled = false
                emailPerfil.setText(" " + loggedEmail)
                emailPerfil.isEnabled = false
                celularPerfil.setText(" " + loggedCelular)
                celularPerfil.isEnabled = false
                cepPerfil.setText(" " + loggedCep)
                cepPerfil.isEnabled = false
                ruaPerfil.setText(" " + loggedRua)
                ruaPerfil.isEnabled = false
                numeroPerfil.setText(" " + loggedNumero)
                numeroPerfil.isEnabled = false
                complementoPerfil.setText(" " + loggedComplemento)
                complementoPerfil.isEnabled = false
                bairroPerfil.setText(" " + loggedBairro)
                bairroPerfil.isEnabled = false
                cidadePerfil.setText(" " + loggedCidade)
                cidadePerfil.isEnabled = false
                val uri = user.fotoPerfil
                if (uri != "") {
                    Picasso.get().load(uri).into(imgFoto)
                } else {

                }

                dialog.closeDialog()

            }
        })


        btnAtualizaInformacoes.setOnClickListener {

            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.atualiza_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView).setTitle("Atualize seus dados")
            val mAlertDialog = mBuilder.show()

            mDialogView.btnAtualizaDados.setOnClickListener {

                val aCelular = mDialogView.txtCelular_Dialog.text.toString()
                val aCep = mDialogView.txtCep_Dialog.text.toString()
                val aRua = mDialogView.txtRua_Dialog.text.toString()
                val aNumero = mDialogView.txtNumero_Dialog.text.toString()
                val aComplemento = mDialogView.txtComplemento_Dialog.text.toString()
                val aBairro = mDialogView.txtBairro_Dialog.text.toString()
                val aCidade = mDialogView.txtCidade_Dialog.text.toString()

                if (aCelular != "") {
                    docRef.update("celular", aCelular)
                    celularPerfil.setText(" " + aCelular)
                }

                if (aCep != "") {
                    docRef.update("cep", aCep)
                    cepPerfil.setText(" " + aCep)
                }

                if (aRua != "") {
                    docRef.update("rua", aRua)
                    ruaPerfil.setText(" " + aRua)
                }

                if (aNumero != "") {
                    docRef.update("numero", aNumero)
                    numeroPerfil.setText(" " + aNumero)
                }

                if (aComplemento != "") {
                    docRef.update("complemento", aComplemento)
                    complementoPerfil.setText(" " + aComplemento)
                }

                if (aBairro != "") {
                    docRef.update("bairro", aBairro)
                    bairroPerfil.setText(" " + aBairro)
                }

                if (aCidade != "") {
                    docRef.update("cidade", aCidade)
                    cidadePerfil.setText(" " + aCidade)
                }

                docRef.update("primeiroLogin", "logado")

                mAlertDialog.dismiss()

            }

            mDialogView.btnNaoAtualiza.setOnClickListener {
                mAlertDialog.dismiss()

            }

        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 42) {

            mSelecteduri = data?.data!!
            imgFoto.setImageURI(mSelecteduri)
            uploadFoto()

        }
    }

    private fun uploadFoto() {
        if (mSelecteduri == null) return

        val fileNmae = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image_users/$fileNmae")

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(currentUser?.uid.toString())

        docRef.update("uriFoto", fileNmae)

        ref.putFile(mSelecteduri!!).addOnSuccessListener {
                Log.d("RegisterActivity", "file location: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully upload image : $it")
                    val fotoPerfil = it.toString()

                    auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(currentUser?.uid.toString())

                    docRef.update("fotoPerfil", fotoPerfil)

                }
            }


    }


}
