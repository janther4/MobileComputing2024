package com.example.mobilecomputing2024.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mobilecomputing2024.MainActivity
import com.example.mobilecomputing2024.R
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme
import com.example.mobilecomputing2024.viewmodel.MyViewModel
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(onNavigateToMessages: () -> Unit) {

    val viewModel: MyViewModel = viewModel()

    val latestImgUri by viewModel.latestImg.collectAsState()

    var selectedImageUri by remember(latestImgUri) {
        mutableStateOf(latestImgUri?.let { Uri.parse(it.imgUri) })
    }

    LaunchedEffect(latestImgUri) {
        selectedImageUri = latestImgUri?.let { Uri.parse(it.imgUri) }
    }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let {
                val imageUri = it.toString()
                viewModel.saveImageToDatabase(imageUri)
            }
        }
    )

    val context = LocalContext.current
    var recognizedText by remember { mutableStateOf("")}
    val speechRecognizerIntent = remember { Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    } }
    val speechResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val spokenText: String? = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { results ->
                results[0]
            }
            recognizedText = spokenText?: ""
            Log.d("SpeechRecognition", "Recognized text: $spokenText")
        }
    }

    // Permission check and request for RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            speechResultLauncher.launch(speechRecognizerIntent)
        } else {
            // Handle permission denial
        }
    }
    // Check permission
    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
        onDispose { }
    }

    Column {
        Button(
            onClick = { onNavigateToMessages() },
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Text("User:")
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
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    speechResultLauncher.launch(speechRecognizerIntent)
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }) {
                Text("Start Speech Recognition")
            }
        if (recognizedText.isNotEmpty()) {
            Text("Recognized Text: $recognizedText")
        }
    }
}


@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    // Use LaunchedEffect to delay the splash screen
    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 2 seconds
        onSplashEnd() // Invoke the callback after the delay
    }

    // Display your splash content here
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.pngtreechristmas_round_ball_ribbon_border_6654910),
            contentDescription = "App Logo",
            modifier = Modifier.size(512.dp) // Adjust the size as needed
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
