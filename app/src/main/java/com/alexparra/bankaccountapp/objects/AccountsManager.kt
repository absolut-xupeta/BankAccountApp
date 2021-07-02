package com.alexparra.bankaccountapp.objects

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.SavingsAccount
import com.alexparra.bankaccountapp.objects.AccountsManager.toSHA256
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.security.MessageDigest
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

    fun authenticate(id: String, password: String = "", context: Context, flag: Boolean = false): Account? {
        getAccountList(context).let { list ->
            list.forEach {
                if (flag) {
                    if (it.accountNumber.toString() == id) {
                        return it
                    }
                }

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
        getAccountList(context).let { list ->
            list.forEach {
                if (it.ownerName == ownerName) {
                    if (it is CurrentAccount && type == "Current Account") {
                        return false
                    }

                    if (it is SavingsAccount && type == "Savings Account") {
                        return false
                    }
                }
            }

            val filePath = File(context.cacheDir, file)

            val fileWriter = FileWriter(filePath, true)
            fileWriter.append("$accountNumber;$type;$ownerName;${password.toSHA256()};$creationDate;$balance\n")
            fileWriter.close()

            list.add(
                when (type) {
                    SAVINGS -> SavingsAccount(accountNumber.toInt(), password, ownerName,toDate(creationDate.toLong()), balance)
                    else -> CurrentAccount(accountNumber.toInt(), password, ownerName, toDate(creationDate.toLong()), balance)
                }
            )
        }
        return true
    }

    fun updateUser(context: Context, user: Account) {
        // Check if the clientList is initialized
        clientList?.forEach {
            if (it.accountNumber == user.accountNumber) {
                it.balance = user.balance
            }
        }
        updateFile(context)
    }

    fun updateFile(context: Context) {
        File(context.cacheDir, file).bufferedWriter().use { bw ->
            bw.write("")
            clientList?.forEach {
                if (it is CurrentAccount) {
                    bw.append("${it.accountNumber};Current Account;${it.ownerName};${it.password};${it.creationDate.time};${it.balance}\n")
                } else {
                    bw.append("${it.accountNumber};Savings Account;${it.ownerName};${it.password};${it.creationDate.time};${it.balance}\n")
                }
            }
        }


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