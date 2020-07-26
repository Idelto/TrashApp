package com.trash2money.trashapp.KClasses

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPageAdapter : FragmentPagerAdapter {

    val mFragementList = ArrayList<Fragment>()
    val mFragementTitleList = ArrayList<String>()

    constructor(fm: FragmentManager?) : super(fm!!)

    fun addFragment(fragment: Fragment, title: String) {
        mFragementList.add(fragment)
        mFragementTitleList.add(title)

    }

    fun SectionsPageAdapter(fm: FragmentManager) {

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragementTitleList.get(position)
    }

    override fun getItem(position: Int): Fragment {
        return mFragementList.get(position)
    }

    override fun getCount(): Int {
        return mFragementList.size
    }

}