package com.example.mobilecomputing2024.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme


@Composable
fun HomeScreen(onNavigateToMessages: () -> Unit) {
    Column {
        Text("Home")
        Button(onClick = { onNavigateToMessages() }) {
            Text("Go to messages")
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Composable
fun PreviewConversation() {
    Mobilecomputing2024Theme {
        HomeScreen(onNavigateToMessages = {})
    }
}