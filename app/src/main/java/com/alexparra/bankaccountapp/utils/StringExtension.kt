package com.alexparra.bankaccountapp.utils

import java.security.MessageDigest

fun String.toSHA256(): String {
    val messageDigest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return messageDigest.fold("", { str, it -> str + "%02x".format(it) })
}