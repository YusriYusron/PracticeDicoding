package com.yusriyusron.practice.sqllite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.sqllite.CustomOnItemClickListener;
import com.yusriyusron.practice.sqllite.FormAddUpdateActivity;
import com.yusriyusron.practice.sqllite.Note;

import java.util.ArrayList;
import java.util.LinkedList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> listNotes = new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {
        if (listNotes.size() > 0){
            this.listNotes.clear();
        }else {
            this.listNotes.addAll(listNotes);
            notifyDataSetChanged();
        }
    }

    public void addItem(Note note){
        this.listNotes.add(note);
        notifyItemInserted(listNotes.size() - 1);
    }

    public void updateItem(int position, Note note){
        this.listNotes.set(position,note);
        notifyItemChanged(position,note);
    }

    public void removeItem(int position){
        this.listNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listNotes.size());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note,viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        noteViewHolder.textTitle.setText(getListNotes().get(i).getTitle());
        noteViewHolder.textDescription.setText(getListNotes().get(i).getDescription());
        noteViewHolder.textDate.setText(getListNotes().get(i).getDate());
        noteViewHolder.cardView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION,position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, getListNotes().get(position));
                activity.startActivityForResult(intent,FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return getListNotes().size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle, textDescription, textDate;
        private CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.tv_item_title);
            textDescription = itemView.findViewById(R.id.tv_item_description);
            textDate = itemView.findViewById(R.id.tv_item_date);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
