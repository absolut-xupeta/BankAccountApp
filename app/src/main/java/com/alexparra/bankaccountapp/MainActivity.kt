package com.alexparra.bankaccountapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.alexparra.bankaccountapp.objects.AccountsManager
import com.alexparra.bankaccountapp.utils.AccountScreenFragment
import com.alexparra.bankaccountapp.utils.addFragmentWithArgs

const val SESSION_USER = "SESSION_USER"

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check current session.
        val sessionUser = AccountsManager.checkSession()

        if (sessionUser != null) {
            AccountScreenFragment().addFragmentWithArgs(SESSION_USER, sessionUser)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LoginFragment>(R.id.fragment_container_view)
            }
        }
    }
}