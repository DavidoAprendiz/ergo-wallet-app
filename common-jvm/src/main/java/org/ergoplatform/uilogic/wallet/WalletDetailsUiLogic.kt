package org.ergoplatform.uilogic.wallet

import org.ergoplatform.ErgoAmount
import org.ergoplatform.persistance.Wallet
import org.ergoplatform.persistance.WalletAddress
import org.ergoplatform.persistance.WalletState
import org.ergoplatform.persistance.WalletToken
import org.ergoplatform.uilogic.STRING_LABEL_ALL_ADDRESSES
import org.ergoplatform.uilogic.StringProvider
import org.ergoplatform.wallet.*
import org.ergoplatform.wallet.addresses.getAddressLabel

class WalletDetailsUiLogic {
    var wallet: Wallet? = null
        set(value) {
            field = value
            refreshAddress()
        }
    var addressIdx: Int? = null
        set(value) {
            field = value
            refreshAddress()
        }
    var walletAddress: WalletAddress? = null
        private set

    private fun refreshAddress() {
        walletAddress = addressIdx?.let { wallet?.getDerivedAddressEntity(it) }
    }

    fun getAddressLabel(texts: StringProvider) = walletAddress?.getAddressLabel(texts)
        ?: texts.getString(STRING_LABEL_ALL_ADDRESSES, wallet?.getNumOfAddresses() ?: 0)

    fun getErgoBalance(): ErgoAmount {
        val addressState = getAddressState()
        return ErgoAmount(addressState?.balance ?: wallet?.getBalanceForAllAddresses() ?: 0)
    }

    private fun getAddressState(): WalletState? {
        return walletAddress?.let { wallet?.getStateForAddress(it.publicAddress) }
    }

    fun getUnconfirmedErgoBalance() = ErgoAmount(
        getAddressState()?.unconfirmedBalance ?: wallet?.getUnconfirmedBalanceForAllAddresses() ?: 0
    )

    fun getTokensList(): List<WalletToken> {
        return (walletAddress?.let { wallet?.getTokensForAddress(it.publicAddress) }
            ?: wallet?.getTokensForAllAddresses() ?: emptyList()).sortedBy { it.name?.lowercase() }
    }
}