package com.example.quizapp.di

import androidx.room.Room
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.network.instance.Instance
import com.example.quizapp.data.repository.AuthRepositoryImpl
import com.example.quizapp.data.repository.GoogleAuthServiceImpl
import com.example.quizapp.data.repository.QuizRepositoryImpl
import com.example.quizapp.data.repository.ResultsRepositoryImpl
import com.example.quizapp.data.repository.UserRepositoryImpl
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.repository.GoogleAuthService
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.repository.ResultsRepository
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.usecase.FetchBadgeImageUseCase
import com.example.quizapp.domain.usecase.FetchBadgeLevelUseCase
import com.example.quizapp.domain.usecase.FetchBadgeUseCase
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.FormatProgressPercentageUseCase
import com.example.quizapp.domain.usecase.FormatQuizUseCase
import com.example.quizapp.domain.usecase.GetQuizUseCase
import com.example.quizapp.domain.usecase.InsertResultsUseCase
import com.example.quizapp.domain.usecase.InsertUserUseCase
import com.example.quizapp.domain.usecase.UpdateBadgeUseCase
import com.example.quizapp.domain.usecase.UpdatePointsUseCase
import com.example.quizapp.domain.usecase.UpdateResultsUseCase
import com.example.quizapp.presentation.screens.profile.ProfileViewModel
import com.example.quizapp.presentation.screens.progress.ProgressViewModel
import com.example.quizapp.presentation.screens.quiz.QuizViewModel
import com.example.quizapp.presentation.screens.quizResult.QuizResultViewModel
import com.example.quizapp.presentation.screens.createUser.CreateUserViewModel
import com.example.quizapp.presentation.screens.login.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
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
    single { FirebaseAuth.getInstance() }
    single { Firebase.firestore }
}

val repositoryModule = module {
    single<ResultsRepository> { ResultsRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<QuizRepository> { QuizRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<GoogleAuthService> { GoogleAuthServiceImpl(androidContext()) }
}

val viewModelModule = module {
    viewModel { QuizViewModel(get(), get(), get(), get()) }
    viewModel { CreateUserViewModel(get(), get()) }
    viewModel { ProgressViewModel(get(), get()) }
    viewModel { QuizResultViewModel(get(), get(), get(), get(), get(), androidContext()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }

}

val useCaseModule = module {
    factory { GetQuizUseCase(get()) }
    factory { FormatQuizUseCase() }
    factory { FetchUserUseCase(get()) }
    factory { FormatProgressPercentageUseCase() }
    factory { UpdateResultsUseCase(get()) }
    factory { UpdatePointsUseCase(get()) }
    factory { FetchBadgeImageUseCase() }
    factory { FetchBadgeLevelUseCase() }
    factory { FetchBadgeUseCase() }
    factory { UpdateBadgeUseCase(get()) }
    factory { InsertResultsUseCase(get()) }
    factory { InsertUserUseCase(get()) }
}