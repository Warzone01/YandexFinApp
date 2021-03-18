package com.kirdevelopment.yandexfinapp.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.adapters.MainAdapter
import com.kirdevelopment.yandexfinapp.api.RetrofitInstance
import com.kirdevelopment.yandexfinapp.api.StockApi
import com.kirdevelopment.yandexfinapp.presenters.StocksFragmentPresenter
import com.kirdevelopment.yandexfinapp.views.StocksView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpFragment
import moxy.presenter.InjectPresenter
import retrofit2.*
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class StocksFragment : Fragment() {

    private lateinit var stocksRV: RecyclerView
    private lateinit var stocksProgress: CircularProgressIndicator

    lateinit var stocksPresenter: StocksFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stocks, container, false)

        stocksPresenter = StocksFragmentPresenter()

        stocksRV = view.findViewById(R.id.stocksRV)
        stocksProgress = view.findViewById(R.id.stocksProgress)

        stocksRV.layoutManager = LinearLayoutManager(view.context)

        stocksPresenter.getCurrentData(stocksRV, stocksProgress, view.context)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = StocksFragment()
    }


}