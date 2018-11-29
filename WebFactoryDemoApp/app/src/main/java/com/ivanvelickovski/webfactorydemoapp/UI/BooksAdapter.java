package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

        holder.txtTitleHeader.setText(context.getResources().getString(R.string.book_title));
        holder.txtDescriptionHeader.setText(context.getResources().getString(R.string.book_description));

        String title = bookInfo.getTitle();
        String description = bookInfo.getDescription();

        if (anagrams.get(position) != null) {
            SpannableStringBuilder spanBuilderTitle = new SpannableStringBuilder(title);
            SpannableStringBuilder spanBuilderDesc = new SpannableStringBuilder(description);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_red_dark));

            String[] titleStrings = title.split(" ");
            String[] descStrings = description.split(" ");

            ArrayList<Anagram> anagramsOnPosition = anagrams.get(position);
            ArrayList<Integer> titlesPositionsWithAnagrams = new ArrayList<>();
            ArrayList<Integer> descPositionsWithAnagrams = new ArrayList<>();

            for (Anagram anagram: anagramsOnPosition) {
                titlesPositionsWithAnagrams.add(anagram.getTitlePosition());
                descPositionsWithAnagrams.add(anagram.getDescriptionPosition());
            }

            for (Integer titlePositions : titlesPositionsWithAnagrams) {
                int letterCountBeforeTitleWithAnagram = 0;

                for (int i = 0; i < titlePositions; i++) {
                    letterCountBeforeTitleWithAnagram += titleStrings[i].length();
                }

                int start = letterCountBeforeTitleWithAnagram + titlePositions;
                int end = start + titleStrings[titlePositions].length();
                spanBuilderTitle.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            for (Integer descPositions : descPositionsWithAnagrams) {
                int letterCountBeforeDescWithAnagram = 0;

                for (int i = 0; i < descPositions; i++) {
                    letterCountBeforeDescWithAnagram += descStrings[i].length();
                }

                int start = letterCountBeforeDescWithAnagram + descPositions;
                int end = start + descStrings[descPositions].length();
                spanBuilderDesc.setSpan(colorSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            holder.txtTitleDesc.setText(spanBuilderTitle);
            holder.txtDescriptionDesc.setText(spanBuilderDesc);
        } else {
            holder.txtTitleDesc.setText(title);
            holder.txtDescriptionDesc.setText(description);
        }

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
