package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.alexparra.bankaccountapp.databinding.FragmentCreateAccountBinding
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.replaceFragment
import java.util.*

class CreateAccountFragment : Fragment() {

    private lateinit var binding: FragmentCreateAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            val id = AccountsManager.generateAccountNumber(requireContext())
            accountCreation.text = id

            createAccountButton.setOnClickListener {

                if (nameCreation.text.toString() == "" || passwordCreation.text.toString() == "") {
                    Toast.makeText(context, R.string.field_warning, Toast.LENGTH_SHORT).show()

                } else if (!chooseSavings.isChecked && !chooseCurrent.isChecked) {
                    Toast.makeText(context, R.string.radio_button_warning, Toast.LENGTH_SHORT).show()

                } else {
                    changeButtonState(true)

                    // Check for the initial deposit
                    val balance: Long = if (initialDeposit.text.toString() == "") {
                        0
                    } else {
                        val value = initialDeposit.text.toString()
                        value.toLong() * 100
                    }

                    // Check the user account type
                    val type: String = if (chooseSavings.isChecked) {
                        AccountsManager.SAVINGS
                    } else {
                        AccountsManager.CURRENT
                    }

                    // Get the creation date.
                    val date = Calendar.getInstance().timeInMillis.toString()

                    // Create the new user.
                    val result = AccountsManager.createAccount(
                        requireContext(),
                        type,
                        id,
                        passwordCreation.text.toString(),
                        nameCreation.text.toString(),
                        date,
                        balance,
                    )

                    // Go back to the login screen.
                    if (result) {
                        Toast.makeText(context, R.string.create_account_success, Toast.LENGTH_SHORT).show()
                        AccountsManager.delay {
                            replaceFragment(LoginFragment.newInstance(), R.id.fragment_container_view)
                        }
                    } else {
                        Toast.makeText(context, R.string.same_account_type, Toast.LENGTH_LONG).show()
                        changeButtonState(false)
                    }
                }
            }
        }
    }

    private fun changeButtonState(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                createAccountButton.apply {
                    alpha = 1F
                    isClickable = false
                    isFocusable = false
                }
            } else {
                createAccountButton.apply {
                    alpha = 0.5F
                    isClickable = true
                    isFocusable = true
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CreateAccountFragment.
         */
        @JvmStatic
        fun newInstance() = CreateAccountFragment()
    }
}