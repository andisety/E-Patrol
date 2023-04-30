package com.andi.epatrol

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object Epatrol {
    fun decoderBitmap(encode:String?): Bitmap {
        val imageAsByte: ByteArray = Base64.decode(encode?.toByteArray(), Base64.DEFAULT)
        val bm = BitmapFactory.decodeByteArray(imageAsByte,0,imageAsByte.size)
        return bm
    }

}