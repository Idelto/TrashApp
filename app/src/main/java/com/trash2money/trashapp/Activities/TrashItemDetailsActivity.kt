package com.trash2money.trashapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Adapters.TCoinsKartAdapter
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
import com.trash2money.trashapp.KClasses.Populate
import com.trash2money.trashapp.KClasses.TCoinsKartItem
import com.trash2money.trashapp.KClasses.Trash
import com.trash2money.trashapp.KClasses.TrashLocation
import com.trash2money.trashapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrashItemDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var name: TextView
    lateinit var car: TextView
    lateinit var collected: TextView
    lateinit var rating: RatingBar
    lateinit var since: TextView
    lateinit var recycler: RecyclerView
    lateinit var address: TextView

    //lateinit var btnMap : ImageButton
    lateinit var txtStatus: TextView
    lateinit var txtScheduleDate: TextView
    lateinit var txtTime: TextView
    lateinit var btnLike: ImageButton
    lateinit var btnDislike: ImageButton
    lateinit var txtAceita: TextView
    lateinit var txtRecusa: TextView
    lateinit var btnBack : ImageButton

    val ERROR_DIALOG_REQUEST: Int = 9001
    var TAG: String = "MainActivity"
    val DEFAULT_ZOOM = 15F
    lateinit var txtSearch: EditText
    lateinit var imgGps: ImageView
    lateinit var mClient: FusedLocationProviderClient

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash_item_details)

        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        name = findViewById(R.id.txtPickerName)
        car = findViewById(R.id.txtPickerCar)
        collected = findViewById(R.id.txtPickerTotalCollected)
        rating = findViewById(R.id.pickerRatingBar)
        since = findViewById(R.id.txtPickerSince)
        recycler = findViewById(R.id.recycler_mytrash)
        address = findViewById(R.id.textLocationAddress)
        txtStatus = findViewById(R.id.txtStatus)
        txtScheduleDate = findViewById(R.id.txtScheduleDate)
        txtTime = findViewById(R.id.txtTime)
        btnLike = findViewById(R.id.btnLike)
        btnDislike = findViewById(R.id.btnDislike)
        txtAceita = findViewById(R.id.txtAceita)
        txtRecusa = findViewById(R.id.txtRecusa)
        btnBack = findViewById(R.id.btn_voltar)

        isServicesOk()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val path = intent.getStringExtra("strPath")

        recycler.layoutManager = LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)

        // FILLING THE LISTVIEW WITH TRASH
        val trashList = ArrayList<TCoinsKartItem>()
        val trashDocRef = FirebaseFirestore.getInstance().document(path)
        val locDocRef = FirebaseFirestore.getInstance().document(path).collection("trash_location")

        trashDocRef.collection("trash_item").get().addOnSuccessListener { docs ->

            for (doc in docs) {

                val trash = doc.toObject<Trash>(Trash::class.java)
                var type = trash.type
                var description = trash.description
                var weight = trash.weight
                var tCoins = trash.trashcoins
                var qty = trash.quantity
                var img = 0

                val df = DecimalFormat("0.00")

                val docsRef = FirebaseFirestore.getInstance().collection("trash_products")
                docsRef.get().addOnSuccessListener { docs ->
                    for (doc in docs) {
                        val obj = doc.toObject<Populate>(Populate::class.java)
                        Log.d("Verificando", doc.id)
                        Log.d("Verificando2", obj.longDesc)
                        Log.d("Verificando3", trash.description)
                        if (obj.shortDesc.capitalize() == trash.description.capitalize()) {
                            Log.d("ShortDesc", "Entrou no Loop")
                            val strDrawable = "ic_b_${doc.id}"
                            val drawable = (this.resources.getIdentifier(strDrawable,
                                "drawable",
                                this.packageName))
                            Log.d("drawable_var", drawable.toString())
                            Log.d("drawable_caderno",
                                this.resources.getDrawable(R.drawable.ic_b_pap_caderno).toString())
                            trashList.add(TCoinsKartItem(strDrawable,
                                trash.description,
                                df.format(trash.weight).toString(),
                                df.format(trash.quantity).toString(),
                                df.format(trash.trashcoins).toString(),
                                trash.trashid))
                        }
                    }

                    val adapter = TCoinsKartAdapter(trashList)
                    recycler.adapter = adapter
                }
            }
        }

        // CAR WITH PICKER INFORMATION
        val docRef = FirebaseFirestore.getInstance().document(path)
        docRef.get().addOnSuccessListener { doc ->

            val obj = doc.toObject<Document>(Document::class.java)

            val picker = obj?.picker
            //val scheduleDate = obj?.scheduleDate
            val status = obj?.status

            Log.d("PICKER", picker.toString())

            if (picker != "") {

                val docRefPicker =
                    FirebaseFirestore.getInstance().collection("picker").document(picker.toString())

                docRefPicker.get().addOnSuccessListener { doc ->

                    val pickerObj = doc.toObject<Picker>(Picker::class.java)
                    val pickerName = pickerObj?.name
                    Log.d("NAME", pickerName.toString())
                    val pickerCarModel = pickerObj?.carModel
                    Log.d("CAR", pickerCarModel.toString())
                    val pickerRating = pickerObj?.rating
                    val pickerTotalCollected = pickerObj?.totalCollected
                    Log.d("TOTALCOLLECT", pickerTotalCollected.toString())
                    val pickerSince = pickerObj?.singInDate
                    Log.d("SINCE", pickerSince.toString())

                    name.text = pickerName.toString()
                    car.text = "Carro : " + pickerCarModel.toString()
                    Log.d("RATING", pickerRating.toString())
                    rating.rating = pickerRating!!
                    collected.text =
                        pickerName.toString() + " já coletou " + pickerTotalCollected.toString() + " kg Com a Trash2money"

                    val newDate = Date(pickerSince!!)
                    val dateFormat = SimpleDateFormat("MMM - yyyy")

                    since.text = "Parceiro desde : " + dateFormat.format(newDate)

                }
            }
        }

        //FILING SCHEDULING DATA
        val scheduleDocRef = FirebaseFirestore.getInstance().document(path)
        scheduleDocRef.get().addOnSuccessListener { doc ->

            val sdate = doc["scheduleDate"].toString().toLong()
            val periodo = doc["periodo"].toString()
            Log.d("Date2", "$periodo")
            Log.d("Date", "$sdate")
            if (sdate > 0) {
                val scheduleDate = Date(doc["scheduleDate"].toString().toLong())
                Log.d("Date2", "$scheduleDate")
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
                val date = dateFormat.format(scheduleDate)
                Log.d("Date", date.toString())

                val weekdayNum = scheduleDate.day
                var weekday = ""

                when (weekdayNum) {
                    0 -> weekday = "Dom."
                    1 -> weekday = "Seg."
                    2 -> weekday = "Ter."
                    3 -> weekday = "Qua."
                    4 -> weekday = "Qui."
                    5 -> weekday = "Sex."
                    6 -> weekday = "Sab."
                }

                val statusCod = doc["status"].toString().toInt()
                var status = ""

                when (statusCod) {

                    0 -> status = "Lixeira Vazia \uD83D\uDE11"
                    1 -> status = "Aguardando Coleta \uD83D\uDE07"
                    2 -> status = "Solicitação e Agendamento \uD83D\uDE07"
                    3 -> status = "Agendamento Confirmado \uD83D\uDE07"
                    4 -> status = "Coletor a Caminho \uD83D\uDE9C"
                    5 -> status = "Coletado  \uD83C\uDFC6"

                }

                if (statusCod != 2) {

                    btnLike.visibility = View.INVISIBLE
                    txtAceita.visibility = View.INVISIBLE
                    btnDislike.visibility = View.INVISIBLE
                    txtRecusa.visibility = View.INVISIBLE
                }

                var time = doc["time"]

                txtScheduleDate.text = weekday + " " + date
                txtStatus.text = status
                txtTime.text = periodo.capitalize().toString()

            } else {

                btnLike.visibility = View.INVISIBLE
                txtAceita.visibility = View.INVISIBLE
                btnDislike.visibility = View.INVISIBLE
                txtRecusa.visibility = View.INVISIBLE

                txtScheduleDate.text = "Coleta não Agendada"
                txtStatus.text = ""
                txtTime.text = ""
            }

        }

        btnLike.setOnClickListener() {

            scheduleDocRef.update("status", 3)

            val intent = Intent(baseContext, TrashItemDetailsActivity::class.java)
            startActivity(intent)

            val toast =
                Toast.makeText(baseContext, "Agendamento Confirmado com sucesso", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

        }

        btnDislike.setOnClickListener() {

            locDocRef.get().addOnSuccessListener { locs ->

                for (loc in locs) {

                    val obj = loc.toObject<TrashLocation>(TrashLocation::class.java)

                    val state = obj.State
                    val number = obj.num
                    val city = obj.city
                    var street = obj.street
                    val refPoint = obj.refPoint
                    val neighbord = obj.neighbord
                    val comp = obj.comp
                    val zip = obj.zip
                    val lat = obj.lat
                    val lng = obj.lng

                    val intent = Intent(baseContext, ScheduleActivity::class.java)

                    intent.putExtra("street", number)
                    intent.putExtra("zip", zip)
                    intent.putExtra("neighborood", neighbord)
                    intent.putExtra("city", city)
                    intent.putExtra("state", state)
                    intent.putExtra("featureName", number)
                    intent.putExtra("complement", comp)
                    intent.putExtra("refPoint", refPoint)
                    intent.putExtra("lat", lat.toString())
                    intent.putExtra("lng", lng.toString())
                    intent.putExtra("docRefpath", "")
                    intent.putExtra("path", path)
                    intent.putExtra("st", "0")
                    startActivity(intent)

                }
            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyRequests")
            startActivity(intent)

        }

    }

    private fun init(myMap: GoogleMap, newLatLng: LatLng) {
        val d = Log.d(TAG, "iniciando")

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        val path = intent.getStringExtra("strPath")

        //FILLING TRASH LOCATION LOCATION INFORMATION
        val locDocRef = FirebaseFirestore.getInstance().document(path).collection("trash_location")

        locDocRef.get().addOnSuccessListener { locs ->

            for (loc in locs) {

                val obj = loc.toObject<TrashLocation>(TrashLocation::class.java)

                val state = obj.State
                var number = obj.num
                val city = obj.city
                val street = obj.street
                var refPoint = obj.refPoint
                val neighbord = obj.neighbord
                var comp = obj.comp
                val zip = obj.zip
                val lat = obj.lat
                val lng = obj.lng

                address.text = "${street} ${neighbord} ${city} ${state} - ${zip}"

                val newLat = lat
                val newLng = lng

                val newLatLng = LatLng(newLat, newLng)

                Log.d("NEWLATLNG", newLatLng.toString())

                val myMap = googleMap

                myMap.isMyLocationEnabled = true
                myMap.uiSettings.isMyLocationButtonEnabled = false

                init(myMap, newLatLng)
                myMap.addMarker(MarkerOptions().position(newLatLng).title("Sua lixeira está aqui"))
            }
        }
    }

    fun isServicesOk(): Boolean {
        Log.d(TAG, "isServices Ok : Checando google services version")

        val availabe: Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (availabe == ConnectionResult.SUCCESS) {
            //Está tudo correto e o usuário conseguirá fazer as requisições do mapa
            Log.d(TAG, "isServicesOK : Google Play Services are working")
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            //Ocorreu um erro mas o usuário pode resolver
            Log.d(TAG, "isServicesOK: Um Erro ocorreu mas nós conseguimos resolver isso")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, availabe, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this, "Você não pode fazer uma requisição de mapa", Toast.LENGTH_LONG)
                .show()
        }
        return false
    }

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
                    Toast.makeText(this,
                        "Não foi possível encontrar a localização",
                        Toast.LENGTH_LONG).show()
                }
            })

        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: Security Exception" + e.message)
        }
    }

    fun moveCamera(latLng: LatLng, zoom: Float, myMap: GoogleMap) {

        Log.d(TAG, "Move Camera to : lat: " + latLng.latitude + ", long: " + latLng.longitude)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }
}

class Document(val picker: String, val scheduleDate: Long, val status: Int) {
    constructor() : this("", 0, 0)
}

class Picker(
    val carModel: String,
    val carPlate: String,
    val email: String,
    val name: String,
    val rating: Float,
    val singInDate: Long,
    val status: Int,
    val totalCollected: Float
) {
    constructor() : this("", "", "", "", 0F, 0, 0, 0F)
}
