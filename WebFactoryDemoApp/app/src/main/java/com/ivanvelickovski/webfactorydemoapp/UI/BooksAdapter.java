package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanvelickovski.webfactorydemoapp.Model.Anagram;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeInfo;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;
import com.ivanvelickovski.webfactorydemoapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BaseHolder> {
    private ArrayList<VolumeItem> books;
    private Context context;
    private BooksAdapterListener mListener;
    private HashMap<Integer, ArrayList<Anagram>> anagrams;

    public BooksAdapter(ArrayList<VolumeItem> books, HashMap<Integer, ArrayList<Anagram>> anagrams) {
        this.books = books;
        this.anagrams = anagrams;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
        mListener = (BooksAdapterListener)context;
    }

    @NonNull
    @Override
    public BooksAdapter.BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_adapter, parent, false);
        return new BaseHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        VolumeInfo bookInfo = books.get(position).getVolumeInfo();

        String title = bookInfo.getTitle();
        String description = bookInfo.getDescription();

        if (anagrams.get(position) != null) {
            setViewHolderWithAnagram(holder, position, title, description);
        } else {
            holder.txtTitleDesc.setText(title);
            holder.txtDescriptionDesc.setText(description);
        }

        setItemViewFromPosition(holder, position);
    }

    private void setViewHolderWithAnagram(@NonNull BaseHolder holder, int position, String title, String description) {
        SpannableStringBuilder spanBuilderTitle = new SpannableStringBuilder(title);
        SpannableStringBuilder spanBuilderDesc = new SpannableStringBuilder(description);

        ArrayList<Anagram> anagramsAtPosition = anagrams.get(position);

        if (anagramsAtPosition == null) {
            // TODO: show error message
            return;
        }

        for (Anagram singleAnagram : anagramsAtPosition) {
            String titleAnagram = singleAnagram.getTitle();
            String descAnagram = singleAnagram.getDescription();

            int titleAnagramStartPosition = findStartIndexOfAnagramInText(singleAnagram.getTitleAnagramPosition(), title.split(" "));
            int descAnagramStartPosition = findStartIndexOfAnagramInText(singleAnagram.getDescriptionAnagramPosition(), description.split(" "));

            spanBuilderTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_red_dark)),
                    titleAnagramStartPosition, titleAnagramStartPosition + titleAnagram.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            spanBuilderDesc.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_red_dark)),
                    descAnagramStartPosition, descAnagramStartPosition + descAnagram.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        holder.txtTitleDesc.setText(spanBuilderTitle);
        holder.txtDescriptionDesc.setText(spanBuilderDesc);
    }

    private void setItemViewFromPosition(@NonNull BaseHolder holder, int position) {
        holder.txtTitleHeader.setText(context.getResources().getString(R.string.book_title));
        holder.txtDescriptionHeader.setText(context.getResources().getString(R.string.book_description));

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.list_color_1));
            holder.txtTitleDesc.setTextColor(context.getResources().getColor(R.color.text_color_list_bg_1));
            holder.txtDescriptionDesc.setTextColor(context.getResources().getColor(R.color.text_color_list_bg_1));
            mListener.setFragmentBackgroundColor(context.getResources().getColor(R.color.list_color_1));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.list_color_2));
            holder.txtTitleDesc.setTextColor(context.getResources().getColor(R.color.text_color_list_bg_2));
            holder.txtDescriptionDesc.setTextColor(context.getResources().getColor(R.color.text_color_list_bg_2));
            mListener.setFragmentBackgroundColor(context.getResources().getColor(R.color.list_color_2));
        }
    }

    private int findStartIndexOfAnagramInText(int anagramIndexInStringList, String[] listOfStrings) {
        int lengthOfWordsBeforeTitleAnagram = 0;

        for (int i = 0; i < anagramIndexInStringList; i++) {
            lengthOfWordsBeforeTitleAnagram += listOfStrings[i].length();
        }

        return anagramIndexInStringList + lengthOfWordsBeforeTitleAnagram;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateAnagrams(HashMap<Integer, ArrayList<Anagram>> anagrams) {
        this.anagrams = anagrams;
        notifyDataSetChanged();
    }

    public class BaseHolder extends RecyclerView.ViewHolder {
        private TextView txtTitleHeader;
        private TextView txtTitleDesc;
        private TextView txtDescriptionHeader;
        private TextView txtDescriptionDesc;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);

            txtTitleHeader = itemView.findViewById(R.id.txtBookTitleHeader);
            txtTitleDesc = itemView.findViewById(R.id.txtBookTitleDesc);
            txtDescriptionHeader = itemView.findViewById(R.id.txtBookDescriptionHeader);
            txtDescriptionDesc = itemView.findViewById(R.id.txtBookDescriptionDesc);
        }
    }

    public interface BooksAdapterListener {
        void setFragmentBackgroundColor(int color);
    }
}