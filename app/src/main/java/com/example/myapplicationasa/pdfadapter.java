package com.example.myapplicationasa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class pdfadapter extends RecyclerView.Adapter<pdfadapter.PDFViewHolder> {

    private List<File> pdfList;
    private Context context;
    private int selectedPosition = -1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(File file);
    }

    public pdfadapter(Context context, List<File> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pdf_item, parent, false);
        return new PDFViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PDFViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File pdfFile = pdfList.get(position);
        holder.fileName.setText(pdfFile.getName());

        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onItemClick(pdfFile);

            }
            // Toggle selection
            if (selectedPosition == position) {
                selectedPosition = -1; // Deselect if the same item is clicked again
                holder.itemView.setBackgroundColor(Color.TRANSPARENT); // Reset background color
            } else {
                selectedPosition = position; // Select the clicked item
                notifyDataSetChanged(); // Refresh the list to highlight selection
            }

            // Notify the listener that a file has been selected
            if (listener != null) {
                listener.onItemClick(pdfFile);
            }
        });

        if (selectedPosition == position){
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public void updateList(List<File> newPdfFiles) {
        this.pdfList = new ArrayList<>(newPdfFiles);
        selectedPosition = -1; // Reset selection
        notifyDataSetChanged();
    }

    public File getSelectedPdfFile() {
        if (selectedPosition >= 0 && selectedPosition < pdfList.size()) {
            return pdfList.get(selectedPosition);
        } else {
            return null;
        }
    }



  class PDFViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        public PDFViewHolder(View view) {
            super(view);
            fileName = view.findViewById(R.id.fileName);
        }
    }


}
