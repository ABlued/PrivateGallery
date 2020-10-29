package com.thesimplycoder.imagegallery.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.thesimplycoder.imagegallery.R
import com.thesimplycoder.imagegallery.adapter.GalleryImageAdapter
import com.thesimplycoder.imagegallery.adapter.GalleryImageClickListener
import com.thesimplycoder.imagegallery.adapter.Image
import com.thesimplycoder.imagegallery.fragment.GalleryFullscreenFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), GalleryImageClickListener {

    // gallery column count
    private val SPAN_COUNT = 2
    private val OPEN_GALLLERY = 1
    private val imageList = ArrayList<Image>()
    private val PRIVATE_GALLERY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/.photo/"
    lateinit var galleryAdapter: GalleryImageAdapter
    fun saveInt(Key: String, Value: Int) {
        val pref = getSharedPreferences("passwd", Context.MODE_PRIVATE)
        val ed = pref.edit()
        ed.putInt(Key, Value)
        ed.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 어댑터 초기화
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        // 리사이클러뷰 초기화
        recyclerView.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerView.adapter = galleryAdapter


        btn_password.setOnClickListener() {
            val dlgView = layoutInflater.inflate(R.layout.dialog, null)
            val dlgBuilder = AlertDialog.Builder(this)
            val et1 = dlgView.findViewById<EditText>(R.id.dlgEdt1)
            val et2 = dlgView.findViewById<EditText>(R.id.dlgEdt2)


            dlgBuilder.setTitle("비밀번호 수정")
            dlgBuilder.setView(dlgView)
            dlgBuilder.setPositiveButton("확인") { dialogInterface, i ->
                var value = et1.getText().toString().toInt()
                var value2 = et2.getText().toString().toInt()
                val toast = Toast(this)
                val toastView = layoutInflater.inflate(R.layout.toast, null);
                val toastTextView = toastView.findViewById<TextView>(R.id.toastText)
                if(value == value2) {
                    toastTextView.text = "수정되었습니다."
                    toast.setView(toastView)
                    toast.show()
                    saveInt("passwd", value)
                }
                else{
                    toastTextView.text = "실패하였습니다."
                    toast.setView(toastView)
                    toast.show()
                }
            }.setNegativeButton("취소") { dialogInterface, i ->
                val toast = Toast(this)
                val toastView = layoutInflater.inflate(R.layout.toast, null);
                val toastTextView = toastView.findViewById<TextView>(R.id.toastText)
                toastTextView.text = "취소했습니다."
                toast.setView(toastView)
                toast.show()
            }.show()
        }

        getPrivatePhotos()
        btn_album.setOnClickListener { openGallery() }
    }


    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(requestCode== OPEN_GALLLERY){
                var currentImageUrl: Uri?=data?.data
                try {
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    Log.d("path","${currentImageUrl.toString()}")
                    val path: String = getPath(currentImageUrl)
                    Log.d("path" ,"$path")
                    val destPath: String = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/.photo/photo_$timeStamp.jpg"
                    val fileSrc = File(path)
                    val fileDest = File(destPath)
                    fileSrc.copyTo(fileDest,true)
                    imageList.add(Image(currentImageUrl.toString(), "photo_$timeStamp"))
                    galleryAdapter.notifyDataSetChanged()
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }else{
            Log.d("ActivityResult","something wrong")
        }
    }
    private fun openGallery(){
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,OPEN_GALLLERY)
    }
    private fun getPrivatePhotos(){

        var privateFileList = ArrayList<File>()
        privateFileList = getListFiles(File(PRIVATE_GALLERY_PATH))

        privateFileList.forEach{
            imageList.add(Image(it.toString(), "${it.name}"))
            galleryAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(position: Int) {
        // handle click of image

        val bundle = Bundle()
        bundle.putSerializable("images", imageList)
        bundle.putInt("position", position)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val galleryFragment = GalleryFullscreenFragment()
        galleryFragment.setArguments(bundle)
        galleryFragment.show(fragmentTransaction, "gallery")
    }

    override fun onLongClick(position: Int, imageurl: String, imagetitle: String): Boolean {
        alert("사진을 삭제하시겠습니까?", "사진삭제"){
            yesButton{
                imageList.remove(Image(imageurl,"$imagetitle"))
                galleryAdapter.notifyDataSetChanged()
                val file = File(imageurl)
                file.delete()
            }
            noButton{}
        }.show()
        return true
    }

    private fun getListFiles(parentDir: File): ArrayList<File> {
        Log.e("getpath: ", parentDir.toString())
        var inFiles = ArrayList<File>()
        var files: Array<File>
        files = parentDir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.name.endsWith(".jpg") ||file.name.endsWith(".jpeg") || file.name.endsWith(".gif")|| file.name.endsWith(".png")){
                    if (!inFiles.contains(file))
                        inFiles.add(file)
                }
            }
            Log.e("getListFiles: ", java.lang.String.valueOf(inFiles))
        }
        return inFiles
    }

    private fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val realPath = cursor.getString(columnIndex)
        cursor.close()
        return realPath
    }
}