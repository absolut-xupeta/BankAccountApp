package com.alexparra.bankaccountapp.model

import java.util.*

class Currency(
    var balance: Long = 0,
    var currency: String? = null
)

abstract class Account(
    val accountNumber: Int,
    var password: String,
    var ownerName: String,
    val creationDate: String,
    var balance: Long
) {
    abstract fun deposit(value: Long)
    abstract fun withdraw(value: Long)
}