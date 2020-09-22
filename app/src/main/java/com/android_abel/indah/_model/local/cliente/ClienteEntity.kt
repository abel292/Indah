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
    var nombre: String? = null

    @ColumnInfo(name = "celular")
    var celular: String? = null

    @ColumnInfo(name = "direccion")
    var direccion: String? = null

    @ColumnInfo(name = "infoAdicional")
    var infoAdicional: String? = null

}