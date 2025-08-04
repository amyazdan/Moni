package com.app.moni.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.app.moni.R

@Composable
fun BottomNavBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedScreen == "dashboard",
            onClick = { onScreenSelected("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = "داشبورد") },
            label = { Text("داشبورد") }
        )
        NavigationBarItem(
            selected = selectedScreen == "transactions",
            onClick = { onScreenSelected("transactions") },
            icon = { Icon(Icons.Default.List, contentDescription = "تراکنش‌ها") },
            label = { Text("تراکنش‌ها") }
        )
        NavigationBarItem(
            selected = selectedScreen == "budget",
            onClick = { onScreenSelected("budget") },
            icon = { Icon(painterResource(id = R.drawable.ic_budget), contentDescription = "بودجه") },
            label = { Text("بودجه") }
        )
        NavigationBarItem(
            selected = selectedScreen == "settings",
            onClick = { onScreenSelected("settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "تنظیمات") },
            label = { Text("تنظیمات") }
        )
    }
}
