package com.trash2money.trashapp.Activities.Start_Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Activities.IntroTrash
import com.trash2money.trashapp.KClasses.Users
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId

class LoginActivity : AppCompatActivity() {

    lateinit var btnEntrar: Button
    lateinit var btnCadastrar: Button
    lateinit var btnResetarSenha: Button
    lateinit var auth : FirebaseAuth
    lateinit var email : EditText
    lateinit var senha : EditText
    //var firstlogin = "1"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnEntrar = findViewById(R.id.btnEntrar2)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        btnResetarSenha = findViewById(R.id.btnEsqueceuSenha2)
        email = findViewById(R.id.txtEmail2)
        senha = findViewById(R.id.txtPassword2)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()



        btnEntrar.setOnClickListener(){

            val mEmail = email.text.toString() // busca do valor escrito nos EditText
            val mSenha = senha.text.toString() // busca do valor escrito nos EditText



            if(mEmail.isNotEmpty() && mSenha.isNotEmpty()){

                // método de autenticação do firebase
                auth.signInWithEmailAndPassword(mEmail,mSenha).addOnSuccessListener { task->
                    if (auth.getCurrentUser()?.isEmailVerified()!!) {

                        val currentUserlog = auth.currentUser!!.uid

                        val db2 = FirebaseFirestore.getInstance().collection("users").document(currentUserlog)
                        db2.get().addOnSuccessListener{ doc ->
                            Log.d("fristLogin", currentUserlog)
                            val user =doc.toObject<Users>(Users::class.java!!)
                            val firstlogin = user!!.primeiroLogin
                            Log.d("fristLogin", firstlogin)

                            if ( firstlogin == ""){
                                Log.d("fristLogin", firstlogin)
                                val intent = Intent(this, IntroTrash::class.java)
                                startActivity(intent)

                                val currUser = auth.currentUser
                                Toast.makeText(baseContext,
                                    "Usuário Logado com Sucesso : " + currUser?.email,
                                    Toast.LENGTH_LONG).show()
                                db.collection("users").document(currentUserlog).update("primeiroLogin","logado")
                                Log.d("fristLogin", firstlogin)

                            }else{
                                val intent = Intent(this, HomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)

                                var currUser = auth.currentUser
                                /*Toast.makeText(baseContext,
                                    "Usuário Logado com Sucesso : " + currUser?.email,
                                    Toast.LENGTH_LONG).show()*/
                            }

                            }

                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { task->

                            val token = task.token
                            //val token = FirebaseInstanceId.getInstance().getToken()
                            val currUser = FirebaseAuth.getInstance().currentUser!!.uid
                            val obj = hashMapOf(
                                "notification_token" to token,
                                "name" to currUser)
                            FirebaseFirestore.getInstance()
                                .collection("user_activity")
                                .document(currUser).update("notification_token", token)
                            //Toast.makeText(baseContext, token, Toast.LENGTH_LONG).show()
                        }

                    }else{

                        Toast.makeText(baseContext,"Favor verificar o e-mail",Toast.LENGTH_LONG).show()
                    }
                }
                        .addOnFailureListener { task->
                        Toast.makeText(baseContext,"Falha no Login ", Toast.LENGTH_LONG).show()
                    }



            }else{
                Toast.makeText(baseContext,"Por favor Digite o e-mail e a Senha",Toast.LENGTH_LONG).show()
            }

        }

        btnCadastrar.setOnClickListener(){

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }

        btnResetarSenha.setOnClickListener(){

            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)

        }



    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        Log.d("Login Auth", auth.currentUser.toString())

    }
}
