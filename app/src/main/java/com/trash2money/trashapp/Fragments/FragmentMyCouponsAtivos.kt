package com.trash2money.trashapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trash2money.trashapp.Adapters.MyCouponAdapter
import com.trash2money.trashapp.KClasses.*
import com.trash2money.trashapp.R

class FragmentMyCouponsAtivos : Fragment() {

    lateinit var recyclerMyCoupon: RecyclerView

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_my_coupon_tab_ativos, container, false) as View

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()
        val dialog = ProgressBarDialog(context!!)

        dialog.openDialog()

        if (container != null) {
            recyclerMyCoupon = v.findViewById(R.id.recyclerMyCoupon)
            dialog.closeDialog()
        }

        recyclerMyCoupon.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val myCouponList = ArrayList<CouponItem>()

        UserDataCoupon().getMyCouponItem(object : UserDataCoupon.MyCallBackMyCoupons {

            override fun onCallBackCoupons(coupon: CouponItem) {

                myCouponList.add(CouponItem(coupon.id,
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
                    coupon.ratingprice))

                val adapter = MyCouponAdapter(myCouponList)
                recyclerMyCoupon.adapter = adapter
                dialog.closeDialog()
            }
        }, "ativo")

        return v
    }
}
