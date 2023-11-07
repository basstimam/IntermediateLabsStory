package com.example.dicodingstoryapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlin.concurrent.Volatile

class DataStorePref(val context: Context){

    private val JWT_KEY = "JWT_TOKEN"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = JWT_KEY)




suspend fun saveToken(token: String){

    context.dataStore.edit {
        jwtToken ->

        jwtToken[TOKEN] = token
    }
}

    suspend fun readToken(): String? {
        val jwtToken = context.dataStore.data.first()[TOKEN]
        return jwtToken
    }

    suspend fun deleteToken(): String{
        context.dataStore.edit {jwtToken ->
            jwtToken.clear()
        }
        return "Token Deleted"
    }

    fun closeDataStore(){


    }











    companion object{
        private  val TOKEN = stringPreferencesKey("TOKEN")

        @Volatile
        private var instance: DataStorePref? = null

        fun getInstance(context: Context): DataStorePref {
            return instance ?: DataStorePref(context).also { instance = it }
        }
    }


}