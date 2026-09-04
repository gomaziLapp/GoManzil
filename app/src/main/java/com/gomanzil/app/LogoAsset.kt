package com.gomanzil.app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object LogoAsset {
    private const val DATA = "PLACEHOLDER"
    fun bitmap(): Bitmap {
        val bytes = Base64.decode(DATA, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
