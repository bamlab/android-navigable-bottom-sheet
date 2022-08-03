package tech.bam.bottomsheetnavigation.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetStructure(
    modifier: Modifier = Modifier,
    onCrossClick: (() -> Unit)? = null,
    titleText: String? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Box(
                Modifier
                    .padding(top = 8.dp)
                    .height(4.dp)
                    .width(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                onCrossClick?.let {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .testTag("CloseIconButton"),
                        onClick = onCrossClick
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.headlineLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = titleText ?: ""
                )
            }

            Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))
            content()
            Spacer(modifier = androidx.compose.ui.Modifier.height(36.dp))
        }
    }
}
