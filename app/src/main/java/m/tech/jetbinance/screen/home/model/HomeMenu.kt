package m.tech.jetbinance.screen.home.model

import androidx.annotation.DrawableRes
import m.tech.jetbinance.R

data class HomeMenu(
    val id: Int,
    @DrawableRes val icon: Int,
    val content: String,
    val showEdit: Boolean
) {

    companion object {
        fun generateDemoData() = listOf(
            HomeMenu(1, R.drawable.ic_deposit, "Deposit", false),
            HomeMenu(2, R.drawable.ic_refferal, "Referral", false),
            HomeMenu(3, R.drawable.ic_strategy_trading, "Strategy Trading", false),
            HomeMenu(4, R.drawable.ic_launchpad, "Launchpad", false),
            HomeMenu(5, R.drawable.ic_farming, "Liquidity Farming", false),
            HomeMenu(6, R.drawable.ic_defi_staking, "DeFi Staking", false),
            HomeMenu(7, R.drawable.ic_simple_earn, "Simple Earn", false),
            HomeMenu(8, R.drawable.ic_menu_more, "More", true),
        )
    }

}