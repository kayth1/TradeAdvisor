package com.ceaver.assin.assets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.ceaver.assin.action.ActionRepository
import com.ceaver.assin.assets.overview.AssetOverview
import com.ceaver.assin.markets.Title
import com.ceaver.assin.markets.TitleRepository

object AssetRepository {

    fun loadAssetOverviewObserved(): LiveData<AssetOverview> {
        val assetLiveData = loadAllAssetsObserved()
        return Transformations.map(assetLiveData) { it.map {AssetOverview(it.btcValue, it.usdValue) }.reduce { x, y -> AssetOverview(x.btcValue + y.btcValue, x.usdValue + y.usdValue)}}
    }

    fun loadAllAssetsObserved(): LiveData<List<Asset>> {
        val titleLiveData = TitleRepository.loadActiveCryptoTitles()
        val actionLiveData = ActionRepository.loadAllActionsObserved()

        return MediatorLiveData<List<Asset>>().apply {
            fun update() {
                val titles = titleLiveData.value ?: return
                val actions = actionLiveData.value ?: return

                val assets = actions.map { it.toActionEntity() } // TODO ActionRepository.loadDeposits, loadWithdraws, etc.
                val buyPairs = assets.filter { it.buyTitle != null }.map { Pair(it.buyTitle!!, it.buyAmount!!) }
                val sellPairs = assets.filter { it.sellTitle != null }.map { Pair(it.sellTitle!!, it.sellAmount!!.unaryMinus()) }
                val allPairs = buyPairs + sellPairs

                value = allPairs.groupBy { it.first }
                        .map { Pair(it.key, it.value.map { it.second }.reduce { x, y -> x + y }) }
                        .map {
                            Asset(
                                    title = it.first,
                                    amount = it.second,
                                    btcValue = it.first.priceBtc!!.toBigDecimal().times(it.second),
                                    usdValue = it.first.priceUsd!!.toBigDecimal().times(it.second))
                        }
            }

            addSource(titleLiveData) { update() }
            addSource(actionLiveData) { update() }

            update()
        }
    }


    suspend fun loadAllAssets(): List<Asset> {
        val assets = ActionRepository.loadAllActions().map { it.toActionEntity() } // TODO ActionRepository.loadDeposits, loadWithdraws, etc.
        val buyPairs = assets.filter { it.buyTitle != null }.map { Pair(it.buyTitle!!, it.buyAmount!!) }
        val sellPairs = assets.filter { it.sellTitle != null }.map { Pair(it.sellTitle!!, it.sellAmount!!.unaryMinus()) }
        val allPairs = buyPairs + sellPairs
        return allPairs.groupBy { it.first }
                .map { Pair(it.key, it.value.map { it.second }.reduce { x, y -> x + y }) }
                .map {
                    Asset(
                            title = it.first,
                            amount = it.second,
                            btcValue = it.first.priceBtc!!.toBigDecimal().times(it.second),
                            usdValue = it.first.priceUsd!!.toBigDecimal().times(it.second))
                }
    }

    suspend fun loadAsset(title: Title): Asset {
        val actions = ActionRepository.loadActions(title.symbol).map { it.toActionEntity() } // TODO ActionRepository.loadDeposits, loadWithdraws, etc.
        val buyActions = actions.filter { it.buyTitle?.symbol == title.symbol }.map { it.buyAmount!! }
        val sellActions = actions.filter { it.sellTitle?.symbol == title.symbol }.map { it.sellAmount!!.unaryMinus() }
        val allActions = buyActions + sellActions
        val amount = allActions.reduce { x, y -> x.add(y) }
        return Asset(
                title = title,
                amount = amount,
                btcValue = title.priceBtc!!.toBigDecimal().times(amount),
                usdValue = title.priceUsd!!.toBigDecimal().times(amount))
    }
}