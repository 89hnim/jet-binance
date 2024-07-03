package m.tech.jetbinance.data.fake

import m.tech.jetbinance.model.CoinPair
import java.lang.StrictMath.abs
import java.text.DecimalFormat
import kotlin.random.Random

class FakeDataSource {

    private val formatPrice = DecimalFormat("#,###.####")
    private val formatPercent = DecimalFormat("#.##")

    fun generateFavoriteCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "BTC",
                "USDT",
                (24026.0).toPrice(),
                "${(16.04).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-1",
                "ONE",
                "USDT",
                (0.0021).toPrice(),
                "${(-2.41).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-2",
                "XTZ",
                "USDT",
                (1.11).toPrice(),
                "${(6.69).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-3",
                "NEAR",
                "USDT",
                (2.42).toPrice(),
                "${(17.08).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-4",
                "SAND",
                "USDT",
                (0.66).toPrice(),
                "${(-1.43).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-5",
                "DOT",
                "USDT",
                (6.9).toPrice(),
                "${(-0.43).toPercent()}%",
                ""
            ),
        )
    }

    fun generateHotCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "BNB",
                "USDT",
                (319.3).toPrice(),
                "${(5.27).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-1",
                "BTC",
                "USDT",
                (24.467).toPrice(),
                "${(-4.41).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-2",
                "ETH",
                "USDT",
                (1.685).toPrice(),
                "${(7.69).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-3",
                "MATIC",
                "USDT",
                (1.3831).toPrice(),
                "${(9.08).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-4",
                "SHIB",
                "USDT",
                (0.66).toPrice(),
                "${(-1.43).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-5",
                "CFX",
                "USDT",
                (0.14).toPrice(),
                "${(74.2).toPercent()}%",
                ""
            ),
        )
    }

    fun generateNewListingCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "APT",
                "USDT",
                (16.3).toPrice(),
                "${(9.27).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-1",
                "GMX",
                "USDT",
                (83.46).toPrice(),
                "${(-4.41).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-2",
                "HFT",
                "USDT",
                (0.685).toPrice(),
                "${(7.69).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-3",
                "HOOK",
                "USDT",
                (2.41).toPrice(),
                "${(9.08).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-4",
                "LEVER",
                "USDT",
                (0.006).toPrice(),
                "${(-1.43).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-5",
                "MAGIC",
                "USDT",
                (2.14).toPrice(),
                "${(74.2).toPercent()}%",
                ""
            ),
        )
    }

    fun generateGainerCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "DREP",
                "USDT",
                (16.3).toPrice(),
                "${(29.27).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-1",
                "CFX",
                "USDT",
                (83.46).toPrice(),
                "${(20.41).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-2",
                "IRIS",
                "USDT",
                (0.685).toPrice(),
                "${(25.69).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-3",
                "KEY",
                "USDT",
                (2.41).toPrice(),
                "${(16.08).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-4",
                "TRU",
                "USDT",
                (0.006).toPrice(),
                "${(12.43).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-5",
                "ACH",
                "USDT",
                (2.14).toPrice(),
                "${(13.2).toPercent()}%",
                ""
            ),
        )
    }

    fun generateLoserCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "RIF",
                "USDT",
                (16.3).toPrice(),
                "${(-29.27).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-1",
                "PIVX",
                "USDT",
                (83.46).toPrice(),
                "${(-20.41).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-2",
                "TORN",
                "USDT",
                (0.685).toPrice(),
                "${(-25.69).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-3",
                "GRT",
                "USDT",
                (2.41).toPrice(),
                "${(-16.08).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-4",
                "SYS",
                "USDT",
                (0.006).toPrice(),
                "${(-12.43).toPercent()}%",
                ""
            ),
            CoinPair(
                "id-5",
                "IMX",
                "USDT",
                (2.14).toPrice(),
                "${(-70.2).toPercent()}%",
                ""
            ),
        )
    }

    fun generate24hVolCoins(): List<CoinPair> {
        return listOf(
            CoinPair(
                "id-0",
                "BTC",
                "USDT",
                (24.467).toPrice(),
                "${(-4.41).toPercent()}%",
                "4.30B"
            ),
            CoinPair(
                "id-1",
                "ETH",
                "USDT",
                (1.685).toPrice(),
                "${(7.69).toPercent()}%",
                "702.42M"
            ),
            CoinPair(
                "id-2",
                "MATIC",
                "USDT",
                (1.3831).toPrice(),
                "${(9.08).toPercent()}%",
                "210.0M"
            ),
            CoinPair(
                "id-3",
                "BNB",
                "USDT",
                (319.3).toPrice(),
                "${(5.27).toPercent()}%",
                "151.11M"
            ),
            CoinPair(
                "id-4",
                "SHIB",
                "USDT",
                (0.66).toPrice(),
                "${(-1.43).toPercent()}%",
                "115.06M"
            ),
            CoinPair(
                "id-5",
                "CFX",
                "USDT",
                (0.14).toPrice(),
                "${(74.2).toPercent()}%",
                "98.89M"
            ),
        )
    }

    private fun Double.toPrice(): String {
        var random = Random.nextDouble(-0.01, 0.02)
        if (random == 0.0) random = 1.0
        val price = formatPrice.format(abs(this + this * random))
        return if (price.length > 11) {
            price.substring(0, 11)
        } else {
            price
        }
    }

    private fun Double.toPercent(): String {
        var random = Random.nextDouble(-0.01, 1.001)
        if (random == 0.0) random = 1.0
        return formatPercent.format(this + this * random)
    }

}