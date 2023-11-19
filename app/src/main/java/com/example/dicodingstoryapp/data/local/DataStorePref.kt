package com.example.dicodingstoryapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile



class DataStorePref(val context: Context) {

    private val dispatchers = Dispatchers.IO

    private val JWT_KEY = "JWT_TOKEN"


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = JWT_KEY)


    suspend fun saveToken(token: String) {

        context.dataStore.edit { jwtToken ->

            jwtToken[TOKEN] = token
        }
    }

    fun readToken(): String? {
        return runBlocking {
            withContext(dispatchers) {
                // Access the data synchronously
                val data = context.dataStore.data.first()

                // Retrieve the token from the data
                data[PreferencesKeys.TOKEN]
            }
        }
    }

    suspend fun deleteToken(): String {
        context.dataStore.edit { jwtToken ->
            jwtToken.clear()
        }
        return "Token Deleted"
    }


    private object PreferencesKeys {
        val TOKEN = stringPreferencesKey("TOKEN")

    }


    companion object {
        private val TOKEN = stringPreferencesKey("TOKEN")

        @Volatile
        private var instance: DataStorePref? = null

        fun getInstance(context: Context): DataStorePref {
            return instance ?: DataStorePref(context).also { instance = it }
        }
    }


}