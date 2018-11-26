package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ivanvelickovski.webfactorydemoapp.Model.VolumeInfo;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;
import com.ivanvelickovski.webfactorydemoapp.R;
import com.ivanvelickovski.webfactorydemoapp.Threading.AnalyzeManager;
import com.ivanvelickovski.webfactorydemoapp.Threading.AnalyzeResultUpdateTask;
import com.ivanvelickovski.webfactorydemoapp.Threading.AnalyzeTask;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksFragment extends Fragment {
    private BooksListener mListener;
    private ArrayList<String> anagrams = new ArrayList<>();
    private ArrayList<VolumeItem> books;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.books = getArguments().getParcelableArrayList("books");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_books, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.rvBooks);
        BooksAdapter adapter = new BooksAdapter(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button btnProcessBooks = v.findViewById(R.id.btnProcessBooks);
        btnProcessBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyzeResultUpdateTask resultUpdateTask = new AnalyzeResultUpdateTask(anagrams);

                AnalyzeTask  analyzeTask = new AnalyzeTask(books, resultUpdateTask);
                AnalyzeManager.getAnalyzeManager().runAnalyze(analyzeTask);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BooksListener) {
            mListener = (BooksListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BooksListener {
    }
}
