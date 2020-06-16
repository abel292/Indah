package com.android_abel.indah._model.local.convertes

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.*

class ConverterDate {
    @TypeConverter
    fun fromCountryLangList(countryLang: Date?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type =
                object : TypeToken<Date?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): Date? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type =
                object : TypeToken<Date?>() {}.type
        return gson.fromJson(countryLangString, type)
    }
}