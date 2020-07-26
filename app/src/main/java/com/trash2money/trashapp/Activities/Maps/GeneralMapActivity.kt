package com.trash2money.trashapp.Activities.Maps


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
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

class GeneralMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var TAG: String = "MainActivity"
    val ERROR_DIALOG_REQUEST: Int = 9001
    val DEFAULT_ZOOM = 15F
    lateinit var txtSearch: EditText
    lateinit var imgGps: ImageView
    lateinit var mClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_map)

        isServicesOk()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        imgGps = findViewById(R.id.imgGps)

        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()


    }

    private fun init(myMap: GoogleMap, newLatLng: LatLng) {
        Log.d(TAG, "iniciando")

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val newLat = intent.getDoubleExtra("lat", 0.0)
        val newLng = intent.getDoubleExtra("lng", 0.0)

        val newLatLng = LatLng(newLat, newLng)

        Log.d("NEWLATLNG", newLatLng.toString())

        val myMap = googleMap

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        myMap.isMyLocationEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = false

        init(myMap, newLatLng)
        myMap.addMarker(MarkerOptions().position(newLatLng).title("Sua lixeira está aqui"))

        imgGps.setOnClickListener() {
            getDeviceLocation(myMap)
        }

    }

    fun isServicesOk(): Boolean {
        Log.d(TAG, "isServices Ok : Checando google services version")

        val availabe: Int = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this@GeneralMapActivity)
        if (availabe == ConnectionResult.SUCCESS) {
            //Está tudo correto e o usuário conseguirá fazer as requisições do mapa
            Log.d(TAG, "isServicesOK : Google Play Services are working")
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            //Ocorreu um erro mas o usuário pode resolver
            Log.d(TAG, "isServicesOK: Um Erro ocorreu mas nós conseguimos resolver isso")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@GeneralMapActivity, availabe, ERROR_DIALOG_REQUEST)
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
                    Toast.makeText(this@GeneralMapActivity,
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



