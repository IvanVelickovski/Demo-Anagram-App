package com.ivanvelickovski.webfactorydemoapp.Threading;

import java.util.ArrayList;

public class AnalyzeResultUpdateTask implements Runnable {
    private ArrayList<String> anagrams;

    public AnalyzeResultUpdateTask(ArrayList<String> anagrams){
        this.anagrams = anagrams;
    }

    public void setAnagrams(ArrayList<String> anagrams){
        this.anagrams = anagrams;
    }

    @Override
    public void run() {
        // TODO
    }
}
