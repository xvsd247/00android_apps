/*
 * Copyright  2007 The Android Open Source Project
 * Copyright  2013 Freescale Semiconductor, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.freescale.test_dualplayback;

import java.io.File;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
 {
 	private String TAG = "--xjx--";
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

	private AlertDialog dialog;

	private Button button;
	private String[] FileNameInDir = null;
	private List<String>  VideoFileList = null;
	private File fileDir = null;
	private Spinner s0;
	private Spinner s1;
	private TextView textview0;
	private TextView textview1;
	private String videoFile0 = "";
	private String videoFile1 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
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
				onStartVideoMain();
			}
		}
	}

	public void onStartVideoMain() {
		textview0 = (TextView)findViewById(R.id.textview0);
		textview1 = (TextView)findViewById(R.id.textview1);
		textview0.setText("video displayed on LCD");
		textview1.setText("video displayed on HDMI");


		int fileIndex = 0;
		File externalPath = Environment.getExternalStorageDirectory();

		if(externalPath.length() != 0) {
			fileDir = new File(externalPath.toString() + "/Movies/");
		} else {
			fileDir = new File("/storage/emulated/0/Movies/");
			Log.e(TAG, "No externel dir!");
			finish();
		}
		Log.d(TAG, "fileDir: " + fileDir.toString());
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".mp4")||name.endsWith(".m4v")||name.endsWith(".avi");
			}
		};
		FileNameInDir = fileDir.list(filter);
		VideoFileList = new ArrayList<String>();
		Log.e(TAG, "FileNameInDir.length: " + FileNameInDir.length);
		for(fileIndex = 0;fileIndex < FileNameInDir.length;fileIndex++){
			VideoFileList.add(fileDir.toString() + "/" + FileNameInDir[fileIndex]);
		}
		ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VideoFileList);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, VideoFileList);
		s0 = (Spinner)findViewById(R.id.spinner0);
		adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s0.setAdapter(adapter0);
		s1 = (Spinner) findViewById(R.id.spinner1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(adapter1);
		button = (Button)findViewById(R.id.firstActivityButton);
		button.setText("Play the video");
		button.setOnClickListener(MainActivity.this);
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
				 onStartVideoMain();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,SecondActivity.class);
	    int	selectIndex;
	    selectIndex = s0.getSelectedItemPosition();
	    videoFile0 = fileDir.toString() + "/" + FileNameInDir[selectIndex];
		selectIndex = s1.getSelectedItemPosition();
		videoFile1 = fileDir.toString() + "/"  + FileNameInDir[selectIndex];
		intent.putExtra("videofile0",videoFile0);
		intent.putExtra("videofile1", videoFile1);
		startActivity(intent);

	}

}
