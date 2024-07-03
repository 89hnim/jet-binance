package m.tech.jetbinance.screen.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import m.tech.jetbinance.R
import m.tech.jetbinance.base.JetBinanceSearchBar
import m.tech.jetbinance.screen.core.ReactPopup
import m.tech.jetbinance.screen.core.SocialText
import m.tech.jetbinance.screen.core.isScrollingUp
import m.tech.jetbinance.screen.home.model.FeedReact
import m.tech.jetbinance.screen.home.model.HomeMenu
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
import m.tech.jetbinance.screen.home.viewmodel.HomeViewModel
import m.tech.jetbinance.ui.theme.JetColor

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = HomeViewModel(),
    listState: LazyListState = rememberLazyListState(),
    onNavigateToDetail: (id: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val searchWidgetState by homeViewModel.searchWidgetStateFlow.collectAsState()
    val searchText by homeViewModel.searchTextFlow.collectAsState()
    val homeData by homeViewModel.homeViewDataStateFlow.collectAsState()
    val currentFeedTab by homeViewModel.currentFeedTabStateFlow.collectAsState()
    val showFeedNotificationIcon by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex >= HomeViewData.indexOfFeedHeader
        }
    }

    var popupControl by remember { mutableStateOf(false) }
    var popupViewRect by remember { mutableStateOf(Rect(Offset(0f, 0f), Offset(0f, 0f))) }

    Scaffold(
        modifier = Modifier.background(JetColor.surface),
        topBar = {
            HomeAppBar(
                searchWidgetState = searchWidgetState,
                searchText = searchText,
                onTextChange = {
                    homeViewModel.fireEvent(HomeScreenEvent.TypingText(it))
                },
                onCloseClicked = {
                    homeViewModel.fireEvent(HomeScreenEvent.CloseSearch)
                },
                onSearch = {
                    homeViewModel.fireEvent(HomeScreenEvent.Search(it))
                },
                onSearchClear = {
                    homeViewModel.fireEvent(HomeScreenEvent.TypingText(""))
                },
                onSearchTriggered = {
                    homeViewModel.fireEvent(HomeScreenEvent.ClickSearch)
                },
            )
        },
        floatingActionButton = {
            Column {
                Spacer(modifier = Modifier.size(0.5.dp))
                AnimatedVisibility(
                    visible = showFeedNotificationIcon,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    SmallFloatingActionButton(
                        onClick = { },
                        shape = CircleShape,
                        containerColor = JetColor.accent,
                        elevation = FloatingActionButtonDefaults.elevation(),
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_notification_feed),
                            contentDescription = "Notification",
                            tint = JetColor.surface,
                        )
                    }
                }
                AnimatedVisibility(
                    visible = listState.isScrollingUp(showFeedNotificationIcon),
                ) {
                    SmallFloatingActionButton(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = { coroutineScope.launch { listState.scrollToItem(0) } },
                        shape = CircleShape,
                        containerColor = Color(0xFF434C5B),
                        elevation = FloatingActionButtonDefaults.elevation(),
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_arrow_up),
                            contentDescription = "Move to top",
                            tint = JetColor.text_primary,
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        HomeLazyList(
            listState = listState,
            data = homeData,
            homeViewModel = homeViewModel,
            paddingValues = paddingValues,
            currentFeedTab = currentFeedTab,
            onLikePost = { post ->
                homeViewModel.fireEvent(HomeScreenEvent.LikePost(post.itemId))
            },
            onSelectReactPost = { post, viewRect ->
                popupControl = !popupControl
                popupViewRect = viewRect
                homeViewModel.fireEvent(HomeScreenEvent.OpenReact(post.itemId))
            },
            onSelectFeedTab = { index ->
                homeViewModel.fireEvent(
                    HomeScreenEvent.SelectFeedTab(
                        index,
                        listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
                    )
                )
            },
            onNavigateToDetail = onNavigateToDetail
        )
        if (popupControl) {
            ReactPopup(
                popupViewRect = popupViewRect,
                onDismissRequest = { popupControl = false },
                onReactSelected = { react ->
                    popupControl = false
                    homeViewModel.fireEvent(HomeScreenEvent.SelectReact(react))
                }
            )
        }
    }
}

@Composable
private fun HomeAppBar(
    searchWidgetState: SearchWidgetState,
    searchText: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearch: (query: String) -> Unit,
    onSearchClear: () -> Unit,
    onSearchTriggered: () -> Unit
) {
    Box {
        AnimatedVisibility(
            visible = searchWidgetState.isOpened, enter = scaleIn(), exit = fadeOut()
        ) {
            JetBinanceSearchBar(
                text = searchText,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearch,
                onSearchClear = onSearchClear
            )
        }
        if (!searchWidgetState.isOpened) {
            HomeDefaultAppBar(onSearchClicked = onSearchTriggered)
        }
    }

}

@Composable
private fun HomeDefaultAppBar(onSearchClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_avatar),
            contentDescription = "person",
            modifier = Modifier
                .padding(end = 12.dp)
                .clip(CircleShape)
                .background(color = JetColor.surfaceVariant),
            tint = Color.Unspecified
        )
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(weight = 1f)
                .height(36.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = JetColor.surfaceVariant)
                .padding(horizontal = 8.dp)
                .clickable {
                    onSearchClicked()
                }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "person",
                modifier = Modifier.size(16.dp),
                tint = JetColor.onSurfaceVariant
            )
            Text(
                text = "Search",
                color = JetColor.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_scan_qr),
            contentDescription = "person",
            tint = JetColor.onSurfaceVariant,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .size(24.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_support),
            contentDescription = "person",
            tint = JetColor.onSurfaceVariant,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
        )
        Box(Modifier.padding(end = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "person",
                tint = JetColor.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Text(
                modifier = Modifier
                    .offset(x = 8.dp, y = ((-4).dp))
                    .clip(RoundedCornerShape(size = 8.dp))
                    .background(JetColor.accent)
                    .padding(horizontal = 2.dp, vertical = 1.dp)
                    .align(Alignment.TopEnd),
                text = "99+",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = JetColor.text_primary_inverse, fontSize = 9.sp
                ),
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_random),
            contentDescription = "person",
            tint = JetColor.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun HomeLazyList(
    listState: LazyListState,
    data: List<HomeViewData>,
    homeViewModel: HomeViewModel,
    currentFeedTab: Int,
    paddingValues: PaddingValues,
    onLikePost: (post: HomeFeedPostViewData) -> Unit,
    onSelectReactPost: (post: HomeFeedPostViewData, viewRect: Rect) -> Unit,
    onSelectFeedTab: (tab: Int) -> Unit,
    onNavigateToDetail: (id: String) -> Unit,
) {
    val showStickyHeader by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex >= HomeViewData.indexOfFeedHeader
        }
    }
    val isFeedPostTab = currentFeedTab == 0

    LaunchedEffect(isFeedPostTab) {
        if (isFeedPostTab && homeViewModel.lastPostIndexAndOffset.first != -1) {
            listState.scrollToItem(
                homeViewModel.lastPostIndexAndOffset.first,
                homeViewModel.lastPostIndexAndOffset.second
            )
            homeViewModel.fireEvent(HomeScreenEvent.ResetPostPosition)
        }
    }

    Box {
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
            state = listState
        ) {
            items(data, key = { it.itemId }, contentType = { it.contentType }) {
                when (it) {
                    is HomeBannerViewData -> {
                        HomeBanner(modifier = Modifier.padding(top = 12.dp))
                    }

                    is HomeNotificationViewData -> {
                        HomeNotification(modifier = Modifier.padding(top = 8.dp))
                    }

                    is HomeMenuViewData -> {
                        HomeGridMenu(modifier = Modifier.padding(top = 16.dp))
                    }

                    is HomeFeaturedFeatureViewData -> {
                        HomeFeaturedFeatures(modifier = Modifier.padding(top = 16.dp))
                    }

                    is HomeListCoinViewData -> {
                        HomeTabRow(
                            modifier = Modifier.padding(top = 24.dp),
                            homeViewModel,
                            onNavigateToDetail
                        )
                    }

                    is HomeFeedHeaderViewData -> {
                        HomeFeedHeader(
                            currentFeedTab = currentFeedTab,
                            onSelectFeedTab = onSelectFeedTab
                        )
                    }

                    is HomeFeedPostViewData -> {
                        if (isFeedPostTab) {
                            HomeFeedPost(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                post = it,
                                onLike = onLikePost,
                                onSelectReact = onSelectReactPost
                            )
                        }
                    }

                    is HomeNewTabViewData -> {
                        if (!isFeedPostTab) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "New tab placeholder",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = JetColor.accent,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
        if (showStickyHeader) {
            Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                HomeFeedHeader(currentFeedTab = currentFeedTab, onSelectFeedTab = onSelectFeedTab)
            }
        }
    }
}

@Composable
private fun HomeBanner(modifier: Modifier = Modifier) {
    val banners = remember {
        mutableStateListOf(
            R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4
        )
    }
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = {
        banners.size
    })
    var text by remember {
        mutableStateOf("1/${banners.size}")
    }
    LaunchedEffect(Unit) {
        while (true) {
            ensureActive()
            delay(5000)
            var nextPage = pagerState.currentPage + 1
            if (nextPage > 3) {
                nextPage = 0
                pagerState.scrollToPage(page = nextPage)
            } else {
                pagerState.animateScrollToPage(page = nextPage)
            }
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            text = "${page + 1}/${banners.size}"
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(12.dp))
    ) {
        HorizontalPager(
            state = pagerState
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(banners[page])
                    .crossfade(true).build(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "Banner $page"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0x991F2630))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary),
            )
        }

    }
}

@Composable
private fun HomeNotification(modifier: Modifier = Modifier) {
    val notifications = remember {
        mutableStateListOf(
            "What is Lorem Ipsum?",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " + "when an unknown printer took a galley of type " + "and scrambled it to make a type specimen book.",
            "It has survived not only five centuries, " + "but also the leap into electronic typesetting," + " remaining essentially unchanged.",
            "It was popularised in the 1960s with the release of Letraset sheets " + "containing Lorem Ipsum passages",
            "Why do we use it?"
        )
    }
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = {
        notifications.size
    })
    LaunchedEffect(Unit) {
        while (true) {
            ensureActive()
            delay(3000)
            var nextPage = pagerState.currentPage + 1
            if (nextPage > 3) {
                nextPage = 0
                pagerState.scrollToPage(page = nextPage)
            } else {
                pagerState.animateScrollToPage(page = nextPage)
            }
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        VerticalPager(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(weight = 1f)
                .height(32.dp),
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            Text(
                text = notifications[page], style = MaterialTheme.typography.bodySmall.copy(
                    color = JetColor.onSurface, fontWeight = FontWeight.Medium
                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(size = 8.dp))
                .background(color = Color(0xFF171E26))
                .clickable {

                }, contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker),
                contentDescription = "Notification icon",
                tint = JetColor.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun HomeGridMenu(modifier: Modifier = Modifier) {
    val list = remember { HomeMenu.generateDemoData() }
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(180.dp),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(list, key = { it.id }) { homeMenu ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    IconButton(
                        onClick = { }, modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (homeMenu.showEdit) {
                                    Color(0xFF34342C)
                                } else {
                                    JetColor.surfaceVariant
                                }
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = homeMenu.icon),
                            contentDescription = homeMenu.content,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                    if (homeMenu.showEdit) {
                        Icon(
                            modifier = Modifier
                                .offset(x = 6.dp)
                                .size(20.dp)
                                .border(
                                    border = BorderStroke(width = 2.dp, color = JetColor.surface),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                                .background(color = Color(0xFF434C5C))
                                .align(Alignment.BottomEnd)
                                .padding(all = 4.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = homeMenu.content,
                            tint = JetColor.text_primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = homeMenu.content,
                    style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HomeFeaturedFeatures(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        HomeCard(
            modifier = Modifier.weight(1f),
            title = "P2P Trading",
            body = "Bank Transfer, Digital Wallet Transfer, Mobile Payment and more",
            icon = R.drawable.ic_p2p
        )
        Spacer(modifier = Modifier.width(8.dp))
        HomeCard(
            modifier = Modifier.weight(1f),
            title = "Buy with VND",
            body = "Visa, Mastercard",
            icon = R.drawable.ic_card
        )
        Spacer(modifier = Modifier.width(8.dp))
        HomeCard(
            modifier = Modifier.weight(1f),
            title = "Deposit VND",
            body = "Multiple payment options",
            icon = R.drawable.ic_buy_vnd
        )
    }
}

@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    @DrawableRes icon: Int
) {
    Box(
        modifier = modifier
            .height(height = 100.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .clickable { }
            .drawBehind {
                val startBrush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8F8F8F),
                        Color(0xFF272f3b),
                        Color(0xFF1F2630)
                    )
                )
                val startTrianglePath = Path().apply {
                    lineTo(size.width, 0f)
                    lineTo(0f, size.height)
                    close()
                }
                val endTrianglePath = Path().apply {
                    moveTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(path = startTrianglePath, brush = startBrush)
                drawPath(path = endTrianglePath, color = Color(0xFF4E5257))
            }) {
        Image(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp, bottom = 4.dp),
            painter = painterResource(id = icon),
            contentDescription = "home card"
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xE01F2630))
                .heightIn(min = 120.dp)
        )
        Column(
            modifier = modifier
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(color = JetColor.text_primary)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = JetColor.onSurfaceVariant,
                    fontSize = 10.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HomeTabRow(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onNavigateToDetail: (id: String) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    val list = listOf(
        "Favorites", "Hot", "New Listings", "Gainers", "Losers", "24h Vol"
    )

    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = {
        list.size
    })
    Column {
        ScrollableTabRow(
            modifier = modifier,
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 16.dp,
            containerColor = JetColor.transparent,
            indicator = { tabPositions ->
                val transition = updateTransition(pagerState.currentPage, label = "")
                val indicatorStart by transition.animateDp(
                    transitionSpec = {
                        if (initialState < targetState) {
                            spring(stiffness = Spring.StiffnessVeryLow)
                        } else {
                            spring(stiffness = Spring.StiffnessMediumLow)
                        }
                    }, label = ""
                ) {
                    tabPositions[it].left
                }
                val indicatorEnd by transition.animateDp(
                    transitionSpec = {
                        if (initialState < targetState) {
                            spring(stiffness = Spring.StiffnessMediumLow)
                        } else {
                            spring(stiffness = Spring.StiffnessVeryLow)
                        }
                    }, label = ""
                ) {
                    tabPositions[it].right
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.BottomStart)
                        .offset(x = indicatorStart)
                        .width(indicatorEnd - indicatorStart)
                        .fillMaxSize()
                        .background(color = Color(0xFF434C5B), RoundedCornerShape(4.dp))
                        .zIndex(1f)
                )
            },
            divider = {}
        ) {
            list.forEachIndexed { index, title ->
                val selected = pagerState.currentPage == index
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = selected,
                    onClick = { coroutineScope.launch { pagerState.scrollToPage(index) } }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        text = title,
                        style = if (selected) {
                            MaterialTheme.typography.labelLarge.copy(
                                color = JetColor.text_primary,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            MaterialTheme.typography.labelLarge.copy(
                                color = JetColor.onSurface,
                                fontWeight = FontWeight.Normal
                            )
                        },
                    )

                }
            }

        }
        HorizontalPager(
            state = pagerState,
        ) { page ->
            val homeListCoinData by when (page) {
                HomeListCoinTab.favorites -> {
                    viewModel.favoriteCoinsStateFlow.collectAsState()
                }

                HomeListCoinTab.hot -> {
                    viewModel.hotCoinsStateFlow.collectAsState()
                }

                HomeListCoinTab.newListings -> {
                    viewModel.newListingsCoinStateFlow.collectAsState()
                }

                HomeListCoinTab.gainers -> {
                    viewModel.gainerCoinsStateFlow.collectAsState()
                }

                HomeListCoinTab.losers -> {
                    viewModel.loserCoinsStateFlow.collectAsState()
                }

                HomeListCoinTab.vol24h -> {
                    viewModel.vol24hCoinStateFlow.collectAsState()
                }

                else -> {
                    throw IllegalArgumentException("Page $page not supported")
                }
            }
            HomeListCoin(homeListCoinData, onNavigateToDetail)
        }
    }
}

private object HomeListCoinTab {
    const val favorites = 0
    const val hot = 1
    const val newListings = 2
    const val gainers = 3
    const val losers = 4
    const val vol24h = 5
}

@Composable
private fun HomeListCoin(
    list: List<CoinPairViewData>,
    onNavigateToDetail: (id: String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                modifier = Modifier.weight(2f),
                text = "Name",
                style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1.5f),
                text = "Last Price",
                style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface),
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = if (list.firstOrNull()?.isShowVol == true) "Vol(USD)" else "24h chg%",
                style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface),
                textAlign = TextAlign.End
            )
        }
        list.forEach {
            HomeCoinRow(coinPairViewData = it, onNavigateToDetail = onNavigateToDetail)
        }
        HorizontalDivider(color = JetColor.surfaceVariant)
    }
}

@Composable
private fun HomeCoinRow(
    coinPairViewData: CoinPairViewData,
    onNavigateToDetail: (id: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = JetColor.ripple),
                onClick = {
                    onNavigateToDetail(coinPairViewData.id)
                }
            )
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                modifier = Modifier.padding(end = 2.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.titleMedium
                            .copy(color = JetColor.text_primary)
                            .toSpanStyle()
                    ) {
                        append(coinPairViewData.baseAsset)
                    }
                    withStyle(
                        style = MaterialTheme.typography.labelSmall
                            .copy(color = JetColor.onSurface)
                            .toSpanStyle()
                    ) {
                        append("/")
                        append(coinPairViewData.quoteAsset)
                    }
                },
            )
            Surface(
                modifier = Modifier.padding(all = 1.dp),
                color = JetColor.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "5x",
                    style = MaterialTheme.typography.labelSmall.copy(color = JetColor.accent)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1.5f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = coinPairViewData.price,
                style = MaterialTheme.typography.labelLarge.copy(color = JetColor.text_primary)
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = "$${coinPairViewData.price}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = JetColor.onSurface,
                    fontSize = 10.sp
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(size = 4.dp))
                .background(
                    if (coinPairViewData.isShowVol) {
                        JetColor.transparent
                    } else if (coinPairViewData.isPositiveChange) {
                        JetColor.accentVariant
                    } else {
                        JetColor.error
                    }
                )
                .padding(vertical = 8.dp),
            text = if (coinPairViewData.isShowVol) {
                coinPairViewData.volUsd
            } else {
                coinPairViewData.changesIn24h
            },
            style = MaterialTheme.typography.labelLarge.copy(color = JetColor.text_primary),
            textAlign = if (coinPairViewData.isShowVol) TextAlign.End else TextAlign.Center
        )
    }
}

@Composable
private fun HomeFeedHeader(
    currentFeedTab: Int,
    onSelectFeedTab: (tab: Int) -> Unit
) {
    val list = listOf("Discover", "News")

    Column(
        modifier = Modifier
            .background(JetColor.surface)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = currentFeedTab,
            edgePadding = 0.dp,
            containerColor = JetColor.transparent,
            indicator = { tabPositions ->
                val transition = updateTransition(currentFeedTab, label = "")
                val indicatorStart by transition.animateDp(
                    transitionSpec = {
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    }, label = ""
                ) {
                    tabPositions[it].left + (tabPositions[it].width - 12.dp) * 0.25f
                }
                val indicatorEnd by transition.animateDp(
                    transitionSpec = {
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    }, label = ""
                ) {
                    tabPositions[it].left + (tabPositions[it].width - 12.dp) * 0.75f
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.BottomStart)
                        .height(3.dp)
                        .offset(x = indicatorStart)
                        .width(indicatorEnd - indicatorStart)
                        .background(color = JetColor.accent)
                )
            },
            divider = { }
        ) {
            list.forEachIndexed { index, title ->
                val selected = currentFeedTab == index
                Tab(
                    selected = selected,
                    onClick = { onSelectFeedTab(index) }
                ) {
                    Text(
                        modifier = Modifier.padding(end = 12.dp, bottom = 8.dp),
                        text = title,
                        style = if (selected) {
                            MaterialTheme.typography.titleLarge.copy(
                                color = JetColor.text_primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        } else {
                            MaterialTheme.typography.titleLarge.copy(
                                color = JetColor.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        },
                    )

                }
            }
        }
        ClickableText(
            modifier = Modifier.padding(vertical = 8.dp),
            text = buildAnnotatedString {
                append(
                    "Full disclaimer: This platform includes third party opinions. " +
                            "We do not endorse their accuracy. " +
                            "Digital asset prices can be volatile. " +
                            "Do your own research."
                )
                append(" ")
                withStyle(
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = JetColor.accent,
                        fontWeight = FontWeight.Normal
                    ).toSpanStyle(),
                ) {
                    append("See full terms here")
                }
            },
            style = MaterialTheme.typography.labelSmall.copy(
                color = JetColor.onSurface,
                fontWeight = FontWeight.Normal
            ),
            onClick = {
                // position of text terms
                if (it in 155..173) {
                    Log.e("DSK", "HomeFeedHeader: $it")
                }
            }
        )
    }
}

@Composable
private fun HomeFeedPost(
    modifier: Modifier = Modifier,
    post: HomeFeedPostViewData,
    onLike: (post: HomeFeedPostViewData) -> Unit,
    onSelectReact: (post: HomeFeedPostViewData, viewRect: Rect) -> Unit
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.postAuthorAvatar)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = "avatar"
            )
            Text(
                modifier = Modifier
                    .widthIn(max = 180.dp)
                    .padding(horizontal = 12.dp),
                text = post.postAuthorName,
                style = MaterialTheme.typography.titleSmall.copy(color = JetColor.text_primary),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = post.createdAt,
                style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Follow",
                style = MaterialTheme.typography.titleMedium.copy(color = JetColor.text_primary_inverse),
                modifier = Modifier
                    .background(JetColor.accent, RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .clickable { }
            )
        }
        if (post.hasTitle) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = post.postTitle,
                style = MaterialTheme.typography.titleMedium.copy(color = JetColor.text_primary)
            )
        }
        SocialText(
            modifier = Modifier.padding(top = 8.dp),
            text = post.content,
            style = MaterialTheme.typography.bodyMedium.copy(color = if (post.hasTitle) JetColor.onSurface else JetColor.text_primary),
            hashTags = post.hashtags,
            hashTagColor = JetColor.accent,
            maxLines = if (post.hasTitle) 3 else Int.MAX_VALUE
        )
        LazyRow(modifier = Modifier.padding(top = 12.dp), state = rememberLazyListState()) {
            items(post.featuredCoins) {
                val name = it.first
                val changePercent = it.second
                val isPositive = it.third

                Text(
                    modifier = Modifier
                        .background(color = JetColor.surface) // create padding between items
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = JetColor.surfaceVariant)
                        .clickable { }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            MaterialTheme.typography.labelSmall
                                .copy(color = JetColor.text_primary)
                                .toSpanStyle()
                        ) {
                            append("$name ")
                        }
                        withStyle(
                            MaterialTheme.typography.labelSmall
                                .copy(color = if (isPositive) JetColor.accentVariant else JetColor.error)
                                .toSpanStyle()
                        ) {
                            append(changePercent)
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }
        }

        if (post.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .aspectRatio(1 / 0.5f)
                    .clip(RoundedCornerShape(size = 12.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = "post image"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            HomeFeedPostReact(
                modifier = Modifier
                    .weight(1f),
                reaction = post.reactType,
                onLike = { onLike(post) },
                onSelectReact = { onSelectReact(post, it) }
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_comment),
                    contentDescription = "comment",
                    tint = JetColor.onSurfaceSecond
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Comment",
                    style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "share",
                    tint = JetColor.onSurfaceSecond
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Share",
                    style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                )
            }
        }

        HorizontalDivider(
            color = JetColor.surfaceVariant,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun HomeFeedPostReact(
    modifier: Modifier = Modifier,
    reaction: FeedReact,
    onLike: () -> Unit,
    onSelectReact: (viewRect: Rect) -> Unit
) {
    var currentRect by remember { mutableStateOf(Rect(Offset(0f, 0f), Offset(0f, 0f))) }

    Row(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onSelectReact(currentRect)
                    },
                    onTap = { onLike() }
                )
            }
            .onGloballyPositioned { layoutCoordinates ->
                currentRect = layoutCoordinates.boundsInRoot()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = reaction.icon),
            contentDescription = "react${reaction.description}",
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = reaction.description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (reaction.isDefault) JetColor.onSurface else JetColor.accent
            )
        )
    }
}
