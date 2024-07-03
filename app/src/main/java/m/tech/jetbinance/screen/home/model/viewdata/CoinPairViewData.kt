package m.tech.jetbinance.screen.home.model.viewdata

data class CoinPairViewData(
    val id: String,
    val baseAsset: String,
    val quoteAsset: String,
    val price: String,
    val changesIn24h: String,
    val volUsd: String,
) {

    val isPositiveChange: Boolean = changesIn24h.startsWith("-").not()

    val isShowVol: Boolean = volUsd.isNotEmpty()

}