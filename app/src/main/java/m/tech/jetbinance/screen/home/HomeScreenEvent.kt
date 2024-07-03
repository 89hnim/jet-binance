package m.tech.jetbinance.screen.home

sealed class HomeScreenEvent {
    // click search bar
    object ClickSearch : HomeScreenEvent()

    // close search bar
    object CloseSearch : HomeScreenEvent()

    // type text in search bar
    data class TypingText(val text: String) : HomeScreenEvent()

    // action search
    data class Search(val text: String) : HomeScreenEvent()

    // select tab discover - news
    data class SelectFeedTab(val tab: Int, val lastPostIndexAndOffset: Pair<Int, Int>) :
        HomeScreenEvent()

    // reset post position in tab discover when reselect discover tab
    object ResetPostPosition : HomeScreenEvent()

    // open react dialog when long click in react rect
    data class OpenReact(val postId: String) : HomeScreenEvent()

    // select a react in react dialog
    data class SelectReact(val react: Int) : HomeScreenEvent()

    // click like post, like or unlike
    data class LikePost(val postId: String) : HomeScreenEvent()
}