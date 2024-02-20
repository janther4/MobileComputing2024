package com.example.mobilecomputing2024.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mobilecomputing2024.MainActivity
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme
import com.example.mobilecomputing2024.viewmodel.MyViewModel


@Composable
fun HomeScreen(onNavigateToMessages: () -> Unit) {

    val viewModel: MyViewModel = viewModel()

    val latestImgUri by viewModel.latestImg.collectAsState()

    /*    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }*/

    var selectedImageUri by remember(latestImgUri) {
        mutableStateOf(latestImgUri?.let { Uri.parse(it.imgUri) })
    }

    LaunchedEffect(latestImgUri) {
        // Update the state with the latest image URI from the database
        selectedImageUri = latestImgUri?.let { Uri.parse(it.imgUri) }
    }

    //selectedImageUri = latestImgUri?.let { Uri.parse(it.imgUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            //val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            // Convert URI to string and save to database
            uri?.let {
                val imageUri = it.toString()
                viewModel.saveImageToDatabase(imageUri)
            }
        }
    )
    /*

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val imageUri = it.toString()
            viewModel.saveImageToDatabase(imageUri)
            selectedImageUri = latestImgUri?.let { Uri.parse(it.imgUri) }
        }
    }

*/
    Column {
        Button(
            onClick = { onNavigateToMessages() },
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Text("User:")
        //selectedImageUri = latestImgUri?.let { Uri.parse(it.imgUri) }
        //selectedImageUri?.let { uri ->
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .border(
                        BorderStroke(2.dp, Color.LightGray),
                        CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
            //var text by remember { mutableStateOf("") }
        //}
            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { newText -> viewModel.text = newText },
                label = { Text("Username") },
            )
            Button(onClick = { viewModel.onSaveButtonClick() }) {
                Text("Save to Database")
            }
            val context = LocalContext.current
            var hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                } else mutableStateOf(true)
            }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {isGranted ->
                    hasNotificationPermission = isGranted
                })
            Button(onClick = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            }) {
                Text("Request permission")
            }
            Button(onClick = {
                if(hasNotificationPermission) {
                    MainActivity.showNotification(context, "Notifications enabled")
                }
                }) {
                Text("Show notification")
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
