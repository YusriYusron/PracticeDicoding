package com.yusriyusron.practice.sqllite;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.sqllite.adapter.NoteAdapter;
import com.yusriyusron.practice.sqllite.database.NoteHelper;
import com.yusriyusron.practice.sqllite.interfaces.LoadNotesCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainFormActivity extends AppCompatActivity implements View.OnClickListener,LoadNotesCallback{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private NoteAdapter noteAdapter;
    private NoteHelper noteHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Notes");
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        noteHelper = NoteHelper.getInstange(getApplicationContext());
        noteHelper.open();

        progressBar = findViewById(R.id.progress_bar);
        floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(this);

        noteAdapter = new NoteAdapter(this);
        recyclerView.setAdapter(noteAdapter);

        if (savedInstanceState == null){
            new LoadNoteAsync(noteHelper,this).execute();
        }else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null){
                noteAdapter.setListNotes(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,noteAdapter.getListNotes());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add){
            Intent intent = new Intent(this,FormAddUpdateActivity.class);
            startActivityForResult(intent,FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        progressBar.setVisibility(View.INVISIBLE);
        noteAdapter.setListNotes(notes);
    }

    private class LoadNoteAsync extends AsyncTask<Void , Void, ArrayList<Note>>{
        private final WeakReference<NoteHelper> weakNoteHelper;
        private final WeakReference<LoadNotesCallback> weakCallback;

        public LoadNoteAsync(NoteHelper noteHelper, LoadNotesCallback callback) {
            weakNoteHelper = new WeakReference<>(noteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return weakNoteHelper.get().getAllNotes();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
                if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                    Note note = data.getParcelableExtra(FormAddUpdateActivity.EXTRA_NOTE);
                    noteAdapter.addItem(note);
                    recyclerView.smoothScrollToPosition(noteAdapter.getItemCount() - 1);
                    showSnackBarMessage("Satu item berhasil ditambahkan");
                }
            }else if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
                if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                    Note note = data.getParcelableExtra(FormAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION,0);
                    noteAdapter.updateItem(position,note);
                    recyclerView.smoothScrollToPosition(position);
                    showSnackBarMessage("Satu item berhasil diubah");
                }else if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
                    int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION,0);
                    noteAdapter.removeItem(position);
                    showSnackBarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    private void showSnackBarMessage(String message){
        Snackbar.make(recyclerView,message, Snackbar.LENGTH_SHORT).show();
    }
}
