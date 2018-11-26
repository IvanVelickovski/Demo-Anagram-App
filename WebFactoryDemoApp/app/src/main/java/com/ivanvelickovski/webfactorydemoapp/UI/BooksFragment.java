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
                anagrams = processBooks();

                if (anagrams != null && anagrams.size() > 0) {
                    Toast.makeText(getContext(), "Found " + anagrams.size() / 2 + " anagrams", Toast.LENGTH_SHORT).show();
                }
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

    private String[] splitWordWithoutPunctuation(String stringToSplit) {
        return stringToSplit.replaceAll("[,.();\"]", "")
                .toLowerCase()
                .split(" ");
    }

    private ArrayList<String> findAnagramsForTitleDescPair(String[] titleStrings, String[] descriptionStrings) {
        ArrayList<String> anagrams = new ArrayList<>();

        for (String title : titleStrings) {
            for (String description : descriptionStrings) {
                if (title.length() != description.length()) {
                    // If words have different size, they are certainly not anagrams
                    continue;
                } else if (title.equals(description)) {
                    // If words are equal, then they are anagrams
                    anagrams.add(title);
                    anagrams.add(description);
                    continue;
                }

                HashMap<Character, Integer> titleMap = findDifferentLettersInAWord(title);
                HashMap<Character, Integer> descriptionMap = findDifferentLettersInAWord(description);

                if (titleMap.equals(descriptionMap)) {
                    // If word in title has same length as description
                    // And has same number of occurrences for a given letter as description
                    // Then these 2 words are anagrams for each other with equal hashmaps
                    anagrams.add(title);
                    anagrams.add(description);
                }
            }
        }

        return anagrams;
    }

    private HashMap<Character, Integer> findDifferentLettersInAWord(String word) {
        HashMap<Character, Integer> map = new HashMap<>();

        for (Character letter : word.toCharArray()) {
            if (!map.containsKey(letter)) {
                map.put(letter, 1);
            } else {
                int numOccurencesOfLetter = map.get(letter);
                map.put(letter, ++numOccurencesOfLetter);
            }
        }

        return map;
    }

    private ArrayList<String> processBooks() {
        ArrayList<String> anagramsForBook = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            final VolumeInfo book = books.get(i).getVolumeInfo();

            String[] titleStrings = splitWordWithoutPunctuation(book.getTitle());
            String[] descriptionStrings = splitWordWithoutPunctuation(book.getDescription());

            anagramsForBook.addAll(findAnagramsForTitleDescPair(titleStrings, descriptionStrings));
        }

        return anagramsForBook;
    }

    public interface BooksListener {
    }
}
