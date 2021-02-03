package com.zl.can;
import android.os.Handler;
import android.util.Log;

public class CanUtils {
    private static final String TAG = "CanUtils";
    private static boolean isLoaded;
    private Frame frame;
    private Thread mThread;
    private  boolean running = true;

    static {
        if (!isLoaded) {
            System.loadLibrary("canutils");
            isLoaded = true;
        }
    }

    /**
     * the sonctructor of this class should be call within the thread that has a looper
     * (UI thread or a thread that called Looper.prepare)
     */
    public CanUtils(Handler mHandler) {
        frame = new Frame(mHandler);
    }

    public Frame getFrame(){
        return this.frame;
    }

    public boolean startThread() {
        int status = flexcan_native_getstate();
        if(status < 0 || status > 3 ) {
            Log.e(TAG, "CAN bus off or error!");
            return false;
        }
        if(mThread != null) {
            //to be done
            stopThread();
        }
        mThread = new CanDumpThread();
        running = true;
        mThread.start();
        return true;
    }
    public void stopThread() {
        running = false;
        flexcan_native_stopread();
        if(mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }
    private class CanDumpThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    running = flexcan_native_readloop(0, 0, frame);
                    //Log.e(TAG, "read loop err occur");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //Log.e(TAG, "interrupt occurred, stop thread");
                    return;
                }
            }
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int flexcan_native_getstate();
    public native boolean flexcan_native_config(int bitrate, int loopback, int restart_ms);
    public native boolean flexcan_native_send(int id, int dlc, int extended, int rtr, int loopcount, int data[]);
    private native boolean flexcan_native_readloop(int id, int mask, Frame frame);
    private native boolean flexcan_native_stopread();
}
