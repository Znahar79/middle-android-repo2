package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            true
        }  // TODO Задание 2: добавьте обработку ошибок
    }
}