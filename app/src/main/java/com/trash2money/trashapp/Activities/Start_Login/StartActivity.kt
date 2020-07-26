package com.trash2money.trashapp.Activities.Start_Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var btnCadastro : Button

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btnLogin = findViewById(R.id.btnLogin)
        btnCadastro = findViewById(R.id.btnCadastro)

        btnLogin.setOnClickListener(){

            auth = FirebaseAuth.getInstance() // Instacia o autenticador, para verificar se não há nenhum usuário logado

            val currentUser = auth.currentUser // busca se o usuário está logado.

            Log.d("TAG CURR USER", currentUser?.displayName.toString())

            if (currentUser != null){

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

        }
    }

        btnCadastro.setOnClickListener(){

            val intent = Intent(this, SignInActivity::class.java)
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
