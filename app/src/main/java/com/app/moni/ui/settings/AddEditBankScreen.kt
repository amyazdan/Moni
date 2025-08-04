package com.app.moni.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.moni.data.model.BankEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBankScreen(viewModel: SettingsViewModel, onSave: () -> Unit) {
    var bankName by remember { mutableStateOf("") }
    var senderNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("افزودن بانک جدید") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("نام بانک") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = senderNumber,
                onValueChange = { senderNumber = it },
                label = { Text("سرشماره") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val newBank = BankEntity(
                        bankName = bankName,
                        senderNumber = senderNumber
                    )
                    viewModel.saveBank(newBank)
                    onSave()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = bankName.isNotBlank() && senderNumber.isNotBlank() // افزودن شرط اعتبارسنجی
            ) {
                Text("ذخیره")
            }
        }
    }
}
