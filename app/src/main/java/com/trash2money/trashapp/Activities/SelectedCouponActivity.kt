package com.trash2money.trashapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.trash2money.trashapp.Fragments.Fragment_bottom_bar
import com.trash2money.trashapp.KClasses.CouponItem
import com.trash2money.trashapp.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SelectedCouponActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    lateinit var viewpager_main: ViewPager
    lateinit var tabs_main: TabLayout
    lateinit var btnback: ImageButton
    lateinit var btnFav: ImageButton
    lateinit var nLikes: TextView
    lateinit var shorDesc: TextView
    lateinit var desc: TextView
    lateinit var imgCouponImg: ImageView
    lateinit var btnSendKart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_coupon)

        val intent: Intent
        intent = getIntent()

        viewpager_main = findViewById(R.id.view_pager_options_pages_selectedCoupon)
        tabs_main = findViewById(R.id.tabs_options_selectedCoupon)
        btnback = findViewById(R.id.btn_back2)
        btnFav = findViewById(R.id.btn_fav)
        nLikes = findViewById(R.id.btn_n_likes_)
        shorDesc = findViewById(R.id.txt_short_desc)
        desc = findViewById(R.id.txt_long_desc)
        imgCouponImg = findViewById(R.id.imgCouponImg2)

        var fav = false
        var likes = 0

        val id = intent.getStringExtra("CouponId")

        Log.d("ID", id)

        val currUser = FirebaseAuth.getInstance().currentUser
        val docRef = FirebaseFirestore.getInstance().collection("user_activity")
            .document(currUser?.uid.toString()).collection("coupons").whereEqualTo("id", id)

        //nLikes.text = "${likes.toString()} Likes"

        val fm: FragmentManager = this!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        docRef.get().addOnSuccessListener { docs ->

            for (doc in docs) {

                FirebaseFirestore.getInstance().collection("user_activity")
                    .document(currUser?.uid.toString()).collection("coupons").document(doc.id).get()
                    .addOnSuccessListener { d ->

                        val obj = d.toObject<CouponItem>(CouponItem::class.java)

                        shorDesc.text = obj!!.brand
                        desc.text = obj.description

                        val storage = FirebaseStorage.getInstance().reference
                        val targetUrl = storage.toString() + "coupon_logo/" + obj?.img
                        val storageRef = storage.storage.getReferenceFromUrl(targetUrl!!)

                        storageRef.downloadUrl.addOnSuccessListener { url ->

                            val imgUrl = url
                            Glide.with(baseContext).load(imgUrl).into(imgCouponImg)

                        }
                    }
            }
        }

        btnback.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentMyCoupons")
            startActivity(intent)
        }
    }
}
