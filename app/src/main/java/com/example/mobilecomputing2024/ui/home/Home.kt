package com.example.mobilecomputing2024.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing2024.R
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme


@Composable
fun HomeScreen(onNavigateToMessages: () -> Unit) {
    Column {
        Button(onClick = { onNavigateToMessages() },
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Text("User:")
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
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