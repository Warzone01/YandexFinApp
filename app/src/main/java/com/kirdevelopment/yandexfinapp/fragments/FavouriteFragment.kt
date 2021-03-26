package com.kirdevelopment.yandexfinapp.fragments

import android.content.Context
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.yandexfinapp.R
import com.kirdevelopment.yandexfinapp.listeners.StarClickListener
import com.kirdevelopment.yandexfinapp.presenters.StocksFragmentPresenter
import com.kirdevelopment.yandexfinapp.room.StocksDatabase
import com.kirdevelopment.yandexfinapp.room.StocksEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(), StarClickListener {

    private lateinit var favouriteRV: RecyclerView
    private lateinit var textEmpty: TextView
    private lateinit var favouriteButton: ImageView
    private lateinit var favouriteDatabase: StocksDatabase

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

        favouriteFragmentPresenter.getAllFavourites(favouriteRV, view.context, textEmpty, this)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }

    override fun onStarClicked(stock: StocksEntity, position: Int, context: Context) {
        println("CLICK1")
        if (!stock.isFavourite){
            favouriteFragmentPresenter.addStockToFavourite(stock, position, context, this)
        } else{
            println("Click2")
            favouriteFragmentPresenter.deleteStockFromFavourite(stock, position, context, this)
        }
    }
}
