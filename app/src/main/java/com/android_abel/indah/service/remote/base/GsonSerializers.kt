package com.android_abel.indah.service.remote.base

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

object GsonSerializers {
    @JvmField
    var serializerDateToTimeLong =
        JsonSerializer<Date> { src: Date?, typeOfSrc: Type?, context: JsonSerializationContext? ->
            if (src == null) null else JsonPrimitive(src.time)
        }
    var deserializerDateToTimeLong =
        JsonDeserializer { json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext? ->
            if (json == null) null else Date(json.asLong)
        }
}