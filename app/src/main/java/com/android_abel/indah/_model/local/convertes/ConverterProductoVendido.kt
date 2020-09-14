package com.android_abel.indah._model.local.convertes

import androidx.room.TypeConverter
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ConverterProductoVendido {
    @TypeConverter
    fun fromCountryLangList(countryLang: List<ProductoVendidoEntity>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type =
                object : TypeToken<List<ProductoVendidoEntity>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<ProductoVendidoEntity>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type =
                object : TypeToken<List<ProductoVendidoEntity>?>() {}.type
        return gson.fromJson(countryLangString, type)
    }
}