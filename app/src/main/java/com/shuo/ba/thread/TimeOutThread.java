package com.shuo.ba.thread;

import android.os.Looper;

import com.shuo.ba.helper.TimeOutNotifier;

public class TimeOutThread extends Thread {
    private TimeOutNotifier timeOutNotifier;
    private String tag;

    public TimeOutThread(TimeOutNotifier timeOutNotifier, String tag) {
        this.timeOutNotifier = timeOutNotifier;
        this.tag = tag;
    }

    @Override
    public void run() {
        Looper.prepare();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeOutNotifier.getOutTime());
        timeOutNotifier.timeOut(tag);
    }
}
