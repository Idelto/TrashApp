package com.trash2money.trashapp.Activities.Maps


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
import com.trash2money.trashapp.KClasses.*
import com.trash2money.trashapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TOPIC = "/topics/availableTrash"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var TAG: String = "MainActivity"
    val ERROR_DIALOG_REQUEST: Int = 9001
    val DEFAULT_ZOOM = 15F
    lateinit var myMap: GoogleMap
    lateinit var txtSearch: EditText
    lateinit var imgGps: ImageView
    lateinit var mClient: FusedLocationProviderClient
    lateinit var btnReady: Button
    lateinit var txtSearch2: EditText
    lateinit var neighbord: String
    lateinit var thoroughfare: String
    private val REQUEST_COARSE_LOCATION_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        isServicesOk()

        //trazer o objeto support mapfragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        txtSearch = findViewById(R.id.txtSearch)
        txtSearch2 = findViewById(R.id.txtSearch2)
        imgGps = findViewById(R.id.imgGps)
        btnReady = findViewById<Button>(R.id.btnReady)

        btnReady.isEnabled = false

        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

    }

    // função que inicia o mapa
    private fun init(myMap: GoogleMap) {

        Log.d(TAG, "iniciando")

        txtSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                geoLocate(myMap)
            }
            false
        }
    }

    //Traz a sua localização atual através do gps do celular
    fun geoLocateLatLng(myMap: GoogleMap, lat: Double, long: Double): Address {

        val geoCoder = Geocoder(this@MapsActivity)
        var list = ArrayList<Address>()
        var address: Address

        address = Address(null)

        try {
            list = geoCoder.getFromLocation(lat, long, 1) as ArrayList<Address>

        } catch (e: IOException) {
            Log.e(TAG, "GeoLocate: IO Exception" + e.message)
        }

        if (list.size > 0) {
            address = list[0]
            Log.d(TAG, "GeoLocation: Localização encontrada : " + address.toString())
        }
        return address
    }

    fun geoLocate(myMap: GoogleMap) {

        val searchString: String
        val geoCoder = Geocoder(this@MapsActivity)
        var list = ArrayList<Address>()

        searchString = txtSearch.text.toString()

        try {
            list = geoCoder.getFromLocationName(searchString, 1) as ArrayList<Address>

        } catch (e: IOException) {
            Log.e(TAG, "GeoLocate: IO Exception" + e.message)
        }

        if (list.size > 0) {

            val address: Address
            address = list[0]

            Log.d(TAG, "GeoLocation: Localização encontrada : " + address.toString())

            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude,
                address.longitude), DEFAULT_ZOOM))
            myMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude))
                .title(address.getAddressLine(0)))

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        SetMapPermission().setPermission(this, this@MapsActivity, MapsActivity())
        val myMap = googleMap

        Log.d(TAG, "getDeviceLocation: getting the devices current Location")

        //Informações do meu dispositivo
        mClient = LocationServices.getFusedLocationProviderClient(this)

        try {

            val location = mClient.lastLocation as Task

            //mClient para uma localização no mapa
            location.addOnCompleteListener(OnCompleteListener {
                if (location.isSuccessful) {
                    Log.d(TAG, "On Complete: Localização Encontrada")

                    // variável localização atual = informações do dispositivo
                    val currentLocation = location.result
                    if (currentLocation != null) {
                        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                        val zoom = 15f

                        Log.d(TAG,
                            "Move Camera to : lat: " + latLng.latitude + ", long: " + latLng.longitude)

                        // Método de mover a camera.
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
                        //myMap.addMarker(MarkerOptions().position(latLng).title("Matheus"))
                    }
                } else {
                    //Caso não seja possível encontrar a localização do celular
                    Log.d(TAG, "On Complete: Localização é NULL")
                    Toast.makeText(this@MapsActivity,
                        "Não foi possível encontrar a localização",
                        Toast.LENGTH_LONG).show()
                }

            })

        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: Security Exception" + e.message)
        }

        // Mostrando para o Maps que a sua localização está válida
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        myMap.isMyLocationEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = false

        // inicio no Mapa que foi definido nos passo acima
        init(myMap)

        //Botão que devolve você para a sua origem
        imgGps.setOnClickListener() {
            getDeviceLocation(myMap)
        }

        // Chave da API -  Chave de conexão com o Maps Console
        val apiKey = "AIzaSyALz7_FTU5c2Vu87huK0wX40aLzTR9Ybew"

        //GetUserData
        getUserAddress(myMap)

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)

        // Create a new Places client instance

        val autoCompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
            com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))

        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: com.google.android.libraries.places.api.model.Place) {
                /*Toast.makeText(this@MapsActivity,
                    "Place: " + place.name.toString() + " ID :" + place.latLng.toString(),
                    Toast.LENGTH_LONG).show()*/

                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 15f))
                myMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name))

                btnReady.isEnabled = true

            }

            override fun onError(e: Status) {
                Toast.makeText(this@MapsActivity,
                    "Oopes deu erro: " + e.statusMessage,
                    Toast.LENGTH_LONG).show()
            }

        })

        btnReady.setOnClickListener() {

            init(myMap)

            // Transformando as informações do mapa em um objeto
            val address = geoLocateLatLng(myMap,
                myMap.cameraPosition.target.latitude,
                myMap.cameraPosition.target.longitude)
            if (address == null) {
                Toast.makeText(this@MapsActivity,
                    "O endereço ainda não foi carregado",
                    Toast.LENGTH_SHORT).show()
            } else {
                Log.d("adminArea", address.adminArea)
                //Log.d("thoroughfare" , address?.thoroughfare)
                Log.d("postalCode", address.postalCode)
                Log.d("subAdminArea", address.subAdminArea)
                //Log.d("subLocality" , address?.subLocality)
                Log.d("ADDRESS", txtSearch2.text.toString())
                Log.d("ADDRESS", txtSearch.text.toString())
                Log.d("latitude", address.latitude.toString())
                Log.d("longitude", address.longitude.toString())

                var neighbord2 = ""

                if (address.thoroughfare == null) {
                    thoroughfare = ""
                } else {
                    thoroughfare = address.thoroughfare
                }

                if (address.subLocality == null) {
                    neighbord = ""
                    neighbord2 = "null"
                } else {
                    neighbord = address.subLocality
                    neighbord2 = address.subLocality
                }

                val path = "/pontos_coleta/disponiveis/estados/${address.adminArea}/cidades/${address.subAdminArea}/bairros/${neighbord2}/ceps/${address.postalCode}"

                FirebaseFirestore.getInstance().document(path).get().addOnSuccessListener {doc->

                    val cep = doc["cep"]?.toString()
                    if(cep != null){Log.d("CEP", cep)}
                    Log.d("CEP", address.postalCode)

                    if(cep == address.postalCode){
                        Toast.makeText(baseContext, "Encontrado", Toast.LENGTH_LONG).show()
                        //Criação do objeto localização com base nos dados do Marker
                        val db = FirebaseFirestore.getInstance()
                        val auth = FirebaseAuth.getInstance()
                        val currUser = auth.currentUser
                        val docRef = db.collection("points_trash_can").document(currUser?.uid.toString())
                        //val copyRef = db.collection("user_activity").document(currUser?.uid.toString())
                        docRef.collection("trash_item").get().addOnSuccessListener { doc ->
                            if (doc.size() != 0) {

                                var complement = ""
                                var refPoint = ""

                                if (txtSearch.text == null) {
                                    complement = ""
                                } else {
                                    complement = txtSearch.text.toString()
                                }

                                if (txtSearch2.text == null) {
                                    refPoint = ""
                                } else {
                                    refPoint = txtSearch2.text.toString()
                                }

                                val street = thoroughfare
                                val zip = address?.postalCode.toString()
                                val neighborood = neighbord
                                val city = address?.subAdminArea
                                val state = address?.adminArea
                                val featureName = address?.featureName
                                complement = txtSearch?.text.toString()
                                refPoint = txtSearch2?.text.toString()
                                val lat = address?.latitude.toString()
                                val lng = address?.longitude.toString()
                                val st = "1"

                                scheduleretirada(
                                    street,
                                    zip,
                                    neighborood,
                                    city,
                                    state,
                                    featureName,
                                    complement,
                                    refPoint,
                                    lat,
                                    lng,
                                    st
                                )

                                Log.d("Date", city)
                                Log.d("Date", neighborood)

                                val sdate = FirebaseFirestore.getInstance().collection("coletas")
                                sdate.get().addOnSuccessListener { cidades ->
                                    var existe = false
                                    for (cidade in cidades) {
                                        Log.d("Tag_infiniteFor", cidade.id)
                                        Log.d("Tag_infiniteFor", city)
                                        if (cidade.id == city) {
                                            sdate.document(cidade.id).collection("bairros").get()
                                                .addOnSuccessListener { bairros ->
                                                    for (bairro in bairros) {
                                                        Log.d("Tag_infiniteFor1", bairro.id)
                                                        Log.d("Tag_infiniteFor2", neighborood)
                                                        if (bairro.id == neighborood) {
                                                            sdate.document(cidade.id)
                                                                .collection("bairros")
                                                                .document(bairro.id)
                                                                .collection("coletas").get()
                                                                .addOnSuccessListener { coletas ->
                                                                    val coleta = coletas.last()
                                                                    Log.d(
                                                                        "Tag_infiniteFor", coleta.id
                                                                    )
                                                                    val dia = coleta["data"]
                                                                    val periodo = coleta["periodo"]
                                                                    Log.d(
                                                                        "taginfiniteFor",
                                                                        "cheguei aqui !!!"
                                                                    )


                                                                    val builder2 =
                                                                        AlertDialog.Builder(this@MapsActivity)
                                                                    val dialogView2 =
                                                                        LayoutInflater.from(this@MapsActivity)
                                                                            .inflate(
                                                                                R.layout.dialog_scheduled_date,
                                                                                null
                                                                            )
                                                                    builder2.setCancelable(false)
                                                                    builder2.setView(dialogView2)


                                                                    val btnYes =
                                                                        dialogView2.findViewById<Button>(
                                                                            R.id.btn_Yes
                                                                        )
                                                                    val txtschedule =
                                                                        dialogView2.findViewById<TextView>(
                                                                            R.id.txtDialogscheTitle
                                                                        )

                                                                    val scheduleDate = Date(
                                                                        dia.toString().toLong()
                                                                    )

                                                                    val dateFormat =
                                                                        SimpleDateFormat("dd-MMM-yyyy")
                                                                    val date = dateFormat.format(
                                                                        scheduleDate
                                                                    )
                                                                    Log.d("Date", date.toString())

                                                                    txtschedule.text =
                                                                        "Oba 😍! Seu resíduo está disponível e será coletado no dia " + date + " no periodo da " + periodo + " ,acompanhe o status por minhas retiradas no menu lateral."

                                                                    val dialogschedule =
                                                                        builder2.create() as AlertDialog

                                                                    btnYes.setOnClickListener {

                                                                        //Toast.makeText(baseContext, "Abrir Google Form", Toast.LENGTH_LONG).show()
                                                                        val intent = Intent(
                                                                            this.baseContext,
                                                                            HomeActivity::class.java
                                                                        )
                                                                        startActivity(intent)
                                                                        dialogschedule.cancel()

                                                                    }

                                                                    dialogschedule.show()

                                                                }
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }

                            } else {

                                Toast.makeText(this,
                                        "Você não possui nenhum ítem na sua lixeira para reciclar",
                                        Toast.LENGTH_LONG).show()
                            }
                        }

                    }else{

                        Toast.makeText(baseContext, "Ainda não estamos no seu endereço", Toast.LENGTH_LONG).show()

                        val builder = AlertDialog.Builder(this@MapsActivity)
                        val dialogView = LayoutInflater.from(this@MapsActivity).inflate(R.layout.dialog_new_region, null)
                        builder.setCancelable(false)
                        builder.setView(dialogView)

                        val btnYes = dialogView.findViewById<Button>(R.id.btn_Yes)
                        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

                        val dialog = builder.create() as AlertDialog

                        btnYes.setOnClickListener {

                            //Toast.makeText(baseContext, "Abrir Google Form", Toast.LENGTH_LONG).show()
                            val openURL = Intent(android.content.Intent.ACTION_VIEW)
                            openURL.data = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdbni68oE4_ACw8ryh2r1W-MvNpJg21aJ8rESwLFXj14hw41A/viewform?vc=0&c=0&w=1")
                            startActivity(openURL)
                            dialog.cancel()

                        }

                        btnNo.setOnClickListener {

                            Toast.makeText(baseContext, "Não pare de Reciclar, ;) Jajá chegaremos até você", Toast.LENGTH_LONG).show()
                            dialog.cancel()
                            val intent = Intent(baseContext, HomeActivity::class.java)
                            startActivity(intent)
                        }


                        dialog.show()


                    }

                }.addOnFailureListener{e->
                    Log.e("ERROR", e.message)

                }
            }
        }

    }

    private fun scheduleretirada(
        street: String,
        zip: String,
        neighborood: String,
        city: String,
        state: String,
        featureName: String,
        complement: String,
        refPoint: String,
        lat: String,
        lng: String,
        st: String
    ) {

        //Receber os dados do usuário autenticado.
        val auth = FirebaseAuth.getInstance()
        val currUser = auth.currentUser

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

        db.collection("agendamento").document(currUser?.uid.toString())
            .set(hashMapOf("name" to currUser?.uid))

        //Definir o local de armazenamento
        val storagePath = db.collection("user_activity").document(currUser?.uid.toString())
            .collection("trashcans").document(System.currentTimeMillis().toString())

        //Adicionando a Localização
        storagePath.collection("trash_location").add(location).addOnSuccessListener { added ->
                Log.d("TAG", "Documento Adicionado com sucesso")
            }

        storagePath.set(hashMapOf("recycler" to currUser?.uid))

        //Buscar o TrashItem que será Armazenado
        val trashItemRef = db.collection("points_trash_can").document(currUser?.uid.toString())
        var trashItemRef2 = db.collection("user_activity").document(currUser?.uid.toString())

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

            val sdate = FirebaseFirestore.getInstance().collection("coletas")
            sdate.get().addOnSuccessListener { cidades ->
                var existe = false
                for (cidade in cidades) {
                    Log.d("Tag_infiniteFor", cidade.id)
                    Log.d("Tag_infiniteFor", city)
                    if (cidade.id == city) {
                        sdate.document(cidade.id).collection("bairros").get()
                            .addOnSuccessListener { bairros ->
                                for (bairro in bairros) {
                                    Log.d("Tag_infiniteFor1", bairro.id)
                                    Log.d("Tag_infiniteFor2", neighborood)
                                    if (bairro.id == neighborood) {
                                        sdate.document(cidade.id).collection("bairros")
                                            .document(bairro.id).collection("coletas").get()
                                            .addOnSuccessListener { coletas ->
                                                val coleta = coletas.last()
                                                Log.d("Tag_infiniteFor", coleta.id)
                                                val scheduleDate = coleta["data"]
                                                val periodo = coleta["periodo"]
                                                Log.d("taginfiniteFor", "cheguei aqui !!!")
                                                storagePath.update("scheduleDate", scheduleDate)
                                                storagePath.update("periodo", periodo)
                                                //COMIT
                                                val currUser =
                                                    FirebaseAuth.getInstance().currentUser!!.uid
                                                FirebaseFirestore.getInstance().collection("users")
                                                    .document(currUser).get()
                                                    .addOnSuccessListener { doc ->

                                                        val user =
                                                            doc.toObject<Users>(Users::class.java)
                                                        val userName = user!!.name

                                                        val title = "Novo Resíduo para Coleta"
                                                        val msg =
                                                            "O usuário ${userName} disponibilizou resíduo para coleta"
                                                        PushNotification(NotificationData(title,
                                                            msg), TOPIC).also { it ->
                                                            SendNotification().sendNotification(it)
                                                            Log.d("FCM", it.toString())
                                                            Log.d("TOKEN_FCM", TOPIC.toString())
                                                        }
                                                    }

                                                existe = true
                                            }
                                    }
                                }
                            }
                    }
                }
                if (existe == false) {
                    storagePath.update("scheduleDate", 0)
                    Toast.makeText(baseContext,
                        "Ainda não estamos coletando no seu bairro !",
                        Toast.LENGTH_LONG)
                }
            }
        }

        //Alterando o status da atividade do usuário
        storagePath.update(mapOf("status" to 1)).addOnSuccessListener {
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

       /* val toast = Toast.makeText(this, "Agendamento realizado com Sucesso", Toast.LENGTH_LONG)

        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()*/

        /*val intent = Intent(baseContext, HomeActivity::class.java)
        startActivity(intent)*/

    }

    // Verifica se a API da google play do celular está em uma versão compatível com a SDK do google maps.
    fun isServicesOk(): Boolean {
        Log.d(TAG, "isServices Ok : Checando google services version")

        val availabe: Int =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MapsActivity)
        if (availabe == ConnectionResult.SUCCESS) {
            //Está tudo correto e o usuário conseguirá fazer as requisições do mapa
            Log.d(TAG, "isServicesOK : Google Play Services are working")
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            //Ocorreu um erro mas o usuário pode resolver
            Log.d(TAG, "isServicesOK: Um Erro ocorreu mas nós conseguimos resolver isso")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@MapsActivity, availabe, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this, "Você não pode fazer uma requisição de mapa", Toast.LENGTH_LONG)
                .show()
        }
        return false
    }

    // Busca a localização do Celular
    fun getDeviceLocation(myMap: GoogleMap) {

        // Pega a localização do Dispositivo
        Log.d(TAG, "getDeviceLocation: getting the devices current Location")
        mClient = LocationServices.getFusedLocationProviderClient(this)
        var currentLocation: Location

        try {
            val location = mClient.lastLocation as Task

            location.addOnCompleteListener(OnCompleteListener {
                if (location.isSuccessful) {
                    Log.d(TAG, "On Complete: Localização Encontrada")
                    val currentLocation = location.result

                    if (currentLocation != null) {
                        moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude),
                            15f,
                            myMap)
                    }

                } else {
                    Log.d(TAG, "On Complete: Localização é NULL")
                    Toast.makeText(this@MapsActivity,
                        "Não foi possível encontrar a localização",
                        Toast.LENGTH_LONG).show()
                }
            })

        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: Security Exception" + e.message)
        }

    }

    // Método de mover a câmera
    fun moveCamera(latLng: LatLng, zoom: Float, myMap: GoogleMap) {

        Log.d(TAG, "Move Camera to : lat: " + latLng.latitude + ", long: " + latLng.longitude)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun getUserAddress(myMap: GoogleMap){

        val curruser = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseFirestore.getInstance().collection("users").document(curruser).collection("address")
        ref.get().addOnSuccessListener {docs->
            if(docs.size() > 0){


                val builder = AlertDialog.Builder(this@MapsActivity)
                val dialogView = LayoutInflater.from(this@MapsActivity).inflate(R.layout.dialog_my_map, null)
                builder.setCancelable(false)
                builder.setView(dialogView)

                val btnPositive = dialogView.findViewById<Button>(R.id.btnDialogMapYes)
                val btnNegative = dialogView.findViewById<Button>(R.id.btnMapNo)
                val txt_desc = dialogView.findViewById<TextView>(R.id.txt_dialog_my_map)

                GetCurrentUserData().getUserAddress(object: GetCurrentUserData.MyCallbackUserAdress{
                    override fun onCallBackUserAddress(location: Location) {

                        val rua = location.street
                        val estado = location.State
                        val bairro = location.neighbord
                        val cidade = location.city
                        val cep =  location.zip
                        val num = location.num

                        txt_desc.text = "O Reciclável está no seu endereço de cadastro ? \n : Rua ${rua} nº ${num}, bairro ${bairro}, ${cidade}, ${estado} - ${cep}"


                    }

                })

                val dialog = builder.create() as AlertDialog
                dialog.show()

                btnPositive.setOnClickListener() {

                    val myMap = myMap

                    GetCurrentUserData().getUserAddress(object: GetCurrentUserData.MyCallbackUserAdress{
                        override fun onCallBackUserAddress(location:Location) {
                            val lat = location.lat
                            val lng = location.lng
                            val latlng = LatLng(lat,lng)

                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))
                            myMap.addMarker(MarkerOptions().position(latlng))
                            btnReady.isEnabled = true
                            Toast.makeText(this@MapsActivity, "Certo! Então é só disponibilizar", Toast.LENGTH_LONG).show()
                        }
                    })

                    dialog.cancel()

                }

                btnNegative.setOnClickListener() {

                    dialog.cancel()

                }


            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_COARSE_LOCATION_CODE -> {
                if (grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Log.d(com.trash2money.trashapp.KClasses.TAG, "Permission has been denied by user")
                }
                else{
                    Log.d(com.trash2money.trashapp.KClasses.TAG, "Permission has been granted by user")
                }
            }
        }
    }

}

//Classe Locazição

