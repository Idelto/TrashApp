package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.trash2money.trashapp.Adapters.SelectedCouponAdapter
import com.trash2money.trashapp.R
import com.google.android.material.tabs.TabLayout

class FragmentSelectedCouponMain : Fragment() {

    lateinit var viewpager_main: ViewPager
    lateinit var tabs_main: TabLayout

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.activity_selected_coupon_main, container, false) as View

        viewpager_main = v.findViewById(R.id.view_pager_options_pages_selectedCoupon)
        tabs_main = v.findViewById(R.id.tabs_options_selectedCoupon)

        val couponId = this.arguments?.getString("couponTarget").toString()

        Log.d("txtlog2", " $couponId ")

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        val fragmentAdapter = SelectedCouponAdapter(activity!!.supportFragmentManager, couponId)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
        Log.d("tab", "tab Main")

        return v
    }

}

