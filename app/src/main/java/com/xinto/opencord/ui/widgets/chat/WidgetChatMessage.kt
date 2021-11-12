package com.xinto.opencord.ui.widgets.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.simpleast.render.render
import com.xinto.opencord.util.SimpleAstParser

@OptIn(
    ExperimentalCoilApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun WidgetChatMessage(
    message: DomainMessage,
    parser: SimpleAstParser,
    modifier: Modifier = Modifier,
) {
    val userImage = rememberOpenCordCachePainter(message.author.avatarUrl)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            painter = userImage,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = message.author.username,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            if (message.content.isNotEmpty()) {
                Text(
                    text = parser.render(
                        source = message.content,
                        initialState = null,
                        renderContext = null
                    ).toAnnotatedString(),
                    style = MaterialTheme.typography.body2,
                    inlineContent = mapOf(
                        "emote" to InlineTextContent(
                            placeholder = Placeholder(
                                width = 20.sp,
                                height = 20.sp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) { emoteId ->
                            val image = rememberOpenCordCachePainter("${BuildConfig.URL_CDN}/emojis/$emoteId")
                            Image(
                                painter = image,
                                contentDescription = "Emote"
                            )
                        }
                    )
                )
            }
            for (attachment in message.attachments) {
                when (attachment) {
                    is DomainAttachment.Picture -> {
                        val picture = rememberOpenCordCachePainter(attachment.url) {
                            size(
                                width = attachment.width,
                                height = attachment.height
                            )
                        }

                        Image(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clip(MaterialTheme.shapes.large),
                            painter = picture,
                            contentDescription = null
                        )
                    }
                    else -> { /* TODO */}
                }
            }
        }
    }
}