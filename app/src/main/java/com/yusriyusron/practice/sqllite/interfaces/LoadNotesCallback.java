package com.yusriyusron.practice.sqllite.interfaces;

import com.yusriyusron.practice.sqllite.Note;

import java.util.ArrayList;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note> notes);
}
