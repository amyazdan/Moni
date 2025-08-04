package com.app.moni

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.moni.data.db.AppDatabase
import com.app.moni.data.model.BankEntity
import com.app.moni.ui.budget.BudgetScreen
import com.app.moni.ui.components.BottomNavBar
import com.app.moni.ui.dashboard.DashboardScreen
import com.app.moni.ui.dashboard.DashboardViewModel
import com.app.moni.ui.settings.AddEditBankScreen
import com.app.moni.ui.settings.SettingsScreen
import com.app.moni.ui.settings.SettingsViewModel
import com.app.moni.ui.transactions.TransactionsScreen
import com.app.moni.ui.theme.MoniTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val SMS_PERMISSION_CODE = 100
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        settingsViewModel = SettingsViewModel(db.bankDao())
        dashboardViewModel = DashboardViewModel(db.transactionDao())

        setContent {
            MoniTheme {
                var currentScreen by remember { mutableStateOf("dashboard") }
                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            selectedScreen = currentScreen,
                            onScreenSelected = { currentScreen = it }
                        )
                    }
                ) { innerPadding ->
                    when (currentScreen) {
                        "dashboard" -> {
                            DashboardScreen(viewModel = dashboardViewModel)
                        }
                        "transactions" -> {
                            TransactionsScreen()
                        }
                        "budget" -> {
                            BudgetScreen()
                        }
                        "settings" -> SettingsScreen(
                            viewModel = settingsViewModel,
                            onAddBankClick = { currentScreen = "add_edit_bank" }
                        )
                        "add_edit_bank" -> AddEditBankScreen(
                            viewModel = settingsViewModel,
                            onSave = { currentScreen = "settings" }
                        )
                    }
                }
            }
        }
        checkSmsPermissions()
        addTestBankData()
    }

    private fun checkSmsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                SMS_PERMISSION_CODE
            )
        } else {
            Log.d("Moni", "SMS permissions are already granted.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Moni", "SMS permissions granted.")
            } else {
                Log.d("Moni", "SMS permissions denied.")
            }
        }
    }

    private fun addTestBankData() {
        val db = AppDatabase.getDatabase(this)
        val bankDao = db.bankDao()
        val testBank = BankEntity(senderNumber = "100060", bankName = "بانک ملی")

        CoroutineScope(Dispatchers.IO).launch {
            val existingBankName = bankDao.getBankNameBySender(testBank.senderNumber)
            if (existingBankName == null) {
                bankDao.insertBank(testBank)
                Log.d("Moni", "Test bank data inserted: ${testBank.bankName} with sender ${testBank.senderNumber}")
            } else {
                Log.d("Moni", "Test bank data already exists.")
            }
        }
    }
}
