package com.ivanvelickovski.webfactorydemoapp.Threading;

import android.support.v4.app.Fragment;

import com.ivanvelickovski.webfactorydemoapp.Model.Anagram;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeResultUpdateTask implements Runnable {
    private HashMap<Integer,ArrayList<Anagram>> anagrams;
    private AnagramListener mListener;

    public AnalyzeResultUpdateTask(Fragment fragment) {
        this.mListener = (AnagramListener)fragment;
    }

    public void setAnagrams(HashMap<Integer,ArrayList<Anagram>> anagrams){
        this.anagrams = anagrams;
    }

    @Override
    public void run() {
        mListener.onAnagramsSet(anagrams);
    }

    public interface AnagramListener {
        void onAnagramsSet(HashMap<Integer,ArrayList<Anagram>> anagrams);
    }
}
