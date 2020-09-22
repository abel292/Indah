package com.android_abel.indah._model.repositories

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android_abel.indah._model.local.producto.ProductoEntity
import com.google.android.gms.tasks.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


class FirebaseRepository(context: Context) : BaseRepository(context) {

    private var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseStorage = FirebaseStorage.getInstance().reference

    companion object {
        //TODO CONEXION A FIREBASE
        private const val DATA_USUARIOS = "usuariosData"
        private const val DATA_PRUDUCTOS = "productosData"
    }


    fun registrarProducto(producto: ProductoEntity) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = mDatabase.child(DATA_PRUDUCTOS).push().key
        if (key != null) {
            mDatabase.child(DATA_PRUDUCTOS).child(key).setValue(producto)
        }
    }

    fun uploadImage(keyProducto: String, bitmap: Bitmap) {
        val ref: StorageReference = firebaseStorage.child("productos/$keyProducto.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val data = baos.toByteArray()
        val uploadTask = ref.putBytes(data)
        uploadTask.addOnSuccessListener {
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downUri: Uri? = task.result
                    Log.d("Final URL", "onComplete: Url: $downUri")
                }
            }
        }.addOnFailureListener { e -> Log.d("Final URL", "onComplete: Url: ${e.message}") }
    }
}
