package com.example.quizapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.quizapp.model.quiz.repository.QuizRepository
import com.example.quizapp.view.custom.internet
import com.example.quizapp.view.internet.checkInternetConnection
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class QuizViewModel : ViewModel() {
    private var checking = 0

    private val _getQuizUIState = MutableStateFlow<QuizUIState>(QuizUIState.Error)
    val getQuizUIState: StateFlow<QuizUIState> = _getQuizUIState

    sealed class QuizUIState {
        data class Success(val quiz: HttpResponse) : QuizUIState()
        data object Error : QuizUIState()
        data object Cancel : QuizUIState()
    }

    fun getQuiz(category: String, level: String, context: Context) {
        runBlocking {
            val call = QuizRepository()
                .getQuiz(
                    category.replace(" ", "_").lowercase(),
                    level.lowercase()
                )
            checking = 0
            while (checking == 0) {
                internet.value = checkInternetConnection(context)
                if (internet.value) {
                    if (call.status.value in 200..299) {
                        _getQuizUIState.value = QuizUIState.Success(call)
                        checking++
                    } else {
                        _getQuizUIState.value = QuizUIState.Error
                        checking++
                    }
                } else {
                    _getQuizUIState.value = QuizUIState.Cancel
                    checking++
                }
            }
        }
    }
}