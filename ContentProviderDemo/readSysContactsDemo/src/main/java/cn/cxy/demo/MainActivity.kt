package cn.cxy.demo

import android.Manifest
import android.os.Bundle
import android.provider.ContactsContract
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
     * 先申请读取系统通讯录权限，再进行读取。
     */
    private fun readContactsWithPermission() {
        SoulPermission.getInstance()
            .checkAndRequestPermission(
                Manifest.permission.READ_CONTACTS,
                object : CheckRequestPermissionListener {
                    override fun onPermissionOk(permission: Permission) {
                        readContacts()
                    }

                    override fun onPermissionDenied(permission: Permission) {
                        toast("请授予权限")
                    }
                })
    }

    /**
     * 读取系统通讯录并展示总数和其中一个联系人的姓名与电话。
     */
    private fun readContacts() {
        val contacts: ArrayList<String> = getContactsInfo()
        contacts.forEach {
            Log.d("MainActivity", "联系人信息：$it")
        }
    }

    /**
     * 读取系统通讯录信息
     */
    private fun getContactsInfo(): ArrayList<String> {
        val dataList = ArrayList<String>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                dataList.add("$displayName:$phone")
            }
            cursor.close()
        }

        return dataList
    }

    private fun toast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}