package com.zl.camerauvc_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_CALENDAR,
			Manifest.permission.READ_CALL_LOG,
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.BODY_SENSORS,
			Manifest.permission.CAMERA,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.SEND_SMS};

	private final int mRequestCode = 100;//权限请求码
	AlertDialog mPermissionDialog;
	private Context mContext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			List<String> mPermissionList = new ArrayList<>();
			mPermissionList.clear();
			for (String tmpPermission : permissions) {
				// 检查该权限是否已经获取
				if (ContextCompat.checkSelfPermission(this, tmpPermission) != PackageManager.PERMISSION_GRANTED) {
					mPermissionList.add(tmpPermission);//添加还未授予的权限
				}
			}
			if (mPermissionList.size() > 0) {
				ActivityCompat.requestPermissions(this,
						mPermissionList.toArray(new String[]{}), mRequestCode);
			} else {
//				onStartVideoMain();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean hasPermissionDismiss = false;//有权限没有通过
		if (requestCode == mRequestCode) {
			for (int i = 0; i < grantResults.length; i++) {
				if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
					Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
					hasPermissionDismiss = true;
					break;
				}
			}
			if(hasPermissionDismiss) {
				showPermissionDialog();
			} else {
//				onStartVideoMain();
			}
		}
	}

	private void showPermissionDialog() {
		if (mPermissionDialog == null) {
			mPermissionDialog = new AlertDialog.Builder(this)
					.setMessage("已禁用权限，请手动授予")
					.setPositiveButton("设置", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cancelPermissionDialog();
							Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
							Intent intent = new Intent(Settings.
									ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
							startActivity(intent);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//关闭页面或者做其他操作
							cancelPermissionDialog();
							MainActivity.this.finish();
						}
					})
					.create();
		}
		mPermissionDialog.show();
	}
	private void cancelPermissionDialog() {
		mPermissionDialog.cancel();
	}
}
