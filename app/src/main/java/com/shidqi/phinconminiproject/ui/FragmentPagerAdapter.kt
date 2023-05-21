package com.shidqi.phinconminiproject.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

 class FragmentAdapter(fm: FragmentManager, lifecycle:Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
     private val fragmentList: MutableList<Fragment> = mutableListOf()
     override fun getItemCount(): Int {
         return fragmentList.size
     }

     override fun createFragment(position: Int): Fragment {
         return fragmentList[position]
     }

     fun addFragment(fragment:Fragment){
         fragmentList.add(fragment)
     }

 }

