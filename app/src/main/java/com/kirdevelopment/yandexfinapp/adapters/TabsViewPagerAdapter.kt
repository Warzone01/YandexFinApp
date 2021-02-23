package com.kirdevelopment.yandexfinapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kirdevelopment.yandexfinapp.fragments.FavouriteFragment
import com.kirdevelopment.yandexfinapp.fragments.StocksFragment

class TabsViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment{
        return when (position) {
            0 -> {
                StocksFragment()
            }
            else -> {
                FavouriteFragment()
            }
        }
    }
}