package com.example.resumemaker.views.fragments.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Vadapterswipe(getFragmentManager: FragmentManager): FragmentPagerAdapter(getFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val fragmentArrayList = ArrayList<Fragment>()
    private val fragmenttitle = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment: Fragment?, title: String?) {
        fragmentArrayList.add(fragment!!)
        fragmenttitle.add(title!!)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return fragmenttitle[position]
    }
}