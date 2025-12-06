package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository
import ru.yandex.praktikumchatapp.data.ChatState

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()
    private val _chatState = MutableStateFlow(ChatState(emptyList(), false))
    val chatState: StateFlow<ChatState> = _chatState

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->
                    val updatedMessages = _chatState.value.messages + Message.OtherMessage(response)
                    _chatState.value = _chatState.value.copy(messages = updatedMessages, shouldShowKeyboard = true)
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        val updatedMessages = _chatState.value.messages + Message.MyMessage(messageText)
        _chatState.value = _chatState.value.copy(messages = updatedMessages)
    }
}