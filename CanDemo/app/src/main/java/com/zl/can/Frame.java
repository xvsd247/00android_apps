package com.zl.can;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Frame {
    private static final String TAG="CanFrame";
    private int can_id;
    private int dlc;
    private int remote;
    private int buf[];
    private Handler mHandler;
    public Frame(Handler handler){
        can_id = 1;
        dlc = 0;
        remote = 0;
        buf = new int[8];
        mHandler = handler;
    }
    public int getID()
    {
        return can_id;
    }
    public void setID(int id){
        this.can_id = id;
    }
    public int getRemote()
    {
        return remote;
    }
    public void setRemote(int remote){
        this.remote = remote;
    }
    public int getDlc() {
        return dlc;
    }
    public void setDlc(int dlc)
    {
        this.dlc = dlc;
    }
    public int[] getBuf()
    {
        return this.buf;
    }
    public void setBuf(int buf[])
    {
        for(int i=0;i<buf.length;i++)
            this.buf[i] = buf[i];
    }
    public int getIndexData(int index)
    {
        if(index >= 8)
            return -1;
        else
            return this.buf[index];
    }
    public void dataReadyNotifly (){
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
    }
}