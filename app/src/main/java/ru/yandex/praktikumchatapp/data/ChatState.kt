package ru.yandex.praktikumchatapp.data

import ru.yandex.praktikumchatapp.presentation.Message

data class ChatState(var messages: List<Message>, var shouldShowKeyboard: Boolean)