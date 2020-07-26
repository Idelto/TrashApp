package com.trash2money.trashapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.trash2money.trashapp.R
import com.google.android.material.tabs.TabLayout

class FragmentMyRequest : Fragment() {

    lateinit var MyPagerAdapter: PagerAdapter
    lateinit var viewpager_main: ViewPager
    lateinit var tabs_main: TabLayout
    lateinit var btnHome: ImageButton
    lateinit var btnTrashCan: ImageButton

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_my_request, container, false) as View

        viewpager_main = v.findViewById(R.id.view_pager)
        tabs_main = v.findViewById(R.id.tabs_main)

        val fm: FragmentManager = activity!!.supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.bottom_bar_container, Fragment_bottom_bar()).commit()

        val fragmentAdapter =
            com.trash2money.trashapp.Adapters.MyPagerAdapter(activity!!.supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

        return v
    }

}

