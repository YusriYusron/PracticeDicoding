package com.yusriyusron.practice.pre_load;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.pre_load.helper.MahasiswaHelper;
import com.yusriyusron.practice.pre_load.model.MahasiswaModel;
import com.yusriyusron.practice.pre_load.preferences.AppPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainPreLoadActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load_main);

        progressBar = findViewById(R.id.progress_bar_pre_load);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void,Integer,Void>{
        final String TAG = LoadData.class.getSimpleName();
        MahasiswaHelper mahasiswaHelper;
        AppPreference appPreference;
        double progress;
        double maxProgress = 100;

        @Override
        protected void onPreExecute() {
            mahasiswaHelper = new MahasiswaHelper(MainPreLoadActivity.this);
            appPreference = new AppPreference(MainPreLoadActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = appPreference.getFirstRun();

            if (firstRun){
                ArrayList<MahasiswaModel> mahasiswaModels = preLoadRaw();

                mahasiswaHelper.open();

                progress = 30;
                publishProgress((int) progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

                mahasiswaHelper.beginTrasaction();
                try{
                    for (MahasiswaModel model: mahasiswaModels){
                        mahasiswaHelper.insertTrasaction(model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                    // Jika semua proses telah di set success maka akan di commit ke database
                    mahasiswaHelper.setTransactionSuccess();
                }catch (Exception e){
                    // Jika Gagal
                    Log.e(TAG,"doInBackground: Exception");
                }
                mahasiswaHelper.endTrasaction();

                mahasiswaHelper.close();

                appPreference.setFirstRun(false);

                publishProgress((int) progress);
            }else {
                try {
                    synchronized (this){
                        this.wait(2000);

                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxProgress);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(MainPreLoadActivity.this,MahasiswaActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public ArrayList<MahasiswaModel> preLoadRaw(){
        ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try{
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.data_mahasiswa);

            reader = new BufferedReader(new InputStreamReader(inputStream));
            int count = 0;

            do {
                line = reader.readLine();
                String[] splitStr = line.split("\t");

                MahasiswaModel mahasiswaModel;

                mahasiswaModel = new MahasiswaModel(splitStr[0],splitStr[1]);
                mahasiswaModels.add(mahasiswaModel);
                count++;
            }while (line!=null);
        } catch (IOException e) {
            Toast.makeText(this,"Hallo Error",Toast.LENGTH_SHORT).show();
        }
        return mahasiswaModels;
    }
}
