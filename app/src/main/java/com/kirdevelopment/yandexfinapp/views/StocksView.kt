package com.kirdevelopment.yandexfinapp.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface StocksView: MvpView{

    fun onStartLoading()
    fun onEndLoading()
    fun onError(string: String)


}