package com.android_abel.indah._model.repositories

import android.content.Context
import androidx.room.Dao
import com.android_abel.indah._model.local.DataBaseIndah

abstract class BaseRepository(context: Context) {
    var database = DataBaseIndah.getDatabase(context)

}