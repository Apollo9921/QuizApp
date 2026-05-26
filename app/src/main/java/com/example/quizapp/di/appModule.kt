package com.example.quizapp.di

import androidx.room.Room
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.repository.ResultsRepositoryImpl
import com.example.quizapp.data.repository.UserRepositoryImpl
import com.example.quizapp.viewModel.ProgressViewModel
import com.example.quizapp.viewModel.QuizViewModel
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
    single { HttpClient(Android) }
}

val repositoryModule = module {
    single { ResultsRepositoryImpl(get()) }
    single { UserRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { QuizViewModel() }
    viewModel { ResultsViewModel(androidContext()) }
    viewModel { UserViewModel(get()) }
    viewModel { ProgressViewModel(get()) }
}