package com.trash2money.trashapp.Activities.Start_Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.trash2money.trashapp.KClasses.Populate
import com.trash2money.trashapp.R

class CadastroProduto : AppCompatActivity() {

    lateinit var btncadastrar : Button
    lateinit var nome : EditText
    lateinit var gperUnity : EditText
    lateinit var material : EditText
    lateinit var shortDesc : EditText
    lateinit var longDesc : EditText
    lateinit var finalUnity : EditText
    lateinit var unity : EditText
    lateinit var nomeComp : EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produto)

        btncadastrar = findViewById(R.id.btn_cadastrar)
        nome = findViewById(R.id.txt_cadastro_nome)
        shortDesc = findViewById(R.id.txt_cadastro_des_curta)
        longDesc = findViewById(R.id.txt_cadastro_desc_longa)
        nomeComp = findViewById(R.id.txt_cadastro_nome_componente)
        gperUnity = findViewById(R.id.txt_cadastro_peso)
        unity = findViewById(R.id.txt_cadastro_unity)
        material = findViewById(R.id.txt_cadastro_material)

        btncadastrar.setOnClickListener {
            if(nome.text.isNullOrEmpty() || shortDesc.text.isNullOrEmpty() || longDesc.text.isNullOrEmpty() || nomeComp.text.isNullOrEmpty() || gperUnity.text.isNullOrEmpty() || material.text.isNullOrEmpty() || unity.text.isNullOrEmpty()){
                Toast.makeText(this,"Está faltando preenchimento em algum dos campos", Toast.LENGTH_LONG).show()
            }

            val fu = "g/" + unity.text.toString()
            Populate().poupulate(nomeComp.text.toString(),gperUnity.text.toString().toDouble(),longDesc.text.toString(),material.text.toString(),shortDesc.text.toString(),unity.text.toString(),fu,"material_desc")
            Toast.makeText(this,"Produto Cadastrado", Toast.LENGTH_LONG).show()

            nome.text.clear()
            shortDesc.text.clear()
            longDesc.text.clear()
            nomeComp.text.clear()
            gperUnity.text.clear()
            unity.text.clear()
            material.text.clear()

        }

    }
}