package org.sochidrive.convertpng.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.sochidrive.convertpng.mvp.model.IConverter
import org.sochidrive.convertpng.mvp.model.Image
import java.io.File
import java.io.FileOutputStream

class Converter(val context: Context?) : IConverter {
    override fun convert(image: Image) = Completable.fromAction {

        context?.let { context ->
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                return@let
            }

            val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
            val dstFile = File(context.getExternalFilesDir(null), "converted.png")
            FileOutputStream(dstFile).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

    }.subscribeOn(Schedulers.io())

}