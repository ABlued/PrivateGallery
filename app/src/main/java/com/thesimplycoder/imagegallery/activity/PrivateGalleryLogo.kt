package com.thesimplycoder.imagegallery.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.thesimplycoder.imagegallery.R
import java.io.File
import java.util.*
import kotlin.concurrent.timer


class PrivateGalleryLogo : AppCompatActivity() {
    private var time = 0
    private var timerTask : Timer? = null
    private val PRIVATE_GALLERY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/.photo/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_gallery_logo)
        val intent = Intent(this,
            MainActivity::class.java)
        //'.photo' 폴더 존재하지 않으면 폴더 생성
        val dir = File(PRIVATE_GALLERY_PATH)
        if(!dir.exists()){
            dir.mkdirs()
        }
        timerTask = timer(period = 10){
            time++
            if(time == 200)                //2초가 지나면 화면이 넘어감
                startActivity(intent)
        }
    }
}
