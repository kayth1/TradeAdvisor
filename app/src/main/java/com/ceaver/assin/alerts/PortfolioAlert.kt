package com.ceaver.assin.alerts

import com.ceaver.assin.R
import com.ceaver.assin.assets.AssetRepository
import com.ceaver.assin.extensions.asCurrencyString
import com.ceaver.assin.markets.Title
import com.ceaver.assin.markets.TitleRepository
import org.apache.commons.csv.CSVRecord
import java.math.BigDecimal

data class PortfolioAlert(
        override val id: Long = 0,
        override val active: Boolean,
        override val quoteTitle: Title,
        override val last: BigDecimal,
        override val target: BigDecimal,
        override val diff: BigDecimal?
) : Alert {

    companion object Factory {
        fun fromDto(alertDto: AlertDto): PortfolioAlert {
            require(AlertType.PORTFOLIO == alertDto.alert.type)
            return PortfolioAlert(
                    id = alertDto.alert.id,
                    active = alertDto.alert.active,
                    quoteTitle = alertDto.quoteTitle!!.toTitle(),
                    last = alertDto.alert.last,
                    target = alertDto.alert.target,
                    diff = alertDto.alert.diff
            )
        }

        suspend fun fromImport(csvRecord: CSVRecord): PortfolioAlert {
            require(AlertType.PORTFOLIO.name == csvRecord.get(0))
            return PortfolioAlert(
                    active = csvRecord.get(1).toBoolean(),
                    quoteTitle = TitleRepository.loadById(csvRecord.get(2)),
                    last = csvRecord.get(3).toBigDecimal(),
                    target = csvRecord.get(4).toBigDecimal(),
                    diff = csvRecord.get(5).toBigDecimalOrNull()
            )
        }
    }

    override fun toEntity(): AlertEntity {
        return AlertEntity(
                id = id,
                type = AlertType.PORTFOLIO,
                active = active,
                quoteTitleId = quoteTitle.id,
                last = last,
                target = target,
                diff = diff
        )
    }

    override fun toExport(): List<String> {
        return listOf(
                AlertType.PORTFOLIO.name,
                active.toString(),
                quoteTitle.id,
                last.toPlainString(),
                target.toPlainString(),
                diff?.toPlainString() ?: ""
        )
    }

    override suspend fun lookupCurrent(): BigDecimal = AssetRepository.loadAssetOverview().lookupValue(quoteTitle)

    override fun getBaseImageResource(): Int = R.drawable.polis
    override fun getQuoteImageResource(): Int = quoteTitle.getIcon()

    override fun getNotificationTitle(direction: String): String = "Portfolio $direction"
    override fun getNotificationContent(target: BigDecimal): String = "Target of ${target.asCurrencyString(quoteTitle)} reached."

    override fun getBaseText(): String = getAlertType()
    override fun getAlertType(): String = "Portfolio"

    override fun copyWithCurrent(current: BigDecimal): Alert = copy(last = current)
    override fun copyWithCurrentAndDeactivated(current: BigDecimal): Alert = copy(last = current, active = false)
    override fun copyWithCurrentAndTarget(current: BigDecimal, target: BigDecimal): Alert = copy(last = current, target = target)
}