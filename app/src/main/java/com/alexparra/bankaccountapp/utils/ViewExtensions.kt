package com.alexparra.bankaccountapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    tag: String? = null,
    addToStack: Boolean = true,
    stackName: String? = null
) {
    supportFragmentManager.commit {
        replace(containerId, fragment, tag)
        if (addToStack) {
            supportFragmentManager.findFragmentById(containerId)?.let {
                addToBackStack(stackName)
            }
        }
    }
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    tag: String? = null,
    addToStack: Boolean = true,
    stackName: String? = null
) {
    activity?.replaceFragment(
        fragment,
        containerId,
        tag,
        addToStack,
        stackName
    )
}

fun Fragment.popBackStack(stackName: String? = null, flags: Int = 0) {
    activity?.supportFragmentManager?.popBackStackImmediate(stackName, flags)
}