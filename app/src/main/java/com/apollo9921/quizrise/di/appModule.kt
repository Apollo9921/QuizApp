package com.apollo9921.quizrise.di

import androidx.room.Room
import androidx.work.WorkManager
import com.apollo9921.quizrise.data.local.database.QuizDatabase
import com.apollo9921.quizrise.data.network.instance.Instance
import com.apollo9921.quizrise.data.repository.AuthRepositoryImpl
import com.apollo9921.quizrise.data.repository.CloudQuizTranslatorImpl
import com.apollo9921.quizrise.data.repository.GoogleAuthServiceImpl
import com.apollo9921.quizrise.data.repository.LeaderboardRepositoryImpl
import com.apollo9921.quizrise.data.repository.QuizRepositoryImpl
import com.apollo9921.quizrise.data.repository.ResultsRepositoryImpl
import com.apollo9921.quizrise.data.repository.UserRepositoryImpl
import com.apollo9921.quizrise.domain.repository.AuthRepository
import com.apollo9921.quizrise.domain.repository.CloudQuizTranslator
import com.apollo9921.quizrise.domain.repository.GoogleAuthService
import com.apollo9921.quizrise.domain.repository.LeaderboardRepository
import com.apollo9921.quizrise.domain.repository.QuizRepository
import com.apollo9921.quizrise.domain.repository.ResultsRepository
import com.apollo9921.quizrise.domain.repository.UserRepository
import com.apollo9921.quizrise.domain.usecase.ClearAllDataUseCase
import com.apollo9921.quizrise.domain.usecase.FetchBadgeImageUseCase
import com.apollo9921.quizrise.domain.usecase.FetchBadgeUseCase
import com.apollo9921.quizrise.domain.usecase.FetchResultsUseCase
import com.apollo9921.quizrise.domain.usecase.FetchUserUseCase
import com.apollo9921.quizrise.domain.usecase.FormatProgressPercentageUseCase
import com.apollo9921.quizrise.domain.usecase.FormatQuizUseCase
import com.apollo9921.quizrise.domain.usecase.GetQuizUseCase
import com.apollo9921.quizrise.domain.usecase.GetTopPlayersByCategoryUseCase
import com.apollo9921.quizrise.domain.usecase.GetTopPlayersByLevelUseCase
import com.apollo9921.quizrise.domain.usecase.InsertResultsUseCase
import com.apollo9921.quizrise.domain.usecase.InsertNewResultsUseCase
import com.apollo9921.quizrise.domain.usecase.InsertUserUseCase
import com.apollo9921.quizrise.domain.usecase.InsertNewUserUseCase
import com.apollo9921.quizrise.domain.usecase.PostSessionUseCase
import com.apollo9921.quizrise.domain.usecase.PostUserUseCase
import com.apollo9921.quizrise.domain.usecase.PostUserAndResultsUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateBadgeUseCase
import com.apollo9921.quizrise.domain.usecase.UpdatePointsUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateResultsUseCase
import com.apollo9921.quizrise.domain.usecase.UpdateUserAndResultsUseCase
import com.apollo9921.quizrise.presentation.screens.leaderboard.LeaderboardViewModel
import com.apollo9921.quizrise.presentation.screens.profile.ProfileViewModel
import com.apollo9921.quizrise.presentation.screens.progress.ProgressViewModel
import com.apollo9921.quizrise.presentation.screens.quiz.QuizViewModel
import com.apollo9921.quizrise.presentation.screens.quizResult.QuizResultViewModel
import com.apollo9921.quizrise.presentation.screens.login.LoginViewModel
import com.apollo9921.quizrise.presentation.screens.register.RegisterViewModel
import com.apollo9921.quizrise.presentation.screens.results.ResultsViewModel
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
    single<LeaderboardRepository> { LeaderboardRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { QuizViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProgressViewModel(get(), get()) }
    viewModel { QuizResultViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LeaderboardViewModel(get(), get(), get()) }
    viewModel { ResultsViewModel(get(), get()) }
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
    factory { InsertNewResultsUseCase(get()) }
    factory { InsertNewUserUseCase(get()) }
    factory { PostUserAndResultsUseCase(get()) }
    factory { UpdateUserAndResultsUseCase(androidContext(), get()) }
    factory { PostUserUseCase(get()) }
    factory { InsertResultsUseCase(get()) }
    factory { InsertUserUseCase(get()) }
    factory { GetTopPlayersByLevelUseCase(get()) }
    factory { GetTopPlayersByCategoryUseCase(get()) }
    factory { FetchResultsUseCase(get()) }
    factory { ClearAllDataUseCase(get(), get()) }
    factory { PostSessionUseCase(get()) }
}