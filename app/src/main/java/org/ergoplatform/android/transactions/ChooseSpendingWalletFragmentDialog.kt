package org.ergoplatform.android.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import org.ergoplatform.android.AppDatabase
import org.ergoplatform.android.R
import org.ergoplatform.android.databinding.FragmentSendFundsWalletChooserBinding
import org.ergoplatform.android.databinding.FragmentSendFundsWalletChooserItemBinding
import org.ergoplatform.android.nanoErgsToErgs
import org.ergoplatform.android.parseContentFromQuery
import org.ergoplatform.android.ui.FullScreenFragmentDialog
import org.ergoplatform.android.ui.navigateSafe


/**
 * Deep link to send funds: Choose wallet to spend from
 */
class ChooseSpendingWalletFragmentDialog : FullScreenFragmentDialog() {

    private var _binding: FragmentSendFundsWalletChooserBinding? = null

    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendFundsWalletChooserBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = requireActivity().intent.data?.encodedQuery

        if (query == null) {
            dismiss()
            return
        }

        val content = parseContentFromQuery(query)
        binding.receiverAddress.text = content?.address
        binding.grossAmount.amount = content?.amount ?: 0f

        AppDatabase.getInstance(requireContext()).walletDao().getWalletsWithStates()
            .observe(viewLifecycleOwner, {
                binding.listWallets.removeAllViews()

                if (it.size == 1) {
                    // immediately switch to send funds screen
                    navigateToSendFundsScreen(it.first().walletConfig.id, true)
                }
                it.forEach { wallet ->
                    val itemBinding = FragmentSendFundsWalletChooserItemBinding.inflate(
                        layoutInflater, binding.listWallets, true
                    )

                    itemBinding.walletBalance.amount =
                        nanoErgsToErgs(wallet.state.map { it.balance ?: 0 }.sum())
                    itemBinding.walletName.text = wallet.walletConfig.displayName

                    itemBinding.root.setOnClickListener {
                        navigateToSendFundsScreen(wallet.walletConfig.id, false)
                    }
                }
            })
    }

    private fun navigateToSendFundsScreen(walletId: Int, popThis: Boolean) {
        val navBuilder = NavOptions.Builder()
        val navOptions =
            navBuilder.setPopUpTo(R.id.chooseSpendingWalletFragmentDialog, popThis).build()

        NavHostFragment.findNavController(requireParentFragment())
            .navigateSafe(
                ChooseSpendingWalletFragmentDialogDirections.actionChooseSpendingWalletFragmentDialogToSendFundsFragment(
                    requireActivity().intent.data?.encodedQuery!!, walletId
                ), navOptions
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}