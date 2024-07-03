package m.tech.jetbinance.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import m.tech.jetbinance.screen.core.ChartData
import m.tech.jetbinance.screen.core.JetChart
import m.tech.jetbinance.ui.theme.JetColor
import okhttp3.internal.http2.Header

@Composable
fun DetailScreen(id: String, navController: NavController) {
    Log.e("DSK", "DetailScreen: $id")
    val screenWidth =
        with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JetColor.surface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = JetColor.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 48.dp),
                text = "BTC/USDT",
                style = MaterialTheme.typography.titleMedium.copy(color = JetColor.text_primary),
                textAlign = TextAlign.Center
            )
        }
        Header()
        Box(modifier = Modifier.size(width =screenWidth, height = 300.dp).background(Color.DarkGray)) {
            JetChart(
                width = screenWidth, height = 300.dp, widthOfCandle = 16.dp, chartData = listOf(
                    ChartData(10f, 2f, 0f, 4f),
                    ChartData(12f, 10f, 4f, 11f),
                    ChartData(16f, 12f, 11f, 13f),
                    ChartData(20f, 15f, 13f, 19f),
                    ChartData(5f, 2f, 19f, 3f),
                    ChartData(4f, 1f, 3f, 3f),
                )
            )
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "28,462.43",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = JetColor.accentVariant,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "$28,473.82",
                    style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "+1,07%",
                    style = MaterialTheme.typography.bodySmall.copy(color = JetColor.accentVariant),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                modifier = Modifier.padding(all = 1.dp),
                color = JetColor.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "POW",
                    style = MaterialTheme.typography.labelSmall.copy(color = JetColor.accent)
                )
            }
        }
        Column {
            Row {
                Column {
                    Text(
                        text = "24h High",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                    )
                    Text(
                        text = "28,775.00",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "24h Vol(BTC)",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                    )
                    Text(
                        text = "52,808.08",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column {
                    Text(
                        text = "24h Low",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                    )
                    Text(
                        text = "27,945.69",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "24h Vol(USDT)",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.onSurface)
                    )
                    Text(
                        text = "1.50B",
                        style = MaterialTheme.typography.bodySmall.copy(color = JetColor.text_primary)
                    )
                }
            }
        }
    }
}
