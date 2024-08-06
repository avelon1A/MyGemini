package com.example.mygemini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    var userInput by rememberSaveable { mutableStateOf("") }
    val uiState by chatViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)

    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.chat_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Display chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding( 17.dp)
        ) {
            items(uiState.messages) { message ->
                ChatMessage(message)
            }
            if (uiState.uiState is UiState.Loading) {
                item {
                    ShimmerEffect(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 4.dp)
                            .height(60.dp)
                    )
                }
            }
        }

        // User input field and send button
        Row(
            modifier = Modifier.padding( 8.dp)
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent

                )
            )

            Button(
                onClick = {
                    chatViewModel.sendPrompt(userInput)
                    userInput = ""
                },
                enabled = userInput.isNotEmpty(),
                modifier = Modifier.align(Alignment.CenterVertically),
                colors =  ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            ) {
                Text(text = stringResource(R.string.action_send))
            }
        }
    }
}

@Composable
fun ChatMessage(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER
    val alignment: Alignment = if (isUser) Alignment.BottomEnd else Alignment.BottomStart
    val backgroundColor =
        if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val textColor =
        if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = message.text,
            color = textColor,
            modifier = Modifier
                .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        )
    }
}
