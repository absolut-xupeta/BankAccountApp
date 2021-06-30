package com.alexparra.bankaccountapp.csv

import android.content.Context
import com.alexparra.bankaccountapp.model.Account
import com.alexparra.bankaccountapp.model.CurrentAccount
import com.alexparra.bankaccountapp.model.SavingsAccount
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object AccountsManager {
    private var clientList: ArrayList<Account>? = null
    private const val file = "login.csv"

    fun getAccountList(context: Context): ArrayList<Account> {
        return clientList ?: ArrayList<Account>().also { list ->
            val filePath = File(context.cacheDir, file)
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
                            row[4],
                            row[5].toLong()
                        )
                    )
                } else {
                    list.add(
                        SavingsAccount(
                            row[0].toInt(),
                            row[3],
                            row[2],
                            row[4],
                            row[5].toLong()
                        )
                    )
                }
            }
            clientList = list
        }
    }

    fun authenticate(id: String, password: String, context: Context): Account? {
        // Check if the clientList is initialized
        getAccountList(context)

        clientList?.forEach {
            if (it.accountNumber.toString() == id && it.password == password) {
                return it
            }
        }
        return null
    }

    fun createAccount(context: Context, accountNumber: Int, name: String, password: String) {
        // Check if the clientList is initialized
        getAccountList(context)

        val filePath = File(context.cacheDir, file)
        val fileWriter = FileWriter(filePath, true)
        fileWriter.append("")
        fileWriter.close()
        TODO()
    }

    fun updateUser(context: Context) {
        // Check if the clientList is initialized
        getAccountList(context)
        TODO()
    }

    fun constructAccount() {
        TODO()
    }

    private fun createCsv(path: File) = path.createNewFile()
}