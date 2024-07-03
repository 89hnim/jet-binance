package m.tech.jetbinance.screen.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import m.tech.jetbinance.data.FakeDataRepository
import m.tech.jetbinance.data.fake.FakeDataSource
import m.tech.jetbinance.model.CoinPair
import m.tech.jetbinance.screen.home.HomeScreenEvent
import m.tech.jetbinance.screen.home.SearchWidgetState
import m.tech.jetbinance.screen.home.model.FeedReact
import m.tech.jetbinance.screen.home.model.viewdata.CoinPairViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeBannerViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeFeaturedFeatureViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeFeedHeaderViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeFeedPostViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeListCoinViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeMenuViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeNewTabViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeNotificationViewData
import m.tech.jetbinance.screen.home.model.viewdata.HomeViewData

class HomeViewModel : ViewModel() {

    private val repository = FakeDataRepository(FakeDataSource())
    private val data = arrayListOf(
        HomeBannerViewData(),
        HomeNotificationViewData(),
        HomeMenuViewData(),
        HomeFeaturedFeatureViewData(),
        HomeListCoinViewData(),
        HomeFeedHeaderViewData(),
        HomeNewTabViewData()
    ).apply {
        addAll(HomeFeedPostViewData.fakePostData())
    }

    private val _searchWidgetStateFlow = MutableStateFlow(SearchWidgetState.Closed)
    val searchWidgetStateFlow: StateFlow<SearchWidgetState>
        get() = _searchWidgetStateFlow

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow: StateFlow<String>
        get() = _searchTextFlow

    private val _currentFeedTabStateFlow = MutableStateFlow(0)
    val currentFeedTabStateFlow: StateFlow<Int>
        get() = _currentFeedTabStateFlow

    private val _homeViewDataStateFlow = MutableStateFlow(data.toMutableList())
    val homeViewDataStateFlow: StateFlow<List<HomeViewData>>
        get() = _homeViewDataStateFlow

    private val _viewEvent = MutableSharedFlow<HomeScreenEvent>(replay = 1)

    val favoriteCoinsStateFlow = repository.favoriteCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )

    val hotCoinsStateFlow = repository.hotCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )

    val gainerCoinsStateFlow = repository.gainerCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )

    val loserCoinsStateFlow = repository.loserCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )

    val newListingsCoinStateFlow = repository.newListingCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )

    val vol24hCoinStateFlow = repository.vol24hCoinsFlow()
        .map { it.mapToViewData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeListCoinViewData.loadingCoinPairs
        )
    private var reactingPostId: String? = null
    var lastPostIndexAndOffset = -1 to -1

    init {
        viewModelScope.launch {
            _viewEvent.collect { viewEvent ->
                Log.e("DSK", "called: $viewEvent")
                when (viewEvent) {
                    HomeScreenEvent.ClickSearch -> {
                        _searchWidgetStateFlow.value = SearchWidgetState.Opened
                    }
                    HomeScreenEvent.CloseSearch -> {
                        _searchTextFlow.value = ""
                        _searchWidgetStateFlow.value = SearchWidgetState.Closed
                    }
                    is HomeScreenEvent.TypingText -> {
                        _searchTextFlow.value = viewEvent.text
                    }
                    is HomeScreenEvent.Search -> {
                        Log.e("DSK", "searching: $viewEvent")
                    }
                    is HomeScreenEvent.SelectFeedTab -> {
                        if (viewEvent.tab != _currentFeedTabStateFlow.value) {
                            if (viewEvent.tab != 0) {
                                // save last post index to revert when user click tab post again
                                lastPostIndexAndOffset = viewEvent.lastPostIndexAndOffset
                            }
                            _currentFeedTabStateFlow.value = viewEvent.tab
                        }
                    }
                    is HomeScreenEvent.OpenReact -> {
                        reactingPostId = viewEvent.postId
                    }
                    is HomeScreenEvent.SelectReact -> {
                        val reactingPostId = this@HomeViewModel.reactingPostId
                        if (reactingPostId != null) {
                            toggleReact(reactingPostId, viewEvent.react)
                            this@HomeViewModel.reactingPostId = null
                        }
                    }
                    is HomeScreenEvent.LikePost -> {
                        val currentReact =
                            (data.firstOrNull { it.itemId == viewEvent.postId } as? HomeFeedPostViewData)?.reactType
                        toggleReact(
                            viewEvent.postId,
                            if (currentReact != FeedReact.None) -1 else 0
                        )
                    }
                    HomeScreenEvent.ResetPostPosition -> {
                        lastPostIndexAndOffset = -1 to -1
                    }
                }
            }
        }
    }

    private fun toggleReact(postId: String, react: Int) {
        val index = data.indexOfFirst {
            it is HomeFeedPostViewData && it.itemId == postId
        }

        if (index != -1) {
            val post = data.removeAt(index) as HomeFeedPostViewData
            val feedReact = when (react) {
                0 -> FeedReact.Like
                1 -> FeedReact.Laugh
                2 -> FeedReact.Love
                3 -> FeedReact.Cheer
                4 -> FeedReact.Rocket
                else -> FeedReact.None
            }

            val newPost = post.copy(
                reactType = if (feedReact == post.reactType) {
                    FeedReact.None
                } else {
                    feedReact
                }
            )

            data.add(index, newPost)
            updateHomeViewData()
        }
    }

    private fun updateHomeViewData() {
        _homeViewDataStateFlow.value = data.toMutableList()
    }

    fun fireEvent(event: HomeScreenEvent) {
        _viewEvent.tryEmit(event)
    }

    private fun CoinPair.mapToViewData() =
        CoinPairViewData(id, baseAsset, quoteAsset, price, changesIn24h, volUsd)

    private fun List<CoinPair>.mapToViewData() = map { it.mapToViewData() }

}