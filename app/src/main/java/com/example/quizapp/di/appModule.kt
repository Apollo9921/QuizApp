package com.example.quizapp.di

import androidx.room.Room
import androidx.work.WorkManager
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.network.instance.Instance
import com.example.quizapp.data.repository.AuthRepositoryImpl
import com.example.quizapp.data.repository.CloudQuizTranslatorImpl
import com.example.quizapp.data.repository.GoogleAuthServiceImpl
import com.example.quizapp.data.repository.LeaderboardRepositoryImpl
import com.example.quizapp.data.repository.QuizRepositoryImpl
import com.example.quizapp.data.repository.ResultsRepositoryImpl
import com.example.quizapp.data.repository.UserRepositoryImpl
import com.example.quizapp.domain.repository.AuthRepository
import com.example.quizapp.domain.repository.CloudQuizTranslator
import com.example.quizapp.domain.repository.GoogleAuthService
import com.example.quizapp.domain.repository.LeaderboardRepository
import com.example.quizapp.domain.repository.QuizRepository
import com.example.quizapp.domain.repository.ResultsRepository
import com.example.quizapp.domain.repository.UserRepository
import com.example.quizapp.domain.usecase.FetchBadgeImageUseCase
import com.example.quizapp.domain.usecase.FetchBadgeUseCase
import com.example.quizapp.domain.usecase.FetchUserUseCase
import com.example.quizapp.domain.usecase.FormatProgressPercentageUseCase
import com.example.quizapp.domain.usecase.FormatQuizUseCase
import com.example.quizapp.domain.usecase.GetQuizUseCase
import com.example.quizapp.domain.usecase.GetTopPlayersByCategoryUseCase
import com.example.quizapp.domain.usecase.GetTopPlayersByLevelUseCase
import com.example.quizapp.domain.usecase.InsertResultLocally
import com.example.quizapp.domain.usecase.InsertResultsUseCase
import com.example.quizapp.domain.usecase.InsertUserLocally
import com.example.quizapp.domain.usecase.InsertUserUseCase
import com.example.quizapp.domain.usecase.PostUserUseCase
import com.example.quizapp.domain.usecase.SaveUserToRemoteUseCase
import com.example.quizapp.domain.usecase.UpdateBadgeUseCase
import com.example.quizapp.domain.usecase.UpdatePointsUseCase
import com.example.quizapp.domain.usecase.UpdateResultsUseCase
import com.example.quizapp.domain.usecase.UpdateUserToRemoteUseCase
import com.example.quizapp.presentation.screens.leaderboard.LeaderboardViewModel
import com.example.quizapp.presentation.screens.profile.ProfileViewModel
import com.example.quizapp.presentation.screens.progress.ProgressViewModel
import com.example.quizapp.presentation.screens.quiz.QuizViewModel
import com.example.quizapp.presentation.screens.quizResult.QuizResultViewModel
import com.example.quizapp.presentation.screens.login.LoginViewModel
import com.example.quizapp.presentation.screens.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
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
    single { FirebaseFirestore.getInstance() }
    single { FirebaseFunctions.getInstance() }
    single { WorkManager.getInstance(androidContext()) }
}

val repositoryModule = module {
    single<ResultsRepository> { ResultsRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<QuizRepository> { QuizRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<GoogleAuthService> { GoogleAuthServiceImpl(androidContext()) }
    single<CloudQuizTranslator> { CloudQuizTranslatorImpl(get()) }
    single<LeaderboardRepository> { LeaderboardRepositoryImpl(androidContext(), get()) }
}

val viewModelModule = module {
    viewModel { QuizViewModel(get(), get(), get(), get()) }
    viewModel { ProgressViewModel(get(), get()) }
    viewModel { QuizResultViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LeaderboardViewModel(get(), get(), get()) }
}

val useCaseModule = module {
    factory { GetQuizUseCase(get(), androidContext()) }
    factory { FormatQuizUseCase(get()) }
    factory { FetchUserUseCase(get()) }
    factory { FormatProgressPercentageUseCase() }
    factory { UpdateResultsUseCase(androidContext(), get()) }
    factory { UpdatePointsUseCase(get()) }
    factory { FetchBadgeImageUseCase() }
    factory { FetchBadgeUseCase() }
    factory { UpdateBadgeUseCase(get()) }
    factory { InsertResultsUseCase(get()) }
    factory { InsertUserUseCase(get()) }
    factory { SaveUserToRemoteUseCase(get()) }
    factory { UpdateUserToRemoteUseCase(androidContext(), get()) }
    factory { PostUserUseCase(get()) }
    factory { InsertResultLocally(get()) }
    factory { InsertUserLocally(get()) }
    factory { GetTopPlayersByLevelUseCase(get()) }
    factory { GetTopPlayersByCategoryUseCase(get()) }
}