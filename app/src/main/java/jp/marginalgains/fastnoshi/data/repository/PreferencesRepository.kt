package jp.marginalgains.fastnoshi.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val defaultFontId: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_FONT_ID] ?: DEFAULT_FONT_ID
    }

    val defaultPaperSize: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_PAPER_SIZE] ?: DEFAULT_PAPER_SIZE
    }

    suspend fun setDefaultFontId(fontId: String) {
        dataStore.edit { prefs -> prefs[KEY_DEFAULT_FONT_ID] = fontId }
    }

    suspend fun setDefaultPaperSize(paperSize: String) {
        dataStore.edit { prefs -> prefs[KEY_DEFAULT_PAPER_SIZE] = paperSize }
    }

    companion object {
        private val KEY_DEFAULT_FONT_ID = stringPreferencesKey("default_font_id")
        private val KEY_DEFAULT_PAPER_SIZE = stringPreferencesKey("default_paper_size")
        const val DEFAULT_FONT_ID = "mincho"
        const val DEFAULT_PAPER_SIZE = "A4"
    }
}
