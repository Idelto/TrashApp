package com.trash2money.trashapp.Activities.Start_Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.trash2money.trashapp.R

class AvisoEmail : AppCompatActivity() {

    lateinit var btnAviso : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aviso_email)

        btnAviso = findViewById(R.id.btnvoltarlogin)

        btnAviso.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


    }
}