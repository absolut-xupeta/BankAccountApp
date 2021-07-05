package com.alexparra.bankaccountapp.utils

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.alexparra.bankaccountapp.R

fun Fragment.replaceFragment() {
    activity?.supportFragmentManager?.beginTransaction()?.let {
        it.setReorderingAllowed(true)
        it.replace(R.id.fragment_container_view, this)
    }
}

fun Fragment.addFragmentWithArgs(key: String, value: Any) {
    val bundle = bundleOf(key to value)
    activity?.supportFragmentManager?.commit {
        setReorderingAllowed(true)
        add<AccountScreenFragment>(R.id.fragment_container_view, args = bundle)
    }
}