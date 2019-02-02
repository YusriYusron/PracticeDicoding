package com.yusriyusron.practice.pre_load;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.pre_load.adapter.MahasiswaAdapter;
import com.yusriyusron.practice.pre_load.helper.MahasiswaHelper;
import com.yusriyusron.practice.pre_load.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MahasiswaAdapter mahasiswaAdapter;
    private MahasiswaHelper mahasiswaHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);

        recyclerView = findViewById(R.id.recycler_view_pre_load);

        mahasiswaAdapter = new MahasiswaAdapter(this);
        mahasiswaHelper = new MahasiswaHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(mahasiswaAdapter);

        mahasiswaHelper.open();

        // Get All Mahasiswa in Database
        ArrayList<MahasiswaModel> mahasiswaModels = mahasiswaHelper.getAllData();
        mahasiswaHelper.close();

        mahasiswaAdapter.addItem(mahasiswaModels);
    }
}
