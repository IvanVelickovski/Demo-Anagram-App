package com.ivanvelickovski.webfactorydemoapp.Threading;

import com.ivanvelickovski.webfactorydemoapp.Model.Anagram;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeInfo;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeTask implements Runnable {
    private ArrayList<VolumeItem> books;
    private AnalyzeResultUpdateTask resultUpdateTask;

    public AnalyzeTask(ArrayList<VolumeItem> books, AnalyzeResultUpdateTask resultUpdateTask) {
        this.books = books;
        this.resultUpdateTask = resultUpdateTask;
    }

    @Override
    public void run() {
        HashMap<Integer,ArrayList<Anagram>> anagrams = analyzeTextAndFindAllAnagrams();
        resultUpdateTask.setAnagrams(anagrams);
    }

    private HashMap<Integer,ArrayList<Anagram>> analyzeTextAndFindAllAnagrams() {
        HashMap<Integer,ArrayList<Anagram>> anagrams = new HashMap<>();

        for (int i = 0; i < books.size(); i++) {
            final VolumeInfo book = books.get(i).getVolumeInfo();
            anagrams.put(i, anagramsForBook(book));
        }

        AnalyzeManager.getAnalyzeManager().getMainThreadExecutor().execute(resultUpdateTask);

        return anagrams;
    }

    private ArrayList<Anagram> anagramsForBook(VolumeInfo book) {
        String[] titleStrings = splitWordWithoutPunctuation(book.getTitle());
        String[] descriptionStrings = splitWordWithoutPunctuation(book.getDescription());

        return new ArrayList<>(findAnagramsForTitleDescPair(titleStrings, descriptionStrings, book));
    }

    private String[] splitWordWithoutPunctuation(String stringToSplit) {
        return stringToSplit.replaceAll("[,.();\"]", "")
                .toLowerCase()
                .split(" ");
    }

    private ArrayList<Anagram> findAnagramsForTitleDescPair(String[] titleStrings, String[] descriptionStrings, VolumeInfo book) {
        ArrayList<Anagram> anagrams = new ArrayList<>();

        for (int i = 0; i < titleStrings.length; i++) {
            for (int j = 0; j < descriptionStrings.length; j++) {
                String title = titleStrings[i];
                String description = descriptionStrings[j];

                if (title.length() != description.length()) {
                    // If words have different size, they are certainly not anagrams
                    continue;
                } else if (title.equals(description)) {
                    // If words are equal, then they are anagrams
                    anagrams.add(new Anagram(title, description, i, j, book));
                    continue;
                }

                HashMap<Character, Integer> titleMap = findDifferentLettersInAWord(title);
                HashMap<Character, Integer> descriptionMap = findDifferentLettersInAWord(description);

                if (titleMap.equals(descriptionMap)) {
                    // If word in title has same length as description
                    // And has same number of occurrences for a given letter as description
                    // Then these 2 words are anagrams for each other with equal hashmaps
                    anagrams.add(new Anagram(title, description, i, j, book));
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
                int numOccurrencesOfLetter = map.get(letter);
                map.put(letter, ++numOccurrencesOfLetter);
            }
        }

        return map;
    }
}
