package com.alexparra.bankaccountapp.csv

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.SavingsAccount
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.security.MessageDigest
import java.text.DateFormat.getDateInstance
import java.util.*
import kotlin.collections.ArrayList

object AccountsManager {
    private var clientList: ArrayList<Account>? = null
    private const val file = "login.csv"
    const val SAVINGS = "Savings Account"
    const val CURRENT = "Current Account"

    private fun getAccountList(context: Context): ArrayList<Account> {
        return clientList ?: ArrayList<Account>().also { list ->
            val filePath = File(context.cacheDir, file)

            if (!filePath.exists()) createCsv(filePath)

            // Get the locale date format.
            val dateFormat = getDateInstance()

            // Read file to a buffer.
            val streamReader = FileReader(filePath)
            val bufferedReader = BufferedReader(streamReader)

            var row: List<String>

            // [0] Account Number | [1] Type | [2] Owner Name | [3] Hash | [4] Creation Date | [5] Balance
            while (bufferedReader.ready()) {
                row = bufferedReader.readLine().split(';')

                if (row[1] == "Current Account") {
                    list.add(
                        CurrentAccount(
                            row[0].toInt(),
                            row[3],
                            row[2],
                            creationDate = toDate(row[4].toLong()),
                            row[5].toLong()
                        )
                    )
                } else {
                    list.add(
                        SavingsAccount(
                            row[0].toInt(),
                            row[3],
                            row[2],
                            creationDate = toDate(row[4].toLong()),
                            row[5].toLong()
                        )
                    )
                }
            }
            clientList = list
        }
    }

    fun authenticate(id: String, password: String, context: Context): Account? {
        getAccountList(context).let { list ->
            list.forEach {
                if (it.accountNumber.toString() == id && it.password == password.toSHA256()) {
                    return it
                }
            }
            return null
        }
    }

    fun createAccount(
        context: Context,
        type: String,
        accountNumber: String,
        password: String,
        ownerName: String,
        creationDate: String,
        balance: Long
    ): Boolean {
        // Check if the clientList is initialized
        getAccountList(context).let {
            val filePath = File(context.cacheDir, file)

            val fileWriter = FileWriter(filePath, true)
            fileWriter.append("$accountNumber;$type;$ownerName;${password.toSHA256()};$creationDate;$balance\n")
            fileWriter.close()

            it.add(
                when (type) {
                    SAVINGS -> SavingsAccount(accountNumber.toInt(), password, ownerName,toDate(creationDate.toLong()), balance)
                    else -> CurrentAccount(accountNumber.toInt(), password, ownerName, toDate(creationDate.toLong()), balance)
                }
            )
        }
        return true
    }

    fun updateUser(context: Context) {
        // Check if the clientList is initialized
        getAccountList(context)
        TODO()
    }

    fun generateAccountNumber(context: Context): String {
        // Check if the clientList is initialized
        getAccountList(context)

        var larger: Int = -1

        if (clientList.isNullOrEmpty()) return "0"

        clientList?.forEach {
            if (it.accountNumber > larger) {
                larger = it.accountNumber
            }
        }

        return (larger + 1).toString()
    }

    fun constructAccount() {
        TODO()
    }

    fun delay(delay: Long = 1500, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }

    private fun createCsv(path: File) = path.createNewFile()

    private fun String.toSHA256(): String {
        val messageDigest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
        return messageDigest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun toDate(epoch: Long): Date {
        return Date(epoch)
    }
}