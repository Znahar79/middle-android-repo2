package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()
    private val _messagesFlow = MutableStateFlow<List<Message>>(emptyList())
    val messagesFlow: StateFlow<List<Message>> = _messagesFlow
    private val _shouldShowKeyboard = MutableStateFlow(false)
    val shouldShowKeyboard: StateFlow<Boolean> = _shouldShowKeyboard.asStateFlow()

    // TODO Задание 4: замените messages и shouldShowKeyboard на state

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->
                    _shouldShowKeyboard.value = true
                    val currentMessages = _messagesFlow.value
                    _messagesFlow.value =
                        (currentMessages + Message.OtherMessage(response))

                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        val currentMessages = _messagesFlow.value
        _messagesFlow.value = (currentMessages + Message.MyMessage(messageText))
    }
}