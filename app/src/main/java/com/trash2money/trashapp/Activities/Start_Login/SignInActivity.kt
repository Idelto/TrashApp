package com.trash2money.trashapp.Activities.Start_Login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.trash2money.trashapp.R
import com.trash2money.trashapp.KClasses.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    lateinit var btnCadastro : Button
    lateinit var auth : FirebaseAuth
    lateinit var email : EditText
    lateinit var senha : EditText
    lateinit var name : EditText
    lateinit var termos : CheckBox
    lateinit var politicaDePrivacidade : TextView
    lateinit var termosDeUso : TextView


    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)



        btnCadastro = findViewById(R.id.btnCadastro)

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progressbar,null)
        val mBuilder = android.app.AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Atualize seus dados")



        email = findViewById(R.id.txtEmail2)
        senha = findViewById(R.id.txtPassword2)
        name = findViewById(R.id.txtName)

        auth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance() // acessando o banco de dados

        btnCadastro.setOnClickListener() {
            val mAlertDialog = mBuilder.show()

            val mEmail = email.text.toString()
            val mSenha = senha.text.toString()
            val mName = name.text.toString()
            val mCelular = ""
            var mCep = ""
            var mRua = ""
            var mNumero = ""
            var mComplemento = ""
            var mBairro = ""
            var mCidade = ""
            val mFotoPerfil = ""
            val mPrimeiroLogin = ""
            val mUriFoto = ""


            if (mEmail.isNotEmpty() && mSenha.isNotEmpty()){

                    Log.d("Teste", mEmail + mSenha)

                    // método de criação de usuário do google Auth
                    auth.createUserWithEmailAndPassword(mEmail, mSenha)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                auth.getCurrentUser()?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("Autenticação e-mail",
                                                "Email de autenticação enviado.")
                                            val currUser = auth.currentUser
                                            Log.d("usuario",
                                                "Usero: " + "Email : " + mEmail + "Senha: " + mSenha)
                                            Log.d("Tarefa bem sucedida",
                                                "Usuário cadastrado com sucesso: " + "Email : " + mEmail + "User ID: ")


                                            // acessando o banco de dados e a coleção usuários
                                            val userCollection = db.collection("users")

                                            // Criando o objeto User com base na classe pública Users
                                            val newUser = Users(
                                                mName,
                                                currUser?.uid.toString(),
                                                currUser?.email.toString(),
                                                mCelular,
                                                mFotoPerfil,
                                                mPrimeiroLogin,
                                                mUriFoto
                                            )

                                            // Método de criaçaõ do usuário no firebase
                                            userCollection.document(currUser?.uid.toString())
                                                .set(newUser).addOnSuccessListener {
                                                    Log.d("Adicionando Usuário ",
                                                        "Usuário adicionado com sucesso ${currUser?.uid}")
                                                            mAlertDialog.dismiss()
                                                    try {
                                                        val intent = Intent(baseContext, AvisoEmail::class.java)
                                                        startActivity(intent)
                                                    }catch (e: Exception){
                                                        Log.e("erro tela AvisoEmail", e.message)
                                                    }

                                                }.addOnFailureListener { e ->
                                                    Log.d("Adicionando Usuário ",
                                                        "Usuário adicionado com sucesso ${e.message}")
                                                }

                                            val ref = db.collection("user_activity")
                                            val time = System.currentTimeMillis()
                                            val obj = hashMapOf("name" to currUser!!.uid, "n_item" to 0 , "n_recy" to 0, "peso" to 0 , "tcoins" to 0)
                                            val trashcoins = hashMapOf("date" to time, "value" to 3)
                                            ref.document(currUser!!.uid).set(obj).addOnSuccessListener {
                                                Log.d("user_activity" , "Adicionada ao user_activity")
                                            }
                                            ref.document(currUser!!.uid).collection("trashcoins").document(time.toString()).set(trashcoins)
                                        }
                                    }


                            } else {
                                //progressBar.visibility = View.INVISIBLE
                                mAlertDialog.dismiss()
                                Log.e("Falha na execução", "Falha no usuário" + task.exception)
                                Toast.makeText(baseContext,
                                    "Autentication Failed: ",
                                    Toast.LENGTH_LONG).show()
                            }
                        }

            }else{
                mAlertDialog.dismiss()
                //progressBar.visibility = View.INVISIBLE
                Toast.makeText(baseContext,"Por favor digite o Email e Senha", Toast.LENGTH_LONG).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        Log.d("Login Auth", auth.currentUser.toString())
    }

}



