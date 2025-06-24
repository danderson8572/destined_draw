package com.mtg.randomcard.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory   // ← add
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val DS_NAME = "history_store"
private val Context.historyDataStore by preferencesDataStore(DS_NAME)

object HistoryStore {
    private val KEY = stringPreferencesKey("cards_json")

    /** Moshi that **knows** about Kotlin classes */
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())        // ← add
        .build()

    private val listType = Types.newParameterizedType(List::class.java, CardDto::class.java)
    private val adapter  = moshi.adapter<List<CardDto>>(listType)

    fun flow(ctx: Context): Flow<List<CardDto>> =
        ctx.historyDataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map  { pref -> pref[KEY]?.let { adapter.fromJson(it) } ?: emptyList() }

    suspend fun save(ctx: Context, cards: List<CardDto>) {
        ctx.historyDataStore.edit { it[KEY] = adapter.toJson(cards) }
    }
}
