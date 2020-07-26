package com.trash2money.trashapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
import com.trash2money.trashapp.KClasses.Location
import com.trash2money.trashapp.KClasses.Trash
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ScheduleActivity : AppCompatActivity() {

    lateinit var btnMorning: ImageButton
    lateinit var btnAfternoon: ImageButton
    lateinit var btnNight: ImageButton
    lateinit var btnMorningDark: ImageButton
    lateinit var btnAfternoonDark: ImageButton
    lateinit var btnNightDark: ImageButton
    lateinit var btnSchedule: Button
    lateinit var calendarView: CalendarView
    lateinit var time: String

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // definição dos botões de períodos
        btnMorning = findViewById(R.id.fabManha)
        btnAfternoon = findViewById(R.id.fabTarde)
        btnNight = findViewById(R.id.fabNoite)
        btnMorningDark = findViewById(R.id.fabManha_dark)
        btnAfternoonDark = findViewById(R.id.fabTarde_dark)
        btnNightDark = findViewById(R.id.fabNoite_dark)
        btnSchedule = findViewById(R.id.btnSchedule)
        calendarView = findViewById(R.id.calendarView)
        time = "Manhã"

        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        btnMorning.setOnClickListener() {

            time = "Manhã"
            btnMorning.visibility = View.INVISIBLE
            btnAfternoon.visibility = View.VISIBLE
            btnNight.visibility = View.VISIBLE
            btnMorningDark.visibility = View.VISIBLE
            btnAfternoonDark.visibility = View.INVISIBLE
            btnNightDark.visibility = View.INVISIBLE

        }
        btnAfternoon.setOnClickListener() {

            time = "Tarde"
            btnMorning.visibility = View.VISIBLE
            btnAfternoon.visibility = View.INVISIBLE
            btnNight.visibility = View.VISIBLE
            btnMorningDark.visibility = View.INVISIBLE
            btnAfternoonDark.visibility = View.VISIBLE
            btnNightDark.visibility = View.INVISIBLE

        }
        btnNight.setOnClickListener() {

            time = "Noite"
            btnMorning.visibility = View.VISIBLE
            btnAfternoon.visibility = View.VISIBLE
            btnNight.visibility = View.INVISIBLE
            btnMorningDark.visibility = View.INVISIBLE
            btnAfternoonDark.visibility = View.INVISIBLE
            btnNightDark.visibility = View.VISIBLE

        }

        // Definição do Calendar View
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            //definição da data
            val calendar = GregorianCalendar(year, month, dayOfMonth)

            //data e transformei para timeInMills
            val date = calendar.timeInMillis

            btnSchedule.setOnClickListener {

                //Receber os dados do usuário autenticado.
                val auth = FirebaseAuth.getInstance()
                val currUser = auth.currentUser

                // Busquei as informações da Intent
                val street = intent.getStringExtra("street")
                val zip = intent.getStringExtra("zip")
                val neighborood = intent.getStringExtra("neighborood")
                val city = intent.getStringExtra("city")
                val state = intent.getStringExtra("state")
                val featureName = intent.getStringExtra("featureName")
                val complement = intent.getStringExtra("complement")
                val refPoint = intent.getStringExtra("refPoint")
                val lat = intent.getStringExtra("lat")
                val lng = intent.getStringExtra("lng")
                val st = intent.getStringExtra("st")
                val path = intent.getStringExtra("path")

                //Criação do objeto Location
                val location = Location(street,
                    zip,
                    neighborood,
                    city,
                    state,
                    featureName,
                    complement,
                    refPoint,
                    lat.toDouble(),
                    lng.toDouble())

                //Adicionar o documento no Firebase
                val db = FirebaseFirestore.getInstance()

                Log.d("st", st)

                if (st.equals("0")) {

                    db.document(path).update("status", 1)

                    val toast =
                        Toast.makeText(this, "Agendamento realizado com Sucesso", Toast.LENGTH_LONG)

                    //Criação do Toast no centro da tela
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                    val intent = Intent(baseContext, HomeActivity::class.java)
                    startActivity(intent)

                } else {

                    db.collection("user_activity").document(currUser?.uid.toString())
                        .set(hashMapOf("name" to currUser?.uid))

                    //Definir o local de armazenamento
                    val storagePath =
                        db.collection("user_activity").document(currUser?.uid.toString())
                            .collection("trashcans").document(System.currentTimeMillis().toString())

                    //Adicionando a Localização
                    storagePath.collection("trash_location").add(location)
                        .addOnSuccessListener { added ->
                            Log.d("TAG", "Documento Adicionado com sucesso")
                        }

                    storagePath.set(hashMapOf("recycler" to currUser?.uid))

                    //Buscar o TrashItem que será Armazenado
                    val trashItemRef =
                        db.collection("points_trash_can").document(currUser?.uid.toString())

                    //Adicionando os resíduos na atividade do usuário
                    var totalTCoins = 0F
                    var totalQty = 0F
                    trashItemRef.collection("trash_item").get().addOnSuccessListener { trashs ->

                        for (trash in trashs) {

                            val obj = trash.toObject<Trash>(Trash::class.java)

                            totalTCoins += obj.trashcoins
                            totalQty += obj.quantity

                            Log.d("OBJ", obj.toString())
                            storagePath.collection("trash_item").add(obj!!)

                        }

                        storagePath.update("tCoins", totalTCoins)
                        storagePath.update("qty", totalQty)
                    }

                    //Alterando o status da atividade do usuário
                    storagePath.update(mapOf("status" to 1, "scheduleDate" to date, "time" to time))
                        .addOnSuccessListener {
                            Log.d("TAG ALTERANDO STATUS", "STATUS ALTERADO COM SUCESSO")
                        }.addOnFailureListener { e ->
                            Log.e("ERRO:", e.message)
                        }

                    trashItemRef.collection("trash_location").add(location).addOnSuccessListener {
                        Log.d("TAG LOC CADASTRADA", "LOCALIZAÇÃO CADASTRADA")
                    }

                    trashItemRef.collection("trash_item").get().addOnSuccessListener { query ->

                        for (doc in query) {
                            trashItemRef.collection("trash_item").document(doc.id).delete()
                            Log.d("DOC DELETADO", "DOCUMENTO ${doc.id.toString()} DELETADO")
                        }
                    }

                    val toast =
                        Toast.makeText(this, "Agendamento realizado com Sucesso", Toast.LENGTH_LONG)

                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()

                    val intent = Intent(baseContext, HomeActivity::class.java)
                    startActivity(intent)

                }
            }
        }

    }

}
