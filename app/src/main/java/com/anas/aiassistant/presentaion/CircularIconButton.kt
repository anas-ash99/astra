package com.anas.aiassistant.presentaion

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.ui.theme.AppMainColor

@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    contentDescription: String?,
    backgroundColor: Color = AppMainColor,
    enabled: Boolean = true,
    iconSize: Dp = 23.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(48.dp) // Example size, adjust as needed
            .clip(CircleShape), // Clip to a circle shape
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        enabled = enabled,
        contentPadding = PaddingValues(0.dp) // Remove default button padding
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}
