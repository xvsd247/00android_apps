package cn.cxy.demo

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //对按钮添加点击事件
        button.setOnClickListener {
            readContactsWithPermission()
        }
    }

    /**
     * 先申请读取短信权限，再进行读取。
     */
    private fun readContactsWithPermission() {
        SoulPermission.getInstance()
            .checkAndRequestPermission(
                Manifest.permission.READ_SMS,
                object : CheckRequestPermissionListener {
                    override fun onPermissionOk(permission: Permission) {
                        readSms()
                    }

                    override fun onPermissionDenied(permission: Permission) {
                        toast("请授予权限")
                    }
                })
    }

    /**
     * 读取短信并展示总数和其中一条短信内容。
     */
    private fun readSms() {
        val smsList: ArrayList<String> = getSmsInfo()
        smsList.forEach {
            Log.d("MainActivity", "短信内容：$it")
            toast(it)
        }
    }

    /**
     * 读取短信
     */
    private fun getSmsInfo(): ArrayList<String> {
        val dataList = ArrayList<String>()
        val uri = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val displayName = cursor.getString(cursor.getColumnIndex("body"))
                dataList.add(displayName)
            }
            cursor.close()
        }
        return dataList
    }

    private fun toast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}