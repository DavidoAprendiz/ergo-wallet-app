package org.ergoplatform.desktop.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import org.ergoplatform.persistance.WalletConfig

sealed class ScreenConfig : Parcelable {
    object WalletList : ScreenConfig()
    object Settings : ScreenConfig()
    object AddWalletChooser : ScreenConfig()
    object AddReadOnlyWallet : ScreenConfig()
    data class SendFunds(val name: String) : ScreenConfig()
    data class ReceiveToWallet(val walletConfig: WalletConfig) : ScreenConfig()
    data class WalletConfiguration(val walletConfig: WalletConfig) : ScreenConfig()
    data class QrCodeScanner(val callback: (String) -> Unit) : ScreenConfig()
}