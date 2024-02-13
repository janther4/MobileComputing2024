package com.example.mobilecomputing2024.ui.messages

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mobilecomputing2024.ui.theme.Mobilecomputing2024Theme
import com.example.mobilecomputing2024.viewmodel.MyViewModel
import kotlinx.coroutines.flow.Flow

data class Message(val author: String, val body: String)

@Composable
fun Conversation(
    onNavigateToHome: () -> Unit,
    messages: List<Message>
) {
    Column {
        Button(onClick = { onNavigateToHome() },
            modifier = Modifier.align(Alignment.End),
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
        }
        LazyColumn {
            items(messages) { message ->
                MessageCard(
                    message
                )
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    val viewModel: MyViewModel = viewModel()
    val latestUser by viewModel.latestUser.collectAsState()
    val latestImgUri by viewModel.latestImg.collectAsState()
    val imageUri = latestImgUri?.let { Uri.parse(it.imgUri) }
    // debug
    Log.d("URI RESULT", "$imageUri")

    Row(modifier = Modifier.padding(all = 8.dp)) {
            AsyncImage(
                model = imageUri,
                //model = "${latestImgUri?.imgUri}",
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(2.dp, Color.LightGray),
                        CircleShape
                    )
                    .clip(CircleShape)
            )
            /*        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )*/
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember{ mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface,
            label = "",
        )
        Column (modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = "${latestUser?.name}",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ){
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
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
        Conversation(
            onNavigateToHome = { },
            SampleData.conversationSample
        )
    }
}
