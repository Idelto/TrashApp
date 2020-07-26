package com.trash2money.trashapp.Activities.Start_Login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
import com.trash2money.trashapp.KClasses.GetCurrentUserData
import com.trash2money.trashapp.KClasses.Location
import com.trash2money.trashapp.KClasses.Users
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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*

class ProfileActivity: AppCompatActivity(), OnMapReadyCallback {

    lateinit var auth : FirebaseAuth
    lateinit var nome: TextView
    lateinit var email: EditText
    lateinit var celular: EditText
    lateinit var btnFoto : Button
    lateinit var btnAtualizaInformacoes : Button
    lateinit var mSelecteduri: Uri
    lateinit var imgFoto : ImageView
    lateinit var fabTrocarFoto : View
    lateinit var imagePerfil : Uri
    lateinit var txtEndereco : TextView
    lateinit var btnBack : ImageButton

    var TAG: String = "MainActivity"
    val ERROR_DIALOG_REQUEST: Int = 9001
    val DEFAULT_ZOOM = 15F
    lateinit var myMap: GoogleMap
    lateinit var txtSearch: EditText
    lateinit var imgGps: ImageView
    lateinit var mClient : FusedLocationProviderClient
    lateinit var btnReady : Button
    lateinit var txtSearch2 : EditText
    lateinit var neighbord : String
    lateinit var thoroughfare: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile2)
        isServicesOk()

        //trazer o objeto support mapfragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map4) as SupportMapFragment
        mapFragment.getMapAsync(this)


        nome = findViewById(R.id.txt_nome)
        email = findViewById(R.id.txt_email)
        celular = findViewById(R.id.txt_telefone)
        imgFoto = findViewById(R.id.imgFoto)
        btnAtualizaInformacoes = findViewById(R.id.btnAtualizar_Informacoes)
        fabTrocarFoto = findViewById(R.id.fabTrocarFoto)
        txtEndereco = findViewById(R.id.txtEndereco)
        txtSearch = findViewById(R.id.txt_search)
        txtSearch2 = findViewById(R.id.txt_numero)
        btnBack = findViewById(R.id.btn_back)


        val fm : FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container,Fragment_bottom_bar()).commit()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef =db.collection("users").document(currentUser?.uid.toString())

        btnBack.setOnClickListener{

            val intent = Intent(this.baseContext, HomeActivity::class.java)
            startActivity(intent)
        }


        //API Google Places

        //var placesClient = Places.createClient(this@ProfileActivity) as PlacesClient

        val autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))

        autoCompleteFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener {
            override fun onPlaceSelected(place: com.google.android.libraries.places.api.model.Place) {
                Toast.makeText(this@ProfileActivity, "Place: " + place.name.toString() + " ID :"+ place.latLng.toString(), Toast.LENGTH_LONG).show()

                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng,15f))
                myMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name))

                btnReady.isEnabled = true

            }

            override fun onError(e: Status) {
                Toast.makeText(this@ProfileActivity, "Oopes deu erro: " + e.statusMessage , Toast.LENGTH_LONG).show()
            }

        })

        fabTrocarFoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent,42)


        }

        var loggedName = ""
        var loggedEmail = ""
        var loggedCelular = ""
        var loggedFoto= ""
        var uriFoto = ""

        /*GetCurrentUserData().readDataUser(object : GetCurrentUserData.MyCallbackUser {
            override fun onCallBack(user: Users) {
                loggedName = user.name
                loggedEmail = user.email
                loggedCelular = user.celular
                loggedFoto = user.fotoPerfil
                uriFoto = user.urifoto

                docRef.collection("address").get().addOnSuccessListener {query->

                    var objLocation = query.last().toObject<Location>(Location::class.java)
                    Log.d("querylast", query.last().id.toString())

                    var lat = objLocation.lat
                    var lng = objLocation.lng

                    txtEndereco.text = "${objLocation.street} , ${objLocation.zip} , ${objLocation.city}, ${objLocation.State}"

                    txtSearch2.setText(objLocation.num.toString())
                    txtSearch.setText(objLocation.comp.toString())

                }

                nome.setText(" "+ loggedName)
                email.setText(" "+ loggedEmail)
                celular.setText(" " + loggedCelular)
                val uri = user.fotoPerfil
                if(uri != ""){
                    Picasso.get().load(uri).into(imgFoto)
                }else{

                }
            }
        })*/

    }

    // função que inicia o mapa
    private fun init(myMap: GoogleMap) {
        val d = Log.d(TAG, "iniciando")

        txtSearch.setOnEditorActionListener{textView,actionId,keyEvent->
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId== EditorInfo.IME_ACTION_DONE
                || keyEvent.action== KeyEvent.ACTION_DOWN || keyEvent.action == KeyEvent.KEYCODE_ENTER){
                geoLocate(myMap)
            }
            false
        }
    }


    //Traz a sua localização atual através do gps do celular
    fun geoLocateLatLng(myMap: GoogleMap,lat:Double, long:Double): Address {

        val geoCoder = Geocoder(this@ProfileActivity)
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

    fun geoLocate(myMap: GoogleMap){

        val searchString : String
        val geoCoder = Geocoder(this@ProfileActivity)
        var list = ArrayList<Address>()

        searchString = txtSearch.text.toString()

        try {
            list = geoCoder.getFromLocationName(searchString,1) as ArrayList<Address>

        }catch(e: IOException){
            Log.e(TAG,"GeoLocate: IO Exception"+e.message)
        }

        if(list.size >0){

            val address : Address
            address =  list[0]

            Log.d(TAG,"GeoLocation: Localização encontrada : "+address.toString())

            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude),DEFAULT_ZOOM))
            myMap.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)).title(address.getAddressLine(0)))

        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        val myMap = googleMap
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef =db.collection("users").document(currentUser?.uid.toString())


        GetCurrentUserData().readDataUser(object : GetCurrentUserData.MyCallbackUser {
            override fun onCallBack(user: Users) {
                val loggedName = user.name
                val loggedEmail = currentUser!!.email
                val loggedCelular = user.celular
                var loggedFoto = user.fotoPerfil
                var uriFoto = user.urifoto

                nome.setText(loggedName)
                email.setText(loggedEmail)
                celular.setText(loggedCelular)
                val uri = user.fotoPerfil
                if(uri != ""){
                    Picasso.get().load(uri).into(imgFoto)
                }else{

                }

                    docRef.collection("address").get().addOnSuccessListener { query ->

                        if (query.size() > 0) {

                            val objLocation = query.last().toObject<Location>(Location::class.java)
                            Log.d("querylast", query.last().id.toString())

                            val lat = objLocation.lat
                            val lng = objLocation.lng

                            txtEndereco.text =
                                "${objLocation.street} , ${objLocation.zip} , ${objLocation.city}, ${objLocation.State}"
                            geoLocateLatLng(myMap, lat, lng)
                            moveCamera(LatLng(lat, lng), 15f, myMap)
                            myMap.addMarker(MarkerOptions().position(LatLng(lat, lng))
                                .title("Seu Endereço"))

                            txtSearch2.setText(objLocation.num.toString())
                            txtSearch.setText(objLocation.comp.toString())

                        }
                    }


                btnAtualizaInformacoes.setOnClickListener {

                    docRef.update("primeiroLogin","logado")
                    docRef.update("celular",celular.text.toString())
                    docRef.update("nome",nome.text.toString())
                    //docRef.update("email",email.text.toString())

                    val location = returnlocation(myMap)
                    location.num
                    docRef.collection("address").document(System.currentTimeMillis().toString()).set(location)

                    Log.d("TAG_verifica","${email.text.toString()} e ${currentUser!!.email.toString()}")


                    if (email.text.toString() != currentUser!!.email){

                    val user = FirebaseAuth.getInstance().currentUser!!

                        Log.d("TAG_Passei","${email.text.toString()} e ${currentUser!!.email.toString()}")

                        val builder = AlertDialog.Builder(this@ProfileActivity)
                        val dialogView = LayoutInflater.from(this@ProfileActivity).inflate(R.layout.dialog_login, null)

                        builder.setCancelable(false)
                        builder.setView(dialogView)

                        val dialog = builder.create() as Dialog

                        val btnEntrar2 = dialogView.findViewById<Button>(R.id.btnEntrar2)
                        val btnResetarSenha2 = dialogView.findViewById<Button>(R.id.btnEsqueceuSenha2)
                        val email2 = dialogView.findViewById<EditText>(R.id.txtEmail2)
                        val senha2 = dialogView.findViewById<EditText>(R.id.txtPassword2)
                        val btnBack3 = dialogView.findViewById<ImageButton>(R.id.btn_back3)
                        email2.setText(currentUser!!.email)

                        btnBack3.setOnClickListener{
                            dialog.cancel()
                        }

                        btnEntrar2.setOnClickListener(){

                            val mEmail = email2.text.toString() // busca do valor escrito nos EditText
                            val mSenha = senha2.text.toString() // busca do valor escrito nos EditText

                            if(mEmail.isNotEmpty() && mSenha.isNotEmpty()){
                                Log.d("Email",user.email)
                                // método de autenticação do firebase
                                auth.signInWithEmailAndPassword(mEmail,mSenha).addOnSuccessListener { task->
                                    if (auth.getCurrentUser()?.isEmailVerified()!!) {
                                        var currUser = auth.currentUser
                                        Log.d("Email",user.email)
                                        currentUser.updateEmail(email.text.toString()).addOnSuccessListener {
                                            Log.d("FirebaseAUTH", "Alterou")
                                            Toast.makeText(baseContext,"Alterado ! Um e-mail de verificação foi enviado para seu novo e-mail, favor verificar e realizar login novamente.", Toast.LENGTH_LONG).show()
                                            auth.currentUser!!.sendEmailVerification()
                                            auth.signOut()
                                            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                        }.addOnFailureListener {exception->
                                            Log.d("FirebaseAUTH", "Deu erro")
                                            Log.d("FirebaseAUTH", exception.toString())
                                            Log.d("FirebaseAUTH", currentUser!!.uid)
                                        }

                                        dialog.cancel()
                                    }else{
                                        Toast.makeText(baseContext,"Favor verificar o e-mail",Toast.LENGTH_LONG).show()
                                    }
                                }
                                    .addOnFailureListener { e->
                                        Toast.makeText(baseContext,"Falha no Login ", Toast.LENGTH_LONG).show()
                                        Log.e("ERRO", e.message)
                                    }
                            }else{
                                Toast.makeText(baseContext,"Por favor Digite o e-mail e a Senha",Toast.LENGTH_LONG).show()
                            }



                        }

                        btnResetarSenha2.setOnClickListener(){
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(baseContext, PasswordResetActivity::class.java)
                            startActivity(intent)
                        }
                        dialog.show()
                    }


                    if (currentUser!!.email == email.text.toString()){Toast.makeText(this@ProfileActivity,"Seus dados foram atualizados com sucesso",Toast.LENGTH_LONG).show()}
                }
            }
        })

        Log.d(TAG, "getDeviceLocation: getting the devices current Location")

        //Informações do meu dispositivo
        mClient = LocationServices.getFusedLocationProviderClient(this@ProfileActivity)

        try {

            val location = mClient.lastLocation as Task

            //mClient para uma localização no mapa
            location.addOnCompleteListener(OnCompleteListener {
                if(location.isSuccessful){
                    Log.d(TAG,"On Complete: Localização Encontrada")

                    // variável localização atual = informações do dispositivo
                    val currentLocation = location.result
                    if (currentLocation != null) {
                        val latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
                        val zoom = 15f

                        Log.d(TAG,"Move Camera to : lat: " + latLng.latitude+", long: "+latLng.longitude)

                        // Método de mover a camera.
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
                        //myMap.addMarker(MarkerOptions().position(latLng).title("Matheus"))
                    }
                }else{
                    //Caso não seja possível encontrar a localização do celular
                    Log.d(TAG,"On Complete: Localização é NULL")
                    Toast.makeText(this@ProfileActivity, "Não foi possível encontrar a localização", Toast.LENGTH_LONG).show()
                }

            })

        }catch (e: SecurityException){
            Log.e(TAG,"getDeviceLocation: Security Exception" + e.message)
        }

        // Mostrando para o Maps que a sua localização está válida
        myMap.isMyLocationEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = false

        // inicio no Mapa que foi definido nos passo acima
        init(myMap)

        //Botão que devolve você para a sua origem
        /*imgGps.setOnClickListener(){
            getDeviceLocation(myMap)
        }*/



        // Chave da API -  Chave de conexão com o Maps Console
        val apiKey = "AIzaSyALz7_FTU5c2Vu87huK0wX40aLzTR9Ybew"

        // Initialize the SDK
        Places.initialize(applicationContext,apiKey)

        // Create a new Places client instance

        val autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,com.google.android.libraries.places.api.model.Place.Field.NAME,
            com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))

        autoCompleteFragment.setOnPlaceSelectedListener(object:PlaceSelectionListener{
            override fun onPlaceSelected(place: com.google.android.libraries.places.api.model.Place) {
                Toast.makeText(this@ProfileActivity, "Place: " + place.name.toString() + " ID :"+ place.latLng.toString(), Toast.LENGTH_LONG).show()

                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng,15f))
                myMap.addMarker(MarkerOptions().position(place.latLng!!).title(place.name))

                val address = geoLocateLatLng(myMap,myMap.cameraPosition.target.latitude,myMap.cameraPosition.target.longitude)

                if(address.thoroughfare==null){
                    thoroughfare = ""
                }else{
                    thoroughfare = address.thoroughfare
                }

                if(address.subLocality==null){
                    neighbord = ""
                }else{
                    neighbord = address.subLocality
                }

                //Criação do objeto localização com base nos dados do Marker
                val location =
                    Location(
                        thoroughfare,
                        address?.postalCode.toString(),
                        neighbord,
                        address?.subAdminArea,
                        address?.adminArea,
                        address?.featureName,
                        txtSearch?.text.toString(),
                        txtSearch2?.text.toString(),
                        address?.latitude,
                        address?.longitude)

                txtEndereco.text = "${thoroughfare} , ${address.postalCode} , ${address.subAdminArea}, ${address.adminArea}"

            }

            override fun onError(e: Status) {
                Toast.makeText(this@ProfileActivity, "Oopes deu erro: " + e.statusMessage , Toast.LENGTH_LONG).show()
            }

        })

    }

    fun isServicesOk(): Boolean {
        Log.d(TAG, "isServices Ok : Checando google services version")

        val availabe: Int =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@ProfileActivity)
        if (availabe == ConnectionResult.SUCCESS) {
            //Está tudo correto e o usuário conseguirá fazer as requisições do mapa
            Log.d(TAG, "isServicesOK : Google Play Services are working")
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            //Ocorreu um erro mas o usuário pode resolver
            Log.d(TAG, "isServicesOK: Um Erro ocorreu mas nós conseguimos resolver isso")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@ProfileActivity, availabe, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this@ProfileActivity, "Você não pode fazer uma requisição de mapa", Toast.LENGTH_LONG).show()
        }
        return false
    }


    // Busca a localização do Celular
    fun getDeviceLocation(myMap: GoogleMap){
        // Pega a localização do Dispositivo
        Log.d(TAG, "getDeviceLocation: getting the devices current Location")
        mClient = LocationServices.getFusedLocationProviderClient(this@ProfileActivity)
        var currentLocation: android.location.Location

        try {
            val location = mClient.lastLocation as Task

            location.addOnCompleteListener(OnCompleteListener {
                if(location.isSuccessful){
                    Log.d(TAG,"On Complete: Localização Encontrada")
                    val currentLocation = location.result

                    if (currentLocation != null) {
                        moveCamera(LatLng(currentLocation.latitude,currentLocation.longitude),15f,myMap)
                    }

                }else{
                    Log.d(TAG,"On Complete: Localização é NULL")
                    Toast.makeText(this@ProfileActivity, "Não foi possível encontrar a localização", Toast.LENGTH_LONG).show()
                }
            })

        }catch (e: SecurityException){
            Log.e(TAG,"getDeviceLocation: Security Exception" + e.message)
        }


    }

    // Método de mover a câmera
    fun moveCamera(latLng: LatLng, zoom:Float, myMap: GoogleMap){

        Log.d(TAG,"Move Camera to : lat: " + latLng.latitude+", long: "+latLng.longitude)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))

        //poppopop
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 42) {
            //val takeImage = data.extras.get("data") as Bitmap
            //var takeImage = data?.getParcelableExtra<Bitmap>("data")
            //imgFoto.setImageBitmap(takeImage)
            mSelecteduri = data?.data!!
            imgFoto.setImageURI(mSelecteduri)
            uploadFoto()
            //imgFoto.scaleType = ImageView.ScaleType.FIT_XY
            //Toast.makeText(activity,"trocou a foto ", Toast.LENGTH_LONG).show()
            //val bitmap = (imgFoto.drawable as BitmapDrawable).bitmap
            //val bitmapDrawable = BitmapDrawable(takeImage)
            //imgFoto.setImageDrawable(bitmapDrawable)


        }
    }

    private fun uploadFoto(){
        if (mSelecteduri == null) return

        val fileNmae = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image_users/$fileNmae")

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef =db.collection("users").document(currentUser?.uid.toString())

        docRef.update("uriFoto",fileNmae)

        ref.putFile(mSelecteduri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","file location: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","Successfully upload image : $it")
                    val fotoPerfil = it.toString()

                    auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    val db = FirebaseFirestore.getInstance()
                    val docRef =db.collection("users").document(currentUser?.uid.toString())

                    docRef.update("fotoPerfil",fotoPerfil)

                }
            }

    }

    private fun returnlocation(myMap: GoogleMap):Location{

        val address = geoLocateLatLng(myMap,myMap.cameraPosition.target.latitude,myMap.cameraPosition.target.longitude)

        if(address.thoroughfare==null){
            thoroughfare = ""
        }else{
            thoroughfare = address.thoroughfare
        }

        if(address.subLocality==null){
            neighbord = ""
        }else{
            neighbord = address.subLocality
        }

        val location =
            Location(
                address.thoroughfare,
                address?.postalCode.toString(),
                neighbord,
                address?.subAdminArea,
                address?.adminArea,
                address?.featureName,
                txtSearch?.text.toString(),
                txtSearch2?.text.toString(),
                address?.latitude,
                address?.longitude)

        return location
    }




}