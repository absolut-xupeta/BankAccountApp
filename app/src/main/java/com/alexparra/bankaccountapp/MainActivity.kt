package com.alexparra.bankaccountapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexparra.bankaccountapp.objects.AccountsManager

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        checkSession(navController)
    }

    private fun checkSession(navController: NavController) {
        val sessionUser = AccountsManager.checkSession()

        if (sessionUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToAccountScreenFragment(sessionUser)
            navController.navigate(action)
        } else {
            return
        }
    }
}