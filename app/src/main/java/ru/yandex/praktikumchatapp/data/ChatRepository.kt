package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import ru.yandex.praktikumchatapp.common.INITIAL_DELAY
import ru.yandex.praktikumchatapp.common.powerBaseTwo

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->

            if (cause is Exception) {
                val delayTime = INITIAL_DELAY * powerBaseTwo(attempt)
                delay(delayTime)
                true
            } else {
                false
            }
        }
    }
}