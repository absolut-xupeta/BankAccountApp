package com.alexparra.bankaccountapp.model

import java.util.*

class SavingsAccount(
    accountNumber: Int,
    password: String,
    ownerName: String,
    creationDate: String,
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