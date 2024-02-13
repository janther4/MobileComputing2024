package com.example.mobilecomputing2024.ui.home

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
    // debug
    Log.d("hääääääääää", "$selectedImageUri")
    //Log.d("jups", "$selectedImageUri")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            // debug
            Log.d("URI RESULT", "$uri")
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
        // debug
        Log.d("hääääääääää2232323", "$selectedImageUri")
        selectedImageUri?.let { uri ->
            AsyncImage(
                model = uri,
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

            OutlinedTextField(
                value = viewModel.text,
                onValueChange = { newText -> viewModel.text = newText },
                label = { Text("Username") },
            )

            Button(onClick = { viewModel.onSaveButtonClick() }) {
                Text("Save to Database")
            }
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
