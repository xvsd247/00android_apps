package com.zl.can;
import android.util.Log;

import static java.lang.Thread.interrupted;

public class CanUtils {
    private static final String TAG = "CanUtils";
    // Used to load the 'native-lib' library on application startup.
    private static boolean isLoaded;
    public Frame frame;
    public Thread mThread;
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
    public CanUtils(Frame mFrame) {
        init_native();
        frame = mFrame;
        //mThread = new Thread(new CanDumpThread());
    }

    public void newThread() {
        if(mThread != null) {
            //to be done
        }
        mThread = new CanDumpThread();
        running = true;
    }
    public void stopThread(Thread mThread) {
        running = false;
        flexcan_native_stopread();
        mThread.interrupt();
    }
    public class CanDumpThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    running = flexcan_native_readloop(0, 0, frame);
                    Log.e(TAG, "readloop return: " + running);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "interrupt occurred, stop thread");
                    return;
                }
            }
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native boolean init_native();
    public native boolean flexcan_native_config(int bitrate, int loopback, int restart_ms);
    public native boolean flexcan_native_send(int id, int dlc, int extended, int rtr, int infinite, int loopcount, int data[]);
    private native boolean flexcan_native_readloop(int id, int mask, Frame frame);
    public native boolean flexcan_native_stopread();
}
