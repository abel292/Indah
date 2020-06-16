package com.android_abel.indah.utils.intent

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class IntentUtils {
    companion object Factory {

        private const val TAG = "ImagePicker"
        lateinit var currentPhotoPath: String
        lateinit var newFileUri:Uri

        fun getIntent(origen:String, context: Context):Intent?{
            var takePictureIntent:Intent?=null
            if(origen.equals("camera")){
                var photoURI=
                    createPhoto(
                        context
                    )
                newFileUri =photoURI
                takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.putExtra("return-data", true)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

            }
            else if(origen.equals("archive")){
                //ABRIR ARCHIVOS EXCEPTO CAMARA
                val mimetypes = arrayOf(
                    "application/*",
                    "audio/*",
                    "font/*",  //"image/*",
                    "message/*",
                    "model/*",
                    "multipart/*",
                    "text/*"
                )
                takePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
                takePictureIntent.type = "*/*"
                takePictureIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes)


            }
            else if(origen.equals("galery")){
                takePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
                takePictureIntent.type = "image/*"

            }
            else if(origen.equals("pdf")){
                takePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
                takePictureIntent.type = "application/pdf"

            }

            return takePictureIntent
        }

        fun createPhoto(context: Context): Uri {
            var photo:Uri?=null
            val photoFile: File? = try {
                createImageFile(
                    context
                )
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context.applicationContext,
                    "com.indra.applicability",
                    it
                )
                photo=photoURI

            }
            return photo!!
        }


        @Throws(IOException::class)
         fun createImageFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }

        @Throws(IOException::class)
        fun decodeSampledBitmapFromFile(image: String, reqWidth: Int, reqHeight: Int): Bitmap { // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(image, options)
            // Calculate inSampleSize
            options.inSampleSize =
                calculateInSampleSize(
                    options,
                    reqWidth,
                    reqHeight
                )
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            var scaledBitmap =
                BitmapFactory.decodeFile(image, options)
            //check the rotation of the image and display it properly
            val exif: ExifInterface
            exif = ExifInterface(image)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
                matrix, true
            )
            return scaledBitmap
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int { // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
// height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }


    }
}


