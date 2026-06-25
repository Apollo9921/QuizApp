package com.apollo9921.quizrise.presentation.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreUser: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserManager(val dataStore: DataStore<Preferences>) {

    companion object {
        val IS_LOADED_KEY = booleanPreferencesKey("IS_LOADED")
    }

    suspend fun storeToDataStore(isLoaded: Boolean) {
        dataStore.edit {
            it[IS_LOADED_KEY] = isLoaded
        }
    }

    val userFlow: Flow<Boolean> = dataStore.data.map {
        (it[IS_LOADED_KEY] == true)
    }
}