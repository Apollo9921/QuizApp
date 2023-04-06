package com.example.quizapp.view.dataStore

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
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    }

    suspend fun storeToDataStore(isLoaded: Boolean, userName: String) {
        dataStore.edit {
            it[IS_LOADED_KEY] = isLoaded
            it[USER_NAME_KEY] = userName
        }
    }

    val userFlow: Flow<Boolean> = dataStore.data.map {
        if (it[IS_LOADED_KEY] != null) {
            it[IS_LOADED_KEY]
        } else {
           false
        }!!
    }
    val userName: Flow<String?> = dataStore.data.map {
        if (it[USER_NAME_KEY] != null) {
            it[USER_NAME_KEY]
        } else {
            ""
        }!!
    }
}