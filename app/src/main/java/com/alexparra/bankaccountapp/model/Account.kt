    package com.alexparra.bankaccountapp.model

class Currency(
    var balance: Long = 0,
    var currency: String? = null
)

abstract class Account(
    val accountNumber: Int,
    var password: String,
    var ownerName: String,
) {
    abstract fun deposit(value: Long, currency: String?)
    abstract fun withdraw(value: Long)
}