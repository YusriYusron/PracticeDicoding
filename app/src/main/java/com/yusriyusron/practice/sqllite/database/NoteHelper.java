package com.yusriyusron.practice.sqllite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yusriyusron.practice.sqllite.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.yusriyusron.practice.sqllite.database.DatabaseContract.NoteColums.DATE;
import static com.yusriyusron.practice.sqllite.database.DatabaseContract.NoteColums.DESCRIPTION;
import static com.yusriyusron.practice.sqllite.database.DatabaseContract.NoteColums.TITLE;
import static com.yusriyusron.practice.sqllite.database.DatabaseContract.TABLE_NOTE;

public class NoteHelper {
    private static final String DATABASE_TABLE = TABLE_NOTE;
//    private Context context;
    private static NoteHelper INSTANCE;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static NoteHelper getInstange(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount()>0){
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Note note){
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, note.getTitle());
        initialValues.put(DESCRIPTION, note.getDescription());
        initialValues.put(DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(Note note){
        ContentValues args = new ContentValues();
        args.put(TITLE,note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE,note.getDate());
        return database.update(DATABASE_TABLE, args, _ID +"= '"+note.getId()+"'", null);
    }

    public int delete(int id){
        return database.delete(TABLE_NOTE,_ID+" = '"+id+"'",null);
    }
}
