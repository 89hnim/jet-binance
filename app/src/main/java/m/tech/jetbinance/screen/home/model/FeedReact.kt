package m.tech.jetbinance.screen.home.model

import androidx.annotation.DrawableRes
import m.tech.jetbinance.R

enum class FeedReact(@DrawableRes val icon: Int, val description: String) {
    None(R.drawable.ic_react_default, "Like"),
    Like(R.drawable.ic_react_like, "Like"),
    Love(R.drawable.ic_react_love, "Love"),
    Laugh(R.drawable.ic_react_laugh, "Laugh"),
    Cheer(R.drawable.ic_react_cheer, "Cheer"),
    Rocket(R.drawable.ic_react_rocket, "Rocket");

    val isDefault: Boolean
        get() = this == None
}