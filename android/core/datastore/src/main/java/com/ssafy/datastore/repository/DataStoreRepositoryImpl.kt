package com.ssafy.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DataStoreRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
    ) : DataStoreRepository {
        override suspend fun saveAccessToken(token: String) {
            dataStore.edit { prefs ->
                prefs[ACCESS_TOKEN_KEY] = token
            }
        }

        override suspend fun deleteAccessToken() {
            dataStore.edit { prefs ->
                prefs.remove(ACCESS_TOKEN_KEY)
            }
        }

        override suspend fun getAccessToken(): String? =
            dataStore.data
                .firstOrNull { prefs ->
                    prefs.contains(
                        ACCESS_TOKEN_KEY,
                    )
                }?.get(ACCESS_TOKEN_KEY)

        companion object {
            private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        }
    }
