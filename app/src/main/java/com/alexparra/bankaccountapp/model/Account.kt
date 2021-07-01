package com.alexparra.bankaccountapp.model

import java.io.Serializable
import java.util.*

class Currency(
    var balance: Long = 0,
    var currency: String? = null
)

abstract class Account(
    val accountNumber: Int,
    var password: String,
    var ownerName: String,
    val creationDate: Date,
    var balance: Long
) : Serializable {
    abstract fun deposit(value: Long)
    abstract fun withdraw(value: Long)
}