package com.alexparra.bankaccountapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.replaceFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check current session.
        val sessionUser = AccountsManager.checkSession()

        if (sessionUser != null) {
            replaceFragment(AccountScreenFragment.newInstance(sessionUser), R.id.fragment_container_view)
        } else {
            // Check if there is no other screen instantiated.
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<LoginFragment>(R.id.fragment_container_view)
                }
            }
        }
    }
}