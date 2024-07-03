package m.tech.jetbinance.model

data class CoinPair(
    val id: String,
    val baseAsset: String,
    val quoteAsset: String,
    val price: String,
    val changesIn24h: String,
    val volUsd: String,
) {

}