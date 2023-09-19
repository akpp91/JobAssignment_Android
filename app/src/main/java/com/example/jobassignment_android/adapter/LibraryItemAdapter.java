package com.example.jobassignment_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobassignment_android.R;
import com.example.jobassignment_android.entity.LibraryItem;
import java.util.List;

public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.ViewHolder> {

    private List<LibraryItem> libraryItemList;
    private Context context;

    public LibraryItemAdapter(Context context, List<LibraryItem> libraryItemList) {
        this.context = context;
        this.libraryItemList = libraryItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LibraryItem libraryItem = libraryItemList.get(position);
        holder.idTextView.setText(String.valueOf(libraryItem.getId()));
        holder.nameTextView.setText(libraryItem.getName());
        holder.registerNameEngTextView.setText(libraryItem.getRegisterNameEng());
        holder.countTextView.setText(String.valueOf(libraryItem.getCount()));
    }

    @Override
    public int getItemCount() {
        return libraryItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView registerNameEngTextView;
        TextView countTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.textViewId);
            nameTextView = itemView.findViewById(R.id.textViewName);
            registerNameEngTextView = itemView.findViewById(R.id.textViewRegisterNameENG);
            countTextView = itemView.findViewById(R.id.textViewCount);
        }
    }
}
