package com.example.odm.garbagesorthelper.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.odm.garbagesorthelper.R
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    companion object{
        val TAG = "WelcomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

    }

    override fun onStart() {
        super.onStart()
        initUserPermissions()
    }

    fun initUserPermissions(){
        val rxPermissions = RxPermissions(this)
        rxPermissions.request( Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    //检查权限是否获取，提醒用户
                    if (aBoolean) {
                        Log.e(WelcomeActivity.TAG, "动态申请 文件读写权限回调 true")
                        //成功获取权限则跳转到主页面
                        startActivity(Intent(this,RootActivity::class.java))
                    } else {
                        Log.e(WelcomeActivity.TAG, "动态申请 文件读写权限回调 false")
                        Toast.makeText(this, "未授权应用相关权限，将无法使用拍照识别功能！", Toast.LENGTH_LONG).show()
                    }
                }.isDisposed
    }
}
