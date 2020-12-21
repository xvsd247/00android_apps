package com.zl.candemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.zl.can.CanUtils;
import android.util.Log;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CanUtils mCanUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        mCanUtils = new CanUtils();
        int data[] = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77};
        mCanUtils.flexcan_native_send(0x55, 8, 0, 0, 0, 1, data);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();
}
