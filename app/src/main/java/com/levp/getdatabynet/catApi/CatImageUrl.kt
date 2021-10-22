package com.levp.getdatabynet.catApi

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class CatImageUrl(
    @SerializedName("file")
    val file:String
)
