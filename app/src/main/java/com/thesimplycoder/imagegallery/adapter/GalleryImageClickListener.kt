package com.thesimplycoder.imagegallery.adapter

import android.net.Uri

interface GalleryImageClickListener {

    fun onClick(position: Int)
    fun onLongClick(position: Int,imageurl:String, imagetitle:String):Boolean
}