package com.alexparra.bankaccountapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alexparra.bankaccountapp.databinding.FragmentSplashBinding
import com.alexparra.bankaccountapp.objects.AccountsManager

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AccountsManager.delay(300) { binding.progressBar.visibility = View.VISIBLE }

        AccountsManager.delay {

            checkSession()
        }
    }

    private fun checkSession() {
        val sessionUser = AccountsManager.checkSession()

        if (sessionUser != null) {
            val action = SplashFragmentDirections.actionSplashFragmentToAccountScreenFragment(sessionUser)
            navController.navigate(action)
        } else {
            val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            navController.navigate(action)
        }
    }
}