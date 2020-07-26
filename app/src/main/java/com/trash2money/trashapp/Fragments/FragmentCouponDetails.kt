package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.trash2money.trashapp.Activities.HomeActivity
import com.trash2money.trashapp.Adapters.CouponDetailsAdapter
import com.trash2money.trashapp.KClasses.*
import com.trash2money.trashapp.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentCouponDetails : Fragment() {

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

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_cupon_detail, container, false) as View

        viewpager_main = v.findViewById(R.id.view_pager_options_pages)
        tabs_main = v.findViewById(R.id.tabs_options)
        btnback = v.findViewById(R.id.btn_back2)
        btnFav = v.findViewById(R.id.btn_fav)
        nLikes = v.findViewById(R.id.btn_n_likes_)
        shorDesc = v.findViewById(R.id.txt_short_desc)
        desc = v.findViewById(R.id.txt_long_desc)
        imgCouponImg = v.findViewById(R.id.imgCouponImg2)
        btnSendKart = v.findViewById(R.id.btn_enviar_para_carrinho)

        val currUser = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseFirestore.getInstance().collection("user_activity").document(currUser)
            .collection("liked_coupons")

        var fav = false
        val likes = 0

        val couponId = this.arguments?.getString("couponTarget").toString()
        Log.d("TARGET URL", couponId)

        nLikes.text = likes.toString()

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        val fragmentAdapter = CouponDetailsAdapter(activity!!.supportFragmentManager, couponId)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
        val db = FirebaseFirestore.getInstance().collection("coupons").document(couponId!!)

        Log.d("couponId", couponId)

        db.get().addOnSuccessListener { doc ->
            val coupon = doc.toObject<CouponItem>(CouponItem::class.java)

            shorDesc.text = coupon!!.brand
            desc.text = coupon.description

            val myStorage = FirebaseStorage.getInstance().reference
            val targetUrl = myStorage.toString() + "coupon_logo/" + coupon.img
            val storageRef = myStorage.storage.getReferenceFromUrl(targetUrl)

            storageRef.downloadUrl.addOnSuccessListener { url ->
                Log.d("URL", url.toString())
                val imageUrl = url
                Glide.with(context!!).load(imageUrl).into(imgCouponImg)
            }

        }

        btnback.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("fragment", "FragmentCoupons")
            startActivity(intent)
        }

        GetCoupon().readCoupon(object : GetCoupon.MyCallbackCoupon {
            val couponRef = FirebaseFirestore.getInstance().collection("coupons").document(couponId)

            override fun onCallBack(coupon: CouponItem) {
                var like = coupon.rating.toInt()
                nLikes.text = coupon.rating.toInt().toString()
            }
        }, couponId)

        btnFav.setOnClickListener {
            db.get().addOnSuccessListener { cp ->

                val obj = cp.toObject<CouponItem>(CouponItem::class.java)
                var like = obj!!.rating.toInt()

                if (fav == false) {
                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_24_green)
                    val fav_coupon = hashMapOf("isFav" to true)
                    Log.d("Update", "isFav-True")
                    ref.document(couponId).set(fav_coupon)
                    fav = true
                    like += 1
                    db.update("rating", like).addOnSuccessListener {
                        Log.d("Click", "Clicked + 1 ")
                        Log.d("Click", like.toString())
                    }
                    nLikes.text = (obj.rating.toInt() + 1).toString()
                } else if (fav == true) {
                    fav = false
                    btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                    ref.get().addOnSuccessListener { docs ->
                        for (doc in docs) {
                            if (doc.id == couponId) {
                                ref.document(doc.id).update("isFav", false)
                                Log.d("Update", "isFav-False")
                            }
                        }
                    }
                    if (like > 0) {
                        like = obj.rating.toInt()
                        like -= 1
                        db.update("rating", like).addOnSuccessListener {

                            Log.d("Click", "Clicked - 1 ")
                            Log.d("Click", like.toString())
                        }
                    }
                    nLikes.text = (obj.rating.toInt() - 1).toString()
                }
            }
        }

        ref.get().addOnSuccessListener { docs ->
            for (doc in docs) {
                if (doc.id == couponId) {
                    if (doc["isFav"] == true) {
                        Log.d("IsFav", doc["isFav"].toString())
                        btnFav.setImageResource(R.drawable.ic_baseline_favorite_24_green)
                        fav = true
                    }
                }
            }
        }
        btnSendKart.setOnClickListener {

            //Botao para enviar para o kart
            Log.d("Clicando", "Clicado")
            Log.d("Clicando Context", getContext().toString())

            db.get().addOnSuccessListener { doc ->

                val coupon = doc.toObject<CouponItem>(CouponItem::class.java)

                val db = FirebaseFirestore.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser

                val data = CouponItem(coupon!!.id,
                    coupon.img,
                    coupon.brand,
                    coupon.category,
                    coupon.cost,
                    coupon.description,
                    coupon.disc,
                    coupon.qty,
                    coupon.rating,
                    coupon.validate,
                    coupon.full_desc,
                    coupon.value,
                    coupon.views,
                    coupon.impressions,
                    coupon.status,
                    coupon.idkey,
                    coupon.prio,
                    coupon.rules,
                    coupon.how,
                    coupon.codcoupon,
                    coupon.coupontrash,
                    coupon.marca,
                    coupon.ratingprice)

                db.collection("coupons_kart").document(currentUser?.uid.toString())
                    .collection("coupon_item").add(data).addOnSuccessListener {
                        Toast.makeText(activity,
                            "Seu ítem foi enviado para o carrinho de CUPONS com sucesso",
                            Toast.LENGTH_LONG).show()
                        db.collection("coupons_kart").document(currentUser?.uid.toString())
                            .collection("coupon_item").document(it.id).update(mapOf("id" to it.id))

                    }
            }

            val builder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_coupon, null)
            builder.setCancelable(false)
            builder.setView(dialogView)

            val btnPositive = dialogView.findViewById<Button>(R.id.btnDialogMapYes)
            val btnNegative = dialogView.findViewById<Button>(R.id.btnDialogCouponNo)

            val dialog = builder.create() as AlertDialog

            btnPositive.setOnClickListener() {
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("fragment", "FragmentCoupons")
                startActivity(intent)
            }

            btnNegative.setOnClickListener() {

                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("fragment", "FragmentCouponKart")
                startActivity(intent)

            }

            dialog.show()

        }

        return v
    }

}

