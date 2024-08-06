package com.example.mygemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val sender: MessageSender)

enum class MessageSender {
    USER, AI
}


data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val uiState: UiState = UiState.Loading,
    val initial: UiState = UiState.Initial
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    init {
        _uiState.value = _uiState.value.copy(
            uiState = UiState.Initial
        )
    }


    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = BuildConfig.API_KEY
    )

    fun sendPrompt(prompt: String) {
        _uiState.value = _uiState.value.copy(
            uiState = UiState.Loading,
            messages = _uiState.value.messages + ChatMessage(prompt, MessageSender.USER)
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = _uiState.value.copy(
                        uiState = UiState.Success(outputContent),
                        messages = _uiState.value.messages + ChatMessage(outputContent, MessageSender.AI)
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(
                        uiState = UiState.Error("No response from AI")
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    uiState = UiState.Error(e.localizedMessage ?: "Unknown error")
                )
            }
        }
    }
}
