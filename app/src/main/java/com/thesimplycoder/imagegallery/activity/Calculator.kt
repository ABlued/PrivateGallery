package com.thesimplycoder.imagegallery.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.thesimplycoder.imagegallery.R
import kotlinx.android.synthetic.main.activity_calculator.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


class Calculator : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        // 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                alert("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.", "권한이 필요한 이유"){
                    yesButton{
                        ActivityCompat.requestPermissions(this@Calculator,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton{}
                }.show()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                alert("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.", "권한이 필요한 이유"){
                    yesButton{
                        ActivityCompat.requestPermissions(this@Calculator,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_WRITE_EXTERNAL_STORAGE)
                    }
                    noButton{}
                }.show()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE)
            }
        }

        var targetInt = getSharedPreferences("passwd",Context.MODE_PRIVATE)
        var passwd =targetInt.getInt("passwd",0)
        var input1 : String? = null
        var input2 : String? = null
        val btnArray = arrayOf<Button>(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9)
        val intent = Intent(this,
            PrivateGalleryLogo::class.java)


        for(i in 0..9){                                 //0부터 9까지의 버튼 이벤트 리스너
            btnArray[i].setOnClickListener{
                if(edit1.isFocused() == true){
                    input1 = edit1.text.toString() + btnArray[i].text.toString()
                    edit1.setText(input1)
                }
                else{
                    input2 = edit2.text.toString() + btnArray[i].text.toString()
                    edit2.setText(input2)
                }
            }
        }

        edit1.setOnTouchListener { v, event ->      //edit1을 터치 시 하단에 키보드가 안나오게 하는 터치리스너
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    CloseKeyboard()
                    edit1.requestFocus()
                }
            }
            true
        }
        edit2.setOnTouchListener { v, event ->      //edit2을 터치 시 하단에 키보드가 안나오게 하는 터치리스너
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    CloseKeyboard()
                    edit2.requestFocus()
                }
            }
            true
        }

        buttonAdd.setOnClickListener{      //더하기 버튼 이벤트 리스너
            input1 = edit1.text.toString()
            input2 = edit2.text.toString()
            if(input1 == "" || input2 == ""){
                Toast.makeText(this,"피연산자를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                result.text = "계산 결과 : " + (input1!!.toDouble() + input2!!.toDouble()).toString()
                if(result.text.toString() == "계산 결과 : "+passwd+".0"){             //일단 비밀번호는 0으로 대체되었다.
                    startActivity(intent)
                }
            }
        }

        buttonSub.setOnClickListener{      //빼기 버튼 이벤트 리스너
            input1 = edit1.text.toString()
            input2 = edit2.text.toString()
            if(input1 == "" || input2 == ""){
                Toast.makeText(this,"피연산자를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                result.text = "계산 결과 : " + (input1!!.toDouble() - input2!!.toDouble()).toString()
                if(result.text.toString() == "계산 결과 : "+passwd+".0"){
                    startActivity(intent)
                }
            }
        }

        buttonMul.setOnClickListener{      //곱하기 버튼 이벤트 리스너
            input1 = edit1.text.toString()
            input2 = edit2.text.toString()
            if(input1 == "" || input2 == ""){
                Toast.makeText(this,"피연산자를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                result.text = "계산 결과 : " + (input1!!.toDouble() * input2!!.toDouble()).toString()
                if(result.text.toString() == "계산 결과 : "+passwd+".0"){
                    startActivity(intent)
                }
            }
        }

        buttonDiv.setOnClickListener{      //나누기 버튼 이벤트 리스너
            input1 = edit1.text.toString()
            input2 = edit2.text.toString()
            if(input1 == "" || input2 == ""){
                Toast.makeText(this,"피연산자를 입력하지 않았습니다", Toast.LENGTH_SHORT).show()
            }
            else if(input2 == "0") {
                Toast.makeText(this,"두번째 피연산자가 0입니다", Toast.LENGTH_SHORT).show()
            }
            else {
                result.text = "계산 결과 : " + (input1!!.toDouble() / input2!!.toDouble()).toString()
                if(result.text.toString() == "계산 결과 : "+passwd+".0"){
                    startActivity(intent)
                }
            }
        }

        buttonDel.setOnClickListener {      //숫자 하나 지우기 버튼 이벤트 리스너
            if(edit1.isFocused() == true) {
                if(edit1.text.toString() == ""){
                    Toast.makeText(this,"피연산자를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    var ary =
                        Array<String>(edit1.text.length - 1, { i -> edit1.text.get(i).toString() })
                    edit1.setText("")
                    var arySize = ary.size
                    for (i in 0..arySize - 1) {
                        edit1.setText(edit1.text.toString() + ary.get(i))
                    }
                }
            }
            else{
                if(edit2.text.toString() == ""){
                    Toast.makeText(this,"피연산자를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    var ary =
                        Array<String>(edit2.text.length - 1, { i -> edit2.text.get(i).toString() })
                    edit2.setText("")
                    var arySize = ary.size
                    for (i in 0..arySize - 1) {
                        edit2.setText(edit2.text.toString() + ary.get(i))
                    }
                }
            }
        }

        buttonAlldel.setOnClickListener {      //모든 숫자 지우기 버튼 이벤트 리스너
            edit1.setText("")
            edit2.setText("")
        }
    }
    fun CloseKeyboard()                         //하단에 나오는 키보드 제거 함수
    {
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE->{
                if((grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                    return
                }else{
                    toast("권한 거부 됨")
                    return
                }
            }
            REQUEST_WRITE_EXTERNAL_STORAGE->{
                if((grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                    return
                }else{
                    toast("권한 거부 됨")
                    return
                }
            }
        }
    }

}
