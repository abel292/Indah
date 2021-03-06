package com.android_abel.indah._model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android_abel.indah._model.local.cliente.ClienteEntity
import com.android_abel.indah._model.local.cliente.ClientesDao
import com.android_abel.indah._model.local.convertes.ConverterDate
import com.android_abel.indah._model.local.convertes.ConverterProductoVendido
import com.android_abel.indah._model.local.producto.ProductoDao
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.android_abel.indah._model.local.productoCarrito.DaoProductoVendido
import com.android_abel.indah._model.local.productoCarrito.ProductoVendidoEntity
import com.android_abel.indah._model.local.venta.VentaDao
import com.android_abel.indah._model.local.venta.VentaEntity


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(
    entities = [ProductoEntity::class,
        VentaEntity::class,
        ProductoVendidoEntity::class,
        ClienteEntity::class],
    version = 10
)
@TypeConverters(ConverterProductoVendido::class, ConverterDate::class)
public abstract class DataBaseIndah : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun ventaDao(): VentaDao
    abstract fun clientesDao(): ClientesDao
    abstract fun carritoDao(): DaoProductoVendido


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DataBaseIndah? = null

        fun getDatabase(context: Context): DataBaseIndah {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseIndah::class.java,
                    "indah_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}