package com.example.quizapp.di

import androidx.room.Room
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.network.instance.Instance
import com.example.quizapp.data.repository.QuizRepositoryImpl
import com.example.quizapp.data.repository.ResultsRepositoryImpl
import com.example.quizapp.data.repository.UserRepositoryImpl
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.FormatProgressPercentageUseCase
import com.example.quizapp.domain.usecase.FormatQuizUseCase
import com.example.quizapp.domain.usecase.GetQuizUseCase
import com.example.quizapp.presentation.progress.ProgressViewModel
import com.example.quizapp.presentation.quiz.QuizViewModel
import com.example.quizapp.viewModel.ResultsViewModel
import com.example.quizapp.viewModel.UserViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatchersModule = module {
    single(named("ioDispatcher")) { Dispatchers.IO }
}

val localModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            QuizDatabase::class.java,
            "quiz_database"
        ).build()
    }

    single { get<QuizDatabase>().userDao() }
    single { get<QuizDatabase>().resultsDao() }
}

val networkModule = module {
    single { Instance }
    single { HttpClient(Android) }
}

val repositoryModule = module {
    single { ResultsRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<QuizRepository> { QuizRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { QuizViewModel(get(), get(), get(), get()) }
    viewModel { ResultsViewModel(androidContext()) }
    viewModel { UserViewModel(get()) }
    viewModel { ProgressViewModel(get(), get()) }
}

val useCaseModule = module {
    factory { GetQuizUseCase(get()) }
    factory { FormatQuizUseCase() }
    factory { FetchUserUseCase(get()) }
    factory { FormatProgressPercentageUseCase() }
}