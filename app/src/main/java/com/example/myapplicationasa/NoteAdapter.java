package com.example.myapplicationasa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.noteViewHolder> {

    private List<File> notesList = new ArrayList<>();
    private Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(File file);
    }

    public NoteAdapter(Context context, List<File> notesList) {
        this.context = context;
        this.notesList = new ArrayList<>(notesList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.notes_layout, parent, false);
        return new noteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        File file = notesList.get(position);
        holder.fileName.setText(file.getName());

        // Highlight selection
        holder.itemView.setBackgroundColor(
                (position == selectedPosition) ? Color.LTGRAY : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;
            int oldSelected = selectedPosition;
            if (adapterPos == selectedPosition) {
                selectedPosition = RecyclerView.NO_POSITION;
            } else {
                selectedPosition = adapterPos;
            }

            // Refresh only affected items
            if (oldSelected != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldSelected);
            }
            notifyItemChanged(adapterPos);
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Replace the current list (e.g. after a new scan) and clear selection.
     */
    public void updateList(List<File> newPdfFiles) {
        this.notesList = new ArrayList<>(newPdfFiles);
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * Returns the currently selected file, or null if none.
     */
    public File getSelectedFile() {
        if (selectedPosition >= 0 && selectedPosition < notesList.size()) {
            return notesList.get(selectedPosition);
        }
        return null;
    }

    class noteViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        noteViewHolder(View view) {
            super(view);
            fileName = view.findViewById(R.id.fileName);
        }
    }
}

