package com.alexparra.bankaccountapp.model

import java.util.*

class CurrentAccount(
    accountNumber: Int,
    password: String,
    ownerName: String,
    creationDate: Date,
    balance: Long = 0L
) : Account(
    accountNumber,
    password,
    ownerName,
    creationDate,
    balance
) {
    override fun deposit(value: Long) {
        balance += value
    }

    override fun withdraw(value: Long) {
        balance += -(value)
    }
}