package com.alexparra.bankaccountapp.model

import java.util.*

class CurrentAccount(
    accountNumber: Int,
    password: String,
    ownerName: String,
) : Account(
    accountNumber,
    password,
    ownerName
) {
    private var balance: Currency = Currency()
    val creationDate: Date = Calendar.getInstance().time

    override fun deposit(value: Long, currency: String?) {
        //NumberFormat.getCurrencyInstance(Locale.CANADA).currency.numericCode
        if (balance.currency == null) {
            balance.let {
                it.balance += value
                it.currency = currency ?: "BRL"
            }
        } else {
            balance.balance += value
        }
    }

    override fun withdraw(value: Long) {
        balance.let {
            it.balance += -(value)
        }
    }
}