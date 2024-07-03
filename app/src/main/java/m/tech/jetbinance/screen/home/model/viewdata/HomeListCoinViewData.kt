package m.tech.jetbinance.screen.home.model.viewdata

class HomeListCoinViewData : HomeViewData {
    override val itemId: String
        get() = "HomeListCoinViewDataItemId"
    override val contentType: String
        get() = "HomeListCoinViewDataContentType"

    companion object {
        val loadingCoinPairs = listOf(
            CoinPairViewData("id-loading-cp-1", "-", "-", "-", "0%", "0.0"),
            CoinPairViewData("id-loading-cp-2", "-", "-", "-", "0%", "0.0"),
            CoinPairViewData("id-loading-cp-3", "-", "-", "-", "0%", "0.0"),
            CoinPairViewData("id-loading-cp-4", "-", "-", "-", "0%", "0.0"),
            CoinPairViewData("id-loading-cp-5", "-", "-", "-", "0%", "0.0"),
            CoinPairViewData("id-loading-cp-6", "-", "-", "-", "0%", "0.0"),
        )
    }
}