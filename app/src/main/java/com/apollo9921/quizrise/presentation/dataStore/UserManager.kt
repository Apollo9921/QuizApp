package com.apollo9921.quizrise.presentation.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStoreUser: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserManager(val dataStore: DataStore<Preferences>) {

    companion object {
        val IS_LOADED_KEY = booleanPreferencesKey("IS_LOADED")
        val Quiz_ALLOWED_ANONYMOUSLY_KEY = intPreferencesKey("Quiz_ALLOWED_ANONYMOUSLY")
    }

    suspend fun storeToDataStore(isLoaded: Boolean) {
        dataStore.edit {
            it[IS_LOADED_KEY] = isLoaded
        }
    }

    suspend fun storeQuizAllowed() {
        dataStore.edit {
            it[Quiz_ALLOWED_ANONYMOUSLY_KEY] = 3
        }
    }

    suspend fun updateQuizAllowed() {
        dataStore.edit {
            it[Quiz_ALLOWED_ANONYMOUSLY_KEY] = quizAllowedFlow.first() - 1
        }
    }

    val userFlow: Flow<Boolean> = dataStore.data.map {
        (it[IS_LOADED_KEY] == true)
    }

    val quizAllowedFlow: Flow<Int> = dataStore.data.map {
        it[Quiz_ALLOWED_ANONYMOUSLY_KEY] ?: 0
    }
}