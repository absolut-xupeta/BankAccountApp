package com.alexparra.bankaccountapp.objects

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alexparra.bankaccountapp.MainApplication.Companion.applicationContext
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.SavingsAccount
import com.alexparra.bankaccountapp.utils.toSHA256
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object AccountsManager {

    private var clientList: ArrayList<Account>? = null
    private var savedAccountId = applicationContext().getSharedPreferences("session", Context.MODE_PRIVATE)

    private const val FILE = "login.csv"

    const val SAVINGS = "Savings Account"
    const val CURRENT = "Current Account"

    /**
     * Get's the clientList if it is not null, if it is, returns the list as it is.
     */
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

    /**
     * Returns the logged in user if the id and password are correct.
     * Flag is used if the current authentication is for a session user,
     * this returns automatically its password to authentication.
     */
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

    /**
     * Search if the provided id exists in the clientList.
     */
    fun searchUser(id: String): Boolean {
            clientList?.forEach {
               if (id == it.accountNumber.toString()) return true
            }
        return false
    }

    /**
     * Create a new Savings or Current account.
     */
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

    /**
     * Update the balance for various types of operations.
     */
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
                    // Get transfer date.
                    val date = toDate(Calendar.getInstance().timeInMillis)
                    val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
                    val finalDate = formatter.format(date)

                    // Make transactions.
                    user.balance = user.balance.minus(newValue)
                    receivingUser?.balance = receivingUser?.balance?.plus(newValue) ?: throw Exception("A user is needed.")

                    // Transferring user.
                    addTransaction(user.accountNumber.toString(), "Sent", receivingUser.ownerName, value, finalDate)
                    updateUser(applicationContext(), user)

                    // Receiving user.
                    addTransaction(receivingUser.accountNumber.toString(), "Received", user.ownerName, value, finalDate)
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

    /**
     * Add a transaction to the user transaction csv based on the transferType.
     */
    private fun addTransaction(id: String, transferType: String, name: String, amount: Long, date: String) {
        // Get the unique csv file name.
        val fileName = "$id.csv"
        val filePath = File(applicationContext().cacheDir, fileName)

        val fileWriter = FileWriter(filePath, true)

        // Append the correct format for transactions.
        fileWriter.append("$transferType;$name;$amount;$date\n")
        fileWriter.close()
    }

    /**
     * Retrieve the user, this is needed to authenticate
     */
    private fun retrieveUser(id: String): Account? {
        clientList?.forEach {
            if (id == it.accountNumber.toString()) {
                return it
            }
        }
        return null
    }

    /**
     * Update the user object inside the instantiated clientList and call the
     * updateFile function so that the user .csv is up to date.
     */
    private fun updateUser(context: Context, user: Account) {
        // Check if the clientList is initialized
        clientList?.forEach {
            if (it.accountNumber == user.accountNumber) {
                it.balance = user.balance
                return@forEach
            }
        }
        updateFile(context)
    }

    /**
     * Updated the file based on the current clientList.
     */
    private fun updateFile(context: Context) {
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

    /**
     * Searches for the biggest accountNumber and return the current accountNumber +1
     * so that the new user can have an unique number.
     */
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

    /**
     * Check if there's a session already saved
     */
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

    /**
     * Save the current session id.
     */
    fun saveSession(id: String) {
        savedAccountId.edit().apply {
            putString("accountNumber", id)
            apply()
        }
    }

    /**
     * Clear all the session file.
     */
    fun clearSession() {
        savedAccountId.edit().apply {
            clear()
            apply()
        }
    }

    /**
     * Delay function wrapper for better visual effects.
     */
    fun delay(delay: Long = 1500, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delay)
    }

    /**
     * Creates the user .csv.
     */
    private fun createCsv(path: File) = path.createNewFile()

    /**
     * Transforms a Long number from Epoch and returns a valid Date.
     */
    private fun toDate(epoch: Long): Date {
        return Date(epoch)
    }
}