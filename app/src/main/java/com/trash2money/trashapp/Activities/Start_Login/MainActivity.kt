package com.trash2money.trashapp.Activities.Start_Login

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.KClasses.PushNotification
import com.trash2money.trashapp.KClasses.RetrofitInstance
import com.trash2money.trashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class
MainActivity : AppCompatActivity() {

    lateinit var homeScreen: ImageView
    lateinit var auth: FirebaseAuth //busca no firebase para saber qual o usuário está logado na plataforma
    val TAG = "Permssion"
    private val REQUEST_COARSE_LOCATION_CODE = 1
    lateinit var manufacter : String
    lateinit var model : String
    var celularversao : Int = 0
    lateinit var celularversionrelease : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        FirebaseFirestore.getInstance()
        Log.d("Analytics",FirebaseFirestore.getInstance().toString())
        Log.d("Analytics", mFirebaseAnalytics.firebaseInstanceId.toString())

        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        Log.d("Analytics", mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle).toString() )
        //tst
        //setPermission() // Chamar as permissões de acesso ao App, como por exemplo a permissão de câmera, localização.

        homeScreen = findViewById(R.id.imageView)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser // buscar o usuário logado no firebase
        Log.d("TAG CURR USER", currentUser?.displayName.toString())
        // dadosCelular()
        // tempo para entrar na primeira tela do App.
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            /*if (currentUser != null){


                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            }else{

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }*/

            setLogin()


            // close this activity
            finish()
        }, 3000)



        // clicar para entrar na primeira tela do App.

        /*homeScreen.setOnClickListener(){
            if (currentUser != null){


                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            }else{

                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)

            }

        }*/

    }

    /*private fun dadosCelular() {
        try {
            val auth = FirebaseAuth.getInstance()
            val currUser = auth.currentUser
            val db = FirebaseFirestore.getInstance().collection("sys_log")
            var obj = hashMapOf("id" to currUser!!.uid)
            db.document(currUser!!.uid).set(obj)
            val ref = db.document(currUser!!.uid).collection(System.currentTimeMillis().toString())

            if (Build.MANUFACTURER != null){
                manufacter = Build.MANUFACTURER
            }else{
                manufacter = "Não Encontrado"
            }
            if (Build.MODEL != null){
                 model= Build.MODEL
            }
            if (Build.VERSION.SDK_INT != null){
                celularversao = Build.VERSION.SDK_INT
            }else{
                celularversao = 0
            }
            if (Build.VERSION.RELEASE != null){
                celularversionrelease = Build.VERSION.RELEASE
            }else{
                celularversionrelease = "Não Encontrado"
            }


            var dadosCelular = hashMapOf("empresa" to manufacter,
                "modelo" to model,
                "sdk" to celularversao,
                "version_release" to celularversionrelease)
            ref.document().set(dadosCelular)
        }finally {
            Log.d("tag1", "Não foi possivel acessar os dados")
        }
    }*/

    fun setLogin() {

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


    // Permissões de uso do App
    fun setPermission(){

        val permission  = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission denied" )
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){

            val builder = AlertDialog.Builder(this)

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_permission,null)
            builder.setCancelable(false)
            builder.setView(dialogView)

            val btnPositive = dialogView.findViewById<Button>(R.id.btnDialogMapYes)
            val dialog = builder.create()

            btnPositive.setOnClickListener(){
                Log.d(TAG, "Clicked")
                makeRequest()
                dialog.cancel()
            }

            dialog.show()

        }

        else{
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_COARSE_LOCATION_CODE)

    }

    private fun sendNotification(notification:PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG,"Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG, response.errorBody().toString())
            }

        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }

    }




    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray){
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_COARSE_LOCATION_CODE -> {
                if (grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "Permission has been denied by user")
                }
                else{
                    Log.d(TAG, "Permission has been granted by user")
                }
            }
        }
    }
}

