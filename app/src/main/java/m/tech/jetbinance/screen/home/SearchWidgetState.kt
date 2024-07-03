package m.tech.jetbinance.screen.home

enum class SearchWidgetState {
    Opened,
    Closed;

    val isOpened: Boolean
        get() = this == Opened
}