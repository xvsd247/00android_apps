package com.zl.can;

import android.util.Log;

public class CanCallback {
    private static final String TAG = "CanCallback";
    void canDataNotify() {
        Log.d(TAG, "data coming!");
    }
}
