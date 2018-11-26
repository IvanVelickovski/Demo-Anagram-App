package com.ivanvelickovski.webfactorydemoapp.Threading;

import android.widget.TextView;

public class AnalyzeResultUpdateTask implements Runnable {
    private TextView message;
    private String backgroundMsg;

    public AnalyzeResultUpdateTask(TextView msg){
        message = msg;
    }
    public void setBackgroundMsg(String bmsg){
        backgroundMsg = bmsg;
    }

    @Override
    public void run() {
        message.setText(backgroundMsg);
    }
}
