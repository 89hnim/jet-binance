package m.tech.jetbinance.screen.home.model.viewdata

import m.tech.jetbinance.screen.home.model.FeedReact

data class HomeFeedPostViewData(
    override val itemId: String,
    val postTitle: String,
    val postAuthorAvatar: String,
    val postAuthorName: String,
    val createdAt: String,
    val content: String,
    val hashtags: List<Pair<Int, Int>>,
    val featuredCoins: List<Triple<String, String, Boolean>>,
    val imageUrl: String,
    val reactType: FeedReact = FeedReact.None
) : HomeViewData {
    override val contentType: String
        get() = "HomeFeedPostViewDataContentType"

    val hasTitle = postTitle.isNotEmpty()

    companion object {
        fun fakePostData(): List<HomeFeedPostViewData> {
            return listOf(
                HomeFeedPostViewData(
                    itemId = "post_id_1",
                    postTitle = "Binance NFT: Understanding About Initial Game Offering (IGO)",
                    postAuthorAvatar = "https://i.pinimg.com/originals/ab/6f/a4/ab6fa471d46fa8260faffbcd7b013212.jpg",
                    postAuthorName = "Cryptomoonlight_F",
                    createdAt = "5 mins ago",
                    content = "Binance NFT has offically launched an IGO or initial game offering on Octobor 26, 2021 with the aim of building a launch for NFT games and gamer and Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    hashtags = emptyList(),
                    featuredCoins = listOf(
                        Triple("AUCTION", "+1.84%", true),
                        Triple("FOR", "+0.72%", true),
                    ),
                    imageUrl = "",
                    reactType = FeedReact.None
                ),
                HomeFeedPostViewData(
                    itemId = "post_id_2",
                    postTitle = "Building trust in the Crypto Ecosystem: A Framework For Centralized Exchanges",
                    postAuthorAvatar = "https://public.bnbstatic.com/20190405/eb2349c3-b2f8-4a93-a286-8f86a62ea9d8.png",
                    postAuthorName = "Binance Blog",
                    createdAt = "Feb 16",
                    content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book",
                    hashtags = emptyList(),
                    featuredCoins = listOf(
                        Triple("BAR", "-10.84%", false),
                        Triple("FOR", "-0.72%", false),
                        Triple("FRONT", "-3.72%", false),
                    ),
                    imageUrl = "https://public.bnbstatic.com/image/cms/blog/20200731/6b50144e-7478-4339-9242-ff48aff82625.png",
                    reactType = FeedReact.Like
                ),
                HomeFeedPostViewData(
                    itemId = "post_id_3",
                    postTitle = "",
                    postAuthorAvatar = "https://i.pinimg.com/originals/53/f0/3f/53f03f7f0c5424090eca8db6a861f978.jpg",
                    postAuthorName = "Phoenix Group",
                    createdAt = "Feb 15",
                    content = "LIQUIDATION DATA IN 24H\n\nTOTAL LIQUIDATIONS UP TO $178.42M\nTOP 5 COINS WITH HIGHEST #liquidation:\n\$BTC - \$77.18M\n\$ETH - \$40.18M\n\n#Blockchain #DeFi",
                    hashtags = listOf(
                        84 to 96, 129 to 140, 141 to 146
                    ),
                    featuredCoins = listOf(
                        Triple("BTC", "-10.84%", false),
                        Triple("ETH", "0.72%", true),
                        Triple("MATIC", "-3.72%", false),
                        Triple("SAND", "3.6%", true),
                        Triple("ONE", "10.22%", true),
                        Triple("AVAX", "-14.72%", false),
                    ),
                    imageUrl = "https://i.dawn.com/primary/2022/12/6399523a9b08a.jpg",
                    reactType = FeedReact.Laugh
                ),
            )
        }
    }
}
