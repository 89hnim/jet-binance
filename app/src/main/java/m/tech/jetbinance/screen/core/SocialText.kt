package m.tech.jetbinance.screen.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import m.tech.jetbinance.ui.theme.JetColor

@Composable
fun SocialText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle?,
    maxLines: Int = Int.MAX_VALUE,
    hashTags: List<Pair<Int, Int>> = emptyList(),
    hashTagColor: Color = JetColor.accent,
) {
    val textStyle = style ?: MaterialTheme.typography.bodyMedium
    Text(
        modifier = modifier,
        style = textStyle,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        text = buildAnnotatedString {
            append(text)
            hashTags.forEach {
                addStyle(SpanStyle(color = hashTagColor), it.first, it.second)
            }
        },
    )
}
