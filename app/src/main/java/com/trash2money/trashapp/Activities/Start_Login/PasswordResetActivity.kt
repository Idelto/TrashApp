package com.trash2money.trashapp.Activities.Start_Login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth

class PasswordResetActivity : AppCompatActivity() {

    lateinit var btnVoltarLogin: Button
    lateinit var btnResetSenha: Button
    lateinit var auth: FirebaseAuth
    lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        //inicializar()

        btnVoltarLogin = findViewById(R.id.btnVoltarLogin)
        btnResetSenha = findViewById(R.id.btnResetSenha)
        email = findViewById(R.id.edittxtEmail)

        auth = FirebaseAuth.getInstance()

        btnVoltarLogin.setOnClickListener(){

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnResetSenha.setOnClickListener(){
            val mEmail = email?.text.toString()

            if (!TextUtils.isEmpty(mEmail)){
                auth!!
                    .sendPasswordResetEmail(mEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val message = "Email Enviado"
                            Log.d("email enviado", message)
                            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            //updateUI()
                        }else{
                            Log.d("Email não econtrado", task.exception!!.message)
                            Toast.makeText(this, "Nenhum usuario encontrado, com esse e-mail.", Toast.LENGTH_SHORT).show()

                        }
                    }
            }else{

                Toast.makeText(this, "Entre com um e-mail.", Toast.LENGTH_SHORT).show()
            }


        }
    }

    /*private fun inicializar() {
        email = findViewById(R.id.txtEmail)
        btnResetSenha = findViewById(R.id.btnResetSenha)

        auth = FirebaseAuth.getInstance()

        btnResetSenha!!.setOnClickListener(){
            val mEmail = email?.text.toString()

            if (!TextUtils.isEmpty(mEmail)){
                auth!!
                    .sendPasswordResetEmail(mEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val message = "Email Enviado"
                            Log.d("email enviado", message)
                            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            updateUI()
                        }else{
                            Log.w("Email não econtrado", task.exception!!.message)
                            Toast.makeText(this, "Nenhum usuario encontrado, com esse e-mail.", Toast.LENGTH_SHORT).show()

                        }
                    }
            }else{

                Toast.makeText(this, "Entre com um e-mail.", Toast.LENGTH_SHORT).show()
            }


        }


    }*/

    private fun updateUI(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}