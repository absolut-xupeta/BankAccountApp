package com.alexparra.bankaccountapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.databinding.FragmentLoginBinding
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.toast

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val navController: NavController by lazy {
        findNavController()
    }

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
                        toast(getString(R.string.account_number_missing))
                    }

                    passwordField.text.toString() == "" -> {
                        toast(getString(R.string.password_missing))
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

                                // Clear text fields.
                                binding.accountNumberField.text = null
                                binding.passwordField.text = null

                                // Create action to pass a value to the next fragment.
                                val action = LoginFragmentDirections.actionLoginFragmentToAccountScreenFragment(loginResult)
                                navController.navigate(action)

                            } else {
                                changeButtonState(false)
                                toast(getString(R.string.login_error))
                            }
                        }
                    }
                }
            }

            createAccountButton.setOnClickListener {
                changeButtonState(true)

                val action = LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
                navController.navigate(action)

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
}
