package com.android_abel.indah._model.local.cliente

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "_CLIENTES")
class ClienteEntity() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "nombre")
    lateinit var nombre: String

    @ColumnInfo(name = "celular")
    lateinit var celular: String

    @ColumnInfo(name = "direccion")
    lateinit var direccion: String

    @ColumnInfo(name = "infoAdicional")
    lateinit var infoAdicional: String

}