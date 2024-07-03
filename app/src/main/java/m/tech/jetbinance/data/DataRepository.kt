package m.tech.jetbinance.data

import kotlinx.coroutines.flow.Flow
import m.tech.jetbinance.model.CoinPair

interface DataRepository {

    fun favoriteCoinsFlow(): Flow<List<CoinPair>>

    fun hotCoinsFlow(): Flow<List<CoinPair>>

    fun newListingCoinsFlow(): Flow<List<CoinPair>>

    fun gainerCoinsFlow(): Flow<List<CoinPair>>

    fun loserCoinsFlow(): Flow<List<CoinPair>>

    fun vol24hCoinsFlow(): Flow<List<CoinPair>>

}