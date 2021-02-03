package com.zl.candemo;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import com.zl.can.CanUtils;
import com.zl.can.Frame;

import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    private static final String TAG = "MainActivity";
    private CanUtils mCanUtils;
    private TextView mText = null;
    private Button button = null;
    private Button button1 = null;
    private Switch mSwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        mText = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch.setOnClickListener(this);
        mCanUtils = new CanUtils(mHandler);
    }
    public static String bytesToHexString(int[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            stringBuilder.append("0x");
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e(TAG, "read loop stopped");
                    break;
                case 1:
                    Log.d(TAG,"read can msg id: 0x" + Integer.toHexString(mCanUtils.getFrame().getID())
                            + " remote: " + mCanUtils.getFrame().getRemote()
                            + " data: " + bytesToHexString(mCanUtils.getFrame().getBuf()));
                    mText.append("\n");
                    mText.append("id: 0x" + Integer.toHexString(mCanUtils.getFrame().getID()) +
                            " data: " +bytesToHexString(mCanUtils.getFrame().getBuf()));
                    int offset = mText.getLineCount() * mText.getLineHeight();
                    if (offset > mText.getHeight()) mText.scrollTo(0, offset - mText.getHeight());
                    break;
                case -1:
                    Log.e(TAG, "error occur when read");
                    mCanUtils.stopThread();
                    break;
                default:
                    Log.d(TAG,"message not important!");
            }
        }
    };

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.equals(button)) {
            try {
                int data[] = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77};
                mCanUtils.flexcan_native_send(0x55, 8, 0, 0, 1, data);
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(TAG, "Remote exception while flexcan send!!!");
            }
        } else if (v.equals(button1)) {
            Log.d(TAG, "Config can interface and tear up it!!!");
            mCanUtils.flexcan_native_config(125000, 1, 20);
        } else if (v.equals(mSwitch)) {
            if(mSwitch.isChecked()) {
                Log.d(TAG, "start read");

                if(!mCanUtils.startThread()) {
                    mSwitch.setChecked(false);
                }
            } else {
                Log.d(TAG, "stop read");
                mCanUtils.stopThread();
            }
        }
    }
}
