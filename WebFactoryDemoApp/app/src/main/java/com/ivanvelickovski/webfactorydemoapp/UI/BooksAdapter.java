package com.ivanvelickovski.webfactorydemoapp.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanvelickovski.webfactorydemoapp.Model.VolumeInfo;
import com.ivanvelickovski.webfactorydemoapp.Model.VolumeItem;
import com.ivanvelickovski.webfactorydemoapp.R;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BaseHolder>  {
    private ArrayList<VolumeItem> books;
    private Context context;

    public BooksAdapter(ArrayList<VolumeItem> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
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

        holder.txtTitleDesc.setText(bookInfo.getTitle());
        holder.txtDescriptionDesc.setText(bookInfo.getDescription());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
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
}
