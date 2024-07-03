package m.tech.jetbinance.screen.core

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import m.tech.jetbinance.ui.theme.JetColor
import kotlin.math.abs

data class ChartData(
    val highestPrice: Float,
    val lowestPrice: Float,
    val previousPrice: Float,
    val currentPrice: Float,
)

@Composable
fun JetChart(
    width: Dp,
    height: Dp,
    widthOfCandle: Dp,
    chartData: List<ChartData>,
) {
    val (maxValue, minValue) = calculateBoundaryChart(chartData)
    val candleWidth = with(LocalDensity.current) { widthOfCandle.toPx() }
    val heightPx = with(LocalDensity.current) { height.toPx() }

    Log.e("DSK", "JetChart: $maxValue - $minValue")

    Box(
        modifier = Modifier
            .size(width, height)
            .drawBehind {
                chartData.forEachIndexed { index, chartData ->
                    drawCandle(
                        chartData,
                        index * candleWidth,
                        candleWidth,
                        minValue,
                        maxValue,
                        heightPx
                    )
                    drawMinMaxLine(
                        chartData,
                        index * candleWidth,
                        candleWidth,
                        minValue,
                        maxValue,
                        heightPx
                    )
                }
            }
    )
}

private fun DrawScope.drawMinMaxLine(
    chartData: ChartData,
    positionX: Float,
    candleWidth: Float,
    minValue: Float,
    maxValue: Float,
    chartHeight: Float
) {
    Log.e("DSK", "drawMinMaxLine: ${"-123".toInt()}", )
    val isHighPump = chartData.highestPrice > chartData.previousPrice
    val isLowPump = chartData.lowestPrice > chartData.previousPrice
    val positionMaxY =
        chartHeight - calculatePositionYByPrice(chartData.highestPrice, minValue, maxValue, chartHeight)
    val positionMinY =
        chartHeight - calculatePositionYByPrice(chartData.lowestPrice, minValue, maxValue, chartHeight)
    val previousPositionY =
        chartHeight - calculatePositionYByPrice(chartData.previousPrice, minValue, maxValue, chartHeight)

//    drawLine(
//        color = if (isHighPump) JetColor.accentVariant else JetColor.error,
//        start = Offset(positionX + candleWidth / 2, previousPositionY),
//        end = Offset(positionX + candleWidth / 2, positionMaxY),
//    )
    drawLine(
        color = if (isLowPump) JetColor.accentVariant else JetColor.error,
        start = Offset(positionX + candleWidth / 2, previousPositionY),
        end = Offset(positionX + candleWidth / 2, positionMinY),
    )
}

private fun DrawScope.drawCandle(
    chartData: ChartData,
    positionX: Float,
    candleWidth: Float,
    minValue: Float,
    maxValue: Float,
    chartHeight: Float
) {
    val isPump = chartData.currentPrice > chartData.previousPrice
    val positionY =
        chartHeight - calculatePositionYByPrice(chartData.currentPrice, minValue, maxValue, chartHeight)
    val previousPositionY =
        chartHeight - calculatePositionYByPrice(chartData.previousPrice, minValue, maxValue, chartHeight)

    val topLeft = Offset(positionX, previousPositionY)
    val size = Size(candleWidth, positionY - previousPositionY)
    drawCircle(color = Color.Cyan, center = topLeft, radius = 12f)
    drawRect(
        color = if (isPump) JetColor.accentVariantTest else JetColor.errorTest,
        topLeft = topLeft,
        size = size
    )
}

private fun calculatePositionYByPrice(
    price: Float,
    minValue: Float,
    maxValue: Float,
    chartHeight: Float
): Float {
    return (100f * price / (maxValue - minValue)) * chartHeight / 100f
}

private fun calculateBoundaryChart(chartData: List<ChartData>): Pair<Float, Float> {
    var maxValue = 0f
    var minValue = Float.MAX_VALUE

    chartData.forEach { data ->
        if (data.highestPrice > maxValue) maxValue = data.highestPrice
        if (data.lowestPrice < minValue) minValue = data.lowestPrice
    }

    return maxValue to minValue
}
