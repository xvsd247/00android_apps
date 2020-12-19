package com.zl.can;
import android.util.Log;
public class CanUtils {
    private static final String TAG = "CanUtils";
    // Used to load the 'native-lib' library on application startup.
    private static boolean isLoaded;
    static {
        if (!isLoaded) {
            System.loadLibrary("canutils");
            isLoaded = true;
        }
    }

    public native boolean init_native();
    /**
     * the sonctructor of this class should be call within the thread that has a looper
     * (UI thread or a thread that called Looper.prepare)
     */
    public CanUtils() {
        Log.d(TAG, "-xjx-- test");
        init_native();
    }

}
