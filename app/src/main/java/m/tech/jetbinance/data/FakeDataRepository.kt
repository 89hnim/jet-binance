package m.tech.jetbinance.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import m.tech.jetbinance.data.fake.FakeDataSource
import m.tech.jetbinance.model.CoinPair

class FakeDataRepository(
    private val fakeDataSource: FakeDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): DataRepository {

    override fun favoriteCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generateFavoriteCoins())
            delay(2000)
        }
    }.flowOn(dispatcher)

    override fun hotCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generateHotCoins())
            delay(2000)
        }
    }.flowOn(dispatcher)

    override fun newListingCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generateNewListingCoins())
            delay(2000)
        }
    }.flowOn(dispatcher)

    override fun gainerCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generateGainerCoins().sortedByDescending { it.changesIn24h })
            delay(3000)
        }
    }.flowOn(dispatcher)

    override fun loserCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generateLoserCoins().sortedByDescending { it.changesIn24h })
            delay(3000)
        }
    }.flowOn(dispatcher)

    override fun vol24hCoinsFlow(): Flow<List<CoinPair>> = flow {
        while (true) {
            emit(fakeDataSource.generate24hVolCoins())
            delay(5000)
        }
    }.flowOn(dispatcher)

}