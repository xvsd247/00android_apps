package cn.cxy.contentproviderdemo

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri

class MyContentProvider : ContentProvider() {
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val authority = "cn.cxy.contentproviderdemo.provider"
    private val allUsers = 1 //返回多个记录的匹配码
    private val singleUser = 2 //返回单个记录的匹配码
    private lateinit var dbHelper: DbHelper
    private val userTable = "user_table"

    /**
     * 配置本应用支持的路径后缀
     */
    init {
        uriMatcher.addURI(authority, "user", allUsers);
        uriMatcher.addURI(authority, "user/#", singleUser);
    }

    /**
     * 创建存储数据的数据库
     */
    override fun onCreate(): Boolean {
        dbHelper = DbHelper(context, "userTable", 1)
        return true
    }

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        singleUser -> "vnd.android.cursor.item/vnd.cn.cxy.contentproviderdemo.provider.user"
        allUsers -> "vnd.android.cursor.dir/vnd.cn.cxy.contentproviderdemo.provider.user"
        else -> null
    }

    /**
     * 插入数据
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var resultUri: Uri? = null
        when (uriMatcher.match(uri)) {
            singleUser, allUsers -> {
                var userId = dbHelper.insert(userTable, values!!)
                resultUri = Uri.parse("content://$authority/user/${userId}")
            }
        }

        return resultUri
    }

    /**
     * 更新数据
     */
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = dbHelper.update("user_table", values!!, null, null)

    /**
     * 删除数据
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (uriMatcher.match(uri)) {
            singleUser, allUsers -> {
                dbHelper.deleteAll(userTable)
            }
        }
        return 0
    }

    /**
     * 查询数据
     */
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ) = dbHelper.query("user_table")
}