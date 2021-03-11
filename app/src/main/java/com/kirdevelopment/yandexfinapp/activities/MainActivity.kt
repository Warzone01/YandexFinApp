package com.kirdevelopment.yandexfinapp.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.adapters.TabsViewPagerAdapter
import com.kirdevelopment.yandexfinapp.presenters.MainActivityPresenter
import com.kirdevelopment.yandexfinapp.views.MainView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mainTabsViewPager: ViewPager2
    private lateinit var mainStocksTabText: TextView
    private lateinit var mainFavouriteTabText: TextView

    @InjectPresenter
    lateinit var mainPresenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFavouriteTabText = findViewById(R.id.textTabFavourite)
        mainStocksTabText = findViewById(R.id.textTabStocks)

        mainTabsViewPager = findViewById(R.id.mainTabsViewPager)
        initTabs()

    }

    //add stocks and favourite tabs to main activity
    override fun initTabs() {
        //add adapter for view pager
        mainTabsViewPager.adapter = TabsViewPagerAdapter(supportFragmentManager, lifecycle)
        mainTabsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //change color and text size when focused
                when (position) {
                    //when stocks tab in focus
                    0 -> {
                        mainStocksTabText.setTextColor(Color.BLACK)
                        mainStocksTabText.textSize = 30f

                        mainFavouriteTabText.setTextColor(Color.GRAY)
                        mainFavouriteTabText.textSize = 20f
                    }
                    //when favourite tab in focus
                    else ->{
                        mainFavouriteTabText.setTextColor(Color.BLACK)
                        mainFavouriteTabText.textSize = 30f

                        mainStocksTabText.setTextColor(Color.GRAY)
                        mainStocksTabText.textSize = 20f
                    }
                }
            }
        })

        //change to stocks tab on click
        mainStocksTabText.setOnClickListener {
            mainTabsViewPager.currentItem = 0
        }
        //change to favourite tab on click
        mainFavouriteTabText.setOnClickListener {
            mainTabsViewPager.currentItem = 1
        }
    }
}