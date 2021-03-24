package com.kirdevelopment.yandexfinapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.presenters.FavouriteFragmentPresenter
import com.kirdevelopment.yandexfinapp.presenters.StocksFragmentPresenter
import com.kirdevelopment.yandexfinapp.room.StocksDatabase
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpFragment

class FavouriteFragment : Fragment() {

    private lateinit var favouriteRV: RecyclerView
    private lateinit var textEmpty: TextView

    lateinit var favouriteFragmentPresenter: StocksFragmentPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        favouriteFragmentPresenter = StocksFragmentPresenter()

        favouriteRV = view.findViewById(R.id.favouritesRV)
        favouriteRV.layoutManager = LinearLayoutManager(view.context)

        textEmpty = view.findViewById(R.id.textEmpty)

        favouriteFragmentPresenter.getAllFavourites(favouriteRV, view.context)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}
