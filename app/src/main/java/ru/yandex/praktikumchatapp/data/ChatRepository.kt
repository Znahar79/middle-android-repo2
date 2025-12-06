package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import ru.yandex.praktikumchatapp.common.powerBaseTwo
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            val maxAttempts = 3
            val initialDelay = 1000L

            if (cause is Exception && attempt < maxAttempts) {
                val delayTime = initialDelay * powerBaseTwo(attempt)
                println("Повтор запроса через $delayTime мс (попытка ${attempt + 1})")
                delay(delayTime)
                true
            } else {
                false
            }
        }
    }
}