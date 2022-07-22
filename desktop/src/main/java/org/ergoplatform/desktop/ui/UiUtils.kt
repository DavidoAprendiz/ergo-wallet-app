package org.ergoplatform.desktop.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import org.ergoplatform.Application
import org.ergoplatform.ErgoAmount
import org.ergoplatform.desktop.ui.navigation.NavHostComponent
import org.ergoplatform.mosaik.MosaikDialog
import org.ergoplatform.transactions.MessageSeverity
import org.ergoplatform.uilogic.STRING_BUTTON_COPY_SENSITIVE_DATA
import org.ergoplatform.uilogic.STRING_DESC_COPY_SENSITIVE_DATA
import org.ergoplatform.uilogic.STRING_LABEL_CANCEL
import org.ergoplatform.uilogic.STRING_LABEL_ERG_AMOUNT

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.addOnEnterListener(onEnter: () -> Unit): Modifier {
    return this.onKeyEvent {
        if (it.type == KeyEventType.KeyUp && (it.key == Key.Enter || it.key == Key.NumPadEnter)) {
            onEnter()
            true
        } else false
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun showSensitiveDataCopyDialog(navHost: NavHostComponent, dataToCopy: String) {
    navHost.dialogHandler.showDialog(
        MosaikDialog(
            Application.texts.getString(STRING_DESC_COPY_SENSITIVE_DATA),
            Application.texts.getString(STRING_BUTTON_COPY_SENSITIVE_DATA),
            Application.texts.getString(STRING_LABEL_CANCEL),
            { dataToCopy.copyToClipoard() },
            null
        )
    )
}

@Composable
fun ergoLogo() = painterResource("symbol_bold__1080px__black.svg")

@Composable
fun ErgoAmount.toComposableText() = remember {
    Application.texts.getString(
        STRING_LABEL_ERG_AMOUNT,
        toStringRoundToDecimals()
    )
}

fun MessageSeverity.getSeverityIcon(): ImageVector? =
    when (this) {
        MessageSeverity.NONE -> null
        MessageSeverity.INFORMATION -> Icons.Default.Info
        MessageSeverity.WARNING -> Icons.Default.Warning
        MessageSeverity.ERROR -> Icons.Default.Error
    }