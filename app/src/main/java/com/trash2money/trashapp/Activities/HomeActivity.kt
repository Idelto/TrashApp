package com.trash2money.trashapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.trash2money.trashapp.Activities.Start_Login.LoginActivity
import com.trash2money.trashapp.Activities.Start_Login.ProfileActivity
import com.trash2money.trashapp.Fragments.*
import com.trash2money.trashapp.KClasses.GetCurrentUserData
import com.trash2money.trashapp.KClasses.ShareDialog
import com.trash2money.trashapp.KClasses.Users
import com.trash2money.trashapp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawer: DrawerLayout // Menu Lateral
    private lateinit var navigationView: NavigationView
    private lateinit var mfirebaseAnalytics: FirebaseAnalytics
    lateinit var imgNav: ImageView
    lateinit var txtNav: TextView
    lateinit var loggedName: String
    lateinit var loggedFoto: String

    private lateinit var recycle: ImageButton

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        //Lista de ítens do menu lateral
        when (item.itemId) {
            R.id.nav_home -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentHome()).commit()
            R.id.nav_profile -> startActivity(Intent(baseContext, ProfileActivity::class.java))
            R.id.nav_myTrashPointCan -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyTrashCoinsCan()).commit()
            R.id.nav_myTrashCoinsBalance -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyTrashCoinsBalance()).commit()
            R.id.nav_myCouponKart -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentCouponKart()).commit()
            R.id.nav_myCoupons -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyCouponsMain()).commit()
            R.id.nav_myRequests -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyRequest()).commit()
            R.id.nav_help -> help()
            R.id.nav_share -> ShareDialog(this).openShareDialog()//Toast.makeText(this,"Share",Toast.LENGTH_LONG).show()
            R.id.nav_about -> sobrenos()
            R.id.nav_logout -> logout()
        }

        drawer.closeDrawer(GravityCompat.START)

        return true
    }

    // Função de logout
    fun logout() {

        // função de Logout / sair do Firebase
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Até logo, foi um prazer ter você aqui", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun sobrenos() {

        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("http://blog.trash2money.com/index.php/sobre-nos/")
        startActivity(openURL)

    }

    fun help() {

        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://chat.whatsapp.com/CBHAar546CRKdjXALIQoRd")
        startActivity(openURL)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_home)

        //Cria a toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Definindo o menu lateral
        drawer = findViewById(R.id.drawer_layout)

        //Botão de toogle do menu lateral
        val toggle: ActionBarDrawerToggle

        toggle = ActionBarDrawerToggle(this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this@HomeActivity)

        val Headview = navigationView.getHeaderView(0)

        imgNav = Headview.findViewById(R.id.imageView)
        txtNav = Headview.findViewById(R.id.txtView_header)

        GetCurrentUserData().readDataUser(object : GetCurrentUserData.MyCallbackUser {
            override fun onCallBack(user: Users) {
                loggedName = user.name

                txtNav.setText(loggedName)
                val uri = user.fotoPerfil

                if (user.fotoPerfil != "") {
                    Log.d("foto_perfil", user.fotoPerfil.toString())
                    Picasso.get().load(uri).into(imgNav)
                }
            }
        })

        //txtNav.setText("idelto")

        toggle.syncState();

        // Se for a primeira vez ou o não estiver nenhum ítem de menu selecionado ele abre o fragment home.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentHome()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        @Override
        fun onBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val fragment = intent.getStringExtra("fragment")
        if (!fragment.isNullOrEmpty()) {
            Log.d("LOG_Fragment", fragment)
        }

        when (fragment) {
            "FragmentCouponKart" -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentCouponKart()).commit()
            "FragmentMyTrashCoinsCan" -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyTrashCoinsCan()).commit()
            "FragmentMyTrashCoinBalance" -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentMyTrashCoinsBalance()).commit()
            "FragmentCoupons" -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentCoupons()).commit()
            "FragmentMyRequests" -> getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, FragmentMyRequest()).commit()
            "FragmentRecycleMain" -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentRecycleMain()).commit()
            "FragmentSelectedCouponMain" -> {
                val targetURL = intent.getStringExtra("CouponId")
                val bundle = Bundle()
                bundle.putString("couponTarget", targetURL)
                Log.d("TargetURL", targetURL.toString())
                val frag = FragmentSelectedCouponMain()
                frag.arguments = bundle

                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag).commit()
            }

            "FragmentCouponDetails" -> {
                val targetURL = intent.getStringExtra("couponTarget")

                val bundle = Bundle()
                bundle.putString("couponTarget", targetURL)
                Log.d("TargetURL", targetURL.toString())
                val frag = FragmentCouponDetails()
                frag.arguments = bundle

                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag).commit()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this.baseContext, HomeActivity::class.java)
        startActivity(intent)

    }
}
