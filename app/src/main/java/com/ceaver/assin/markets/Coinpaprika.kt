package com.ceaver.assin.markets

import com.ceaver.assin.MyApplication
import com.ceaver.assin.assets.AssetCategory
import com.ceaver.assin.markets.overview.MarketOverview
import com.coinpaprika.apiclient.api.CoinpaprikaApi
import com.coinpaprika.apiclient.entity.FiatEntity
import com.coinpaprika.apiclient.entity.GlobalStatsEntity
import com.coinpaprika.apiclient.entity.TickerEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.ZonedDateTime
import java.util.*

object Coinpaprika {

    fun loadGlobalStats(): MarketOverview {
        lateinit var marketOverview: MarketOverview
        CoinpaprikaApi(MyApplication.appContext!!)
                .global()
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        { marketOverview = transformGlobalStats(it!!) },
                        { error -> error.printStackTrace() }) // TODO
        return marketOverview
    }

    private fun transformGlobalStats(globalStatsEntity: GlobalStatsEntity): MarketOverview {
        return MarketOverview(
                marketCapUsd = globalStatsEntity.marketCapUsd,
                dailyMarketCapChange = globalStatsEntity.dailyMarketCapChange,
                marketCapAthValue = globalStatsEntity.marketCapAthValue,
                marketCapAthDate = globalStatsEntity.marketCapAthDate,
                dailyVolumeUsd = globalStatsEntity.dailyVolumeUsd,
                dailyVolumeChange = globalStatsEntity.dailyVolumeChange,
                volumeAthValue = globalStatsEntity.volumeAthValue,
                volumeAthDate = globalStatsEntity.volumeAthDate,
                btcDominancePercentage = globalStatsEntity.btcDominancePercentage,
                cryptocurrenciesAmount = globalStatsEntity.cryptocurrenciesAmount,
                lastUpdated = globalStatsEntity.lastUpdated
        )
    }

    fun load(id: String): Optional<Title> {
        var titleOptional: Optional<Title> = Optional.empty()
        CoinpaprikaApi(MyApplication.appContext!!)
                .ticker(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        { ticker -> titleOptional = Optional.of(transform(ticker)) },
                        { error -> error.printStackTrace() }) // TODO

        return titleOptional
    }

    fun loadAllTitles(): Set<Title> {
        val resultSet = mutableSetOf<Title>()

        CoinpaprikaApi(MyApplication.appContext!!)
                .fiats()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        { next ->
                            for (fiatEntity in next) {
                                resultSet.add(transform(fiatEntity))
                            }
                        },
                        { error -> error.printStackTrace() }) // TODO

        CoinpaprikaApi(MyApplication.appContext!!)
                .tickers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(
                        { next ->
                            for (ticker in next) {
                                resultSet.add(transform(ticker))
                            }
                        },
                        { error -> error.printStackTrace() }) // TODO
        return resultSet
    }

    private fun transform(fiatEntity: FiatEntity): Title {
        return Title(
                id = fiatEntity.id,
                name = fiatEntity.name,
                symbol = fiatEntity.symbol,
                category = AssetCategory.FIAT,
                active = 1818,
                priceUsd = 1.0 // TODO this is only valid for USD
        )
    }

    private fun transform(ticker: TickerEntity): Title {
        val usdQuotes = ticker.quotes!!.get("USD")!!
        val btcQuotes = ticker.quotes!!.get("BTC")!!
        val ethQuotes = ticker.quotes!!.get("ETH")!!

        return Title(
                id = ticker.id,
                name = ticker.name,
                symbol = ticker.symbol,
                category = AssetCategory.CRYPTO,
                active = 1818,

                rank = ticker.rank,
                circulatingSupply = ticker.circulatingSupply,
                totalSupply = ticker.totalSupply,
                maxSupply = ticker.maxSupply,
                betaValue = ticker.betaValue,
                lastUpdated = if (ticker.lastUpdated == null) null else transformTimestamp(ticker.lastUpdated!!),

                priceUsd = usdQuotes.price,
                volume24hUsd = usdQuotes.dailyVolume,
                marketCapUsd = usdQuotes.marketCap,
                marketCapChange24hUsd = usdQuotes.marketCapDailyChange,
                percentChange1hUsd = usdQuotes.percentChange1h,
                percentChange24hUsd = usdQuotes.percentChange24h,
                percentChange7dUsd = usdQuotes.percentChange7d,
                percentChange30dUsd = usdQuotes.percentChange30d,
                percentChange1yUsd = usdQuotes.percentChange1y,
                athPriceUsd = usdQuotes.athPrice,
                athDateUsd = if (usdQuotes.athDate == null) null else transformTimestamp(usdQuotes.athDate!!),
                athPercentUsd = usdQuotes.athPrice,

                priceBtc = btcQuotes.price,
                volume24hBtc = btcQuotes.dailyVolume,
                marketCapBtc = btcQuotes.marketCap,
                marketCapChange24hBtc = btcQuotes.marketCapDailyChange,
                percentChange1hBtc = btcQuotes.percentChange1h,
                percentChange24hBtc = btcQuotes.percentChange24h,
                percentChange7dBtc = btcQuotes.percentChange7d,
                percentChange30dBtc = btcQuotes.percentChange30d,
                percentChange1yBtc = btcQuotes.percentChange1y,
                athPriceBtc = btcQuotes.athPrice,
                athDateBtc = if (btcQuotes.athDate == null) null else transformTimestamp(btcQuotes.athDate!!),
                athPercentBtc = btcQuotes.athPrice,

                priceEth = ethQuotes.price,
                volume24hEth = ethQuotes.dailyVolume,
                marketCapEth = ethQuotes.marketCap,
                marketCapChange24hEth = ethQuotes.marketCapDailyChange,
                percentChange1hEth = ethQuotes.percentChange1h,
                percentChange24hEth = ethQuotes.percentChange24h,
                percentChange7dEth = ethQuotes.percentChange7d,
                percentChange30dEth = ethQuotes.percentChange30d,
                percentChange1yEth = ethQuotes.percentChange1y,
                athPriceEth = ethQuotes.athPrice,
                athDateEth = if (ethQuotes.athDate == null) null else transformTimestamp(ethQuotes.athDate!!),
                athPercentEth = ethQuotes.athPrice
        )
    }

    private fun transformTimestamp(lastUpdated: String) = ZonedDateTime.parse(lastUpdated).toLocalDateTime()
}