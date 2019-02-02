package com.yusriyusron.practice.pre_load.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yusriyusron.practice.pre_load.database.DatabaseHelperPreLoad;
import com.yusriyusron.practice.pre_load.model.MahasiswaModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.yusriyusron.practice.pre_load.database.DatabaseContract.MahasiswaColumns.NIM;
import static com.yusriyusron.practice.pre_load.database.DatabaseContract.TABLE_NAME;
import static com.yusriyusron.practice.pre_load.database.DatabaseContract.MahasiswaColumns.NAMA;

public class MahasiswaHelper {
    private Context context;
    private DatabaseHelperPreLoad databaseHelper;

    private SQLiteDatabase sqLiteDatabase;

    public MahasiswaHelper(Context context) {
        this.context = context;
    }

    public MahasiswaHelper open() throws SQLException{
        databaseHelper = new DatabaseHelperPreLoad(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<MahasiswaModel> getDataByName(String name){
        String result = "";
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,null,NAMA+" LIKE ?",new String[]{name},null,null,_ID+" ASC",null);
        cursor.moveToFirst();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0){
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(NIM)));

                arrayList.add(mahasiswaModel);
                cursor.moveToNext();

            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<MahasiswaModel> getAllData(){
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,null,null,null,null,null,_ID+" ASC",null);
        cursor.moveToFirst();
        ArrayList<MahasiswaModel> arrayList = new ArrayList<>();
        MahasiswaModel mahasiswaModel;
        if (cursor.getCount() > 0){
            do {
                mahasiswaModel = new MahasiswaModel();
                mahasiswaModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                mahasiswaModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAMA)));
                mahasiswaModel.setNim(cursor.getString(cursor.getColumnIndexOrThrow(NIM)));

                arrayList.add(mahasiswaModel);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(MahasiswaModel mahasiswaModel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAMA,mahasiswaModel.getName());
        initialValues.put(NIM,mahasiswaModel.getNim());
        return sqLiteDatabase.insert(TABLE_NAME,null,initialValues);
    }

    public void beginTrasaction(){
        sqLiteDatabase.beginTransaction();
    }

    public void setTransactionSuccess(){
        sqLiteDatabase.setTransactionSuccessful();
    }

    public void endTrasaction(){
        sqLiteDatabase.endTransaction();
    }

    public void insertTrasaction(MahasiswaModel mahasiswaModel){
        String sql = "INSERT INTO "+TABLE_NAME+" ("+NAMA+", "+NIM+") VALUES (?, ?)";
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        statement.bindString(1,mahasiswaModel.getName());
        statement.bindString(2,mahasiswaModel.getNim());
        statement.execute();
        statement.clearBindings();
    }

    public int update(MahasiswaModel mahasiswaModel){
        ContentValues args = new ContentValues();
        args.put(NAMA,mahasiswaModel.getName());
        args.put(NIM,mahasiswaModel.getNim());
        return sqLiteDatabase.update(TABLE_NAME,args,_ID+"= '"+mahasiswaModel.getId()+"'",null);
    }

    public int delete(int id){
        return sqLiteDatabase.delete(TABLE_NAME,_ID+" = '"+id+"'",null);
    }
}
