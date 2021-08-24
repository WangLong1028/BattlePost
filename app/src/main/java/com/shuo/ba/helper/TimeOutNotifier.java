package com.shuo.ba.helper;

import com.shuo.ba.thread.TimeOutThread;

public class TimeOutNotifier {

    private long outTime;
    private boolean isSuccess = false;
    private TimeOut timeOut;

    public TimeOutNotifier(long outTime, TimeOut timeOut) {
        this.outTime = outTime;
        this.timeOut = timeOut;
    }

    public long getOutTime() {
        return outTime;
    }

    public void timeOut(String tag){
        if (isSuccess) return;
        timeOut.timeOut(tag);
    }

    public void startNewTask(String tag){
        isSuccess = false;
        Thread detector = new TimeOutThread(this, tag);
        detector.start();
    }

    public void setSuccess() {
        isSuccess = true;
    }

    public interface TimeOut{
        void timeOut(String tag);
    }
}
