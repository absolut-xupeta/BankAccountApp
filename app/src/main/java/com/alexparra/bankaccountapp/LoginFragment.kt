package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.alexparra.bankaccountapp.databinding.FragmentLoginBinding
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.AccountScreenFragment
import com.alexparra.bankaccountapp.utils.replaceFragment

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeButtons()
    }

    private fun initializeButtons() {

        binding.apply {
            // Button actions.
            loginButton.setOnClickListener {
                when {
                    accountNumberField.text.toString() == "" -> {
                        Toast.makeText(
                            context,
                            getString(R.string.account_number_missing),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    passwordField.text.toString() == "" -> {
                        Toast.makeText(context, getString(R.string.password_missing), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        changeButtonState(true)

                        AccountsManager.delay(1000) {
                            val loginResult = AccountsManager.authenticate(
                                accountNumberField.text.toString(),
                                passwordField.text.toString(),
                                requireContext()
                            )

                            if (loginResult != null) {
                                // Save the session for the newly logged user.
                                AccountsManager.saveSession(accountNumberField.text.toString())

                                replaceFragment(AccountScreenFragment.newInstance(loginResult), R.id.fragment_container_view, )

                            } else {
                                changeButtonState(false)
                                Toast.makeText(context, getString(R.string.login_error), Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }

            createAccountButton.setOnClickListener {
                changeButtonState(true)

                replaceFragment(CreateAccountFragment.newInstance(), R.id.fragment_container_view)

                changeButtonState(false)
            }
        }

    }

    private fun changeButtonState(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                loginButton.apply {
                    alpha = 0.5F
                    isClickable = false
                    isFocusable = false
                }

                createAccountButton.apply {
                    isClickable = false
                    isFocusable = false
                }

                mainWindowProgressBar.visibility = View.VISIBLE

            } else {
                loginButton.apply {
                    alpha = 1F
                    isClickable = true
                    isFocusable = true
                }
                createAccountButton.apply {
                    isClickable = true
                    isFocusable = true
                }

                mainWindowProgressBar.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}