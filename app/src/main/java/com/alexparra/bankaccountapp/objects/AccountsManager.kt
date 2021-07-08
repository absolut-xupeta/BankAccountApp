package com.alexparra.bankaccountapp.objects

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.alexparra.bankaccountapp.MainApplication.Companion.applicationContext
import com.alexparra.bankaccountapp.R
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.SavingsAccount
import com.alexparra.bankaccountapp.utils.toSHA256
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList

object AccountsManager {

    private var clientList: ArrayList<Account>? = null
    private var savedAccountId = applicationContext().getSharedPreferences("session", Context.MODE_PRIVATE)

    private const val FILE = "login.csv"

    const val SAVINGS = "Savings Account"
    const val CURRENT = "Current Account"


    private fun getAccountList(context: Context): ArrayList<Account> {
        return clientList ?: ArrayList<Account>().also { list ->
            val filePath = File(context.cacheDir, FILE)

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

    fun authenticate(
        id: String,
        password: String = "",
        context: Context,
        flag: Boolean = false
    ): Account? {
        getAccountList(context).let { list ->
            list.forEach {
                if (flag && it.accountNumber.toString() == id) {
                    return it
                }

                if (it.accountNumber.toString() == id && it.password == password.toSHA256()) {
                    return it
                }
            }
            return null
        }
    }

    fun searchUser(id: String): Boolean {
            clientList?.forEach {
               if (id == it.accountNumber.toString()) return true
            }
        return false
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

            val filePath = File(context.cacheDir, FILE)

            val fileWriter = FileWriter(filePath, true)
            fileWriter.append("$accountNumber;$type;$ownerName;${password.toSHA256()};$creationDate;$balance\n")
            fileWriter.close()

            list.add(
                when (type) {
                    SAVINGS -> SavingsAccount(
                        accountNumber.toInt(),
                        password.toSHA256(),
                        ownerName,
                        toDate(creationDate.toLong()),
                        balance
                    )
                    else -> CurrentAccount(
                        accountNumber.toInt(),
                        password.toSHA256(),
                        ownerName,
                        toDate(creationDate.toLong()),
                        balance
                    )
                }
            )
        }
        return true
    }

    fun updateBalance(userToTransfer: String?, user: Account, value: Long, operationType: String): Boolean {
        val newValue = value * 100

        when (operationType ) {
            "Deposit" -> {
                user.balance = user.balance.plus(newValue)
                updateUser(applicationContext(), user)
                return true
            }

            "Transfer" -> {
                val receivingUser: Account? = userToTransfer?.let { retrieveUser(it) }

                return if (user.balance.minus(newValue) < 0) {
                    // ERROR
                    false

                } else {
                    // Remove from the transferring user.
                    user.balance = user.balance.minus(newValue)
                    updateUser(applicationContext(), user)

                    // Deposit on the receiving user.
                    receivingUser?.balance = receivingUser?.balance?.plus(newValue) ?: throw Exception("A user is needed.")
                    updateUser(applicationContext(), receivingUser)
                    true
                }
            }

            else -> {
                return if (user.balance.minus(newValue) < 0) {
                    // ERROR
                    false

                } else {
                    user.balance = user.balance.minus(newValue)
                    //binding.currencyAmount.text = getBalanceString(user)
                    updateUser(applicationContext(), user)
                    true
                }
            }
        }
    }

    private fun retrieveUser(id: String): Account? {
        clientList?.forEach {
            if (id == it.accountNumber.toString()) {
                return it
            }
        }
        return null
    }

    private fun updateUser(context: Context, user: Account) {
        // Check if the clientList is initialized
        clientList?.forEach {
            if (it.accountNumber == user.accountNumber) {
                it.balance = user.balance
            }
        }
        updateFile(context)
    }

    fun updateFile(context: Context) {
        File(context.cacheDir, FILE).bufferedWriter().use { bw ->
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


    fun checkSession(): Account? {
        if (savedAccountId.getString("accountNumber", "").toString().isNotBlank()) {
            return authenticate(
                id = savedAccountId.getString("accountNumber", "").toString(),
                flag = true,
                context = applicationContext()
            )
        }
        return null
    }

    fun saveSession(id: String) {
        savedAccountId.edit().apply {
            putString("accountNumber", id)
            apply()
        }
    }

    fun clearSession() {
        savedAccountId.edit().apply {
            clear()
            apply()
        }
    }


    fun delay(delay: Long = 1500, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }


    private fun createCsv(path: File) = path.createNewFile()

    private fun toDate(epoch: Long): Date {
        return Date(epoch)
    }
}