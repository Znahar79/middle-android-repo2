import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.yandex.praktikumchatapp.presentation.ChatViewModel
import ru.yandex.praktikumchatapp.presentation.Message

@ExperimentalCoroutinesApi
class ChatViewModelTest {

    private var testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ChatViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(isWithReplies = false)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `send message should update state with MyMessage`() = runTest {
        val message = Message.MyMessage("TestMessage")
        viewModel.sendMyMessage(message.text)

        assertTrue(viewModel.chatState.value.messages.contains(message))
    }

    @Test
    fun testReceiveMessage_concurrentMessages() = runTest {
        val messagesCount = 100
        val messagesToSend = (1..messagesCount).map { Message.MyMessage("Message $it") }

        val jobs = mutableListOf<Job>()

        coroutineScope {
            messagesToSend.forEach { message ->
                val job = launch {
                    viewModel.sendMyMessage(message.text)
                }
                jobs.add(job)
            }

            jobs.joinAll()
        }

        val currentMessages = viewModel.chatState.value.messages

        assertEquals(messagesCount, currentMessages.size)

        messagesToSend.forEach { message ->
            var found = false
            for (i in 0 until currentMessages.size) {
                if (currentMessages[i] is Message.MyMessage &&
                    (currentMessages[i] as Message.MyMessage).text == message.text) {
                    found = true
                    break
                }
            }
            assertTrue(found)
        }
    }
}