package com.yusriyusron.practice;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yusriyusron.practice.pre_load.MainPreLoadActivity;
import com.yusriyusron.practice.read_write.ReadWriteActivity;
import com.yusriyusron.practice.shared_preferences.FormUserPreverenceActivity;
import com.yusriyusron.practice.shared_preferences.UserPreference;
import com.yusriyusron.practice.sqllite.MainFormActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvAge, tvEmail, tvLoveMu, tvPhone;
    private Button btnSave, btnLatihanReadWrite, btnLatihanAddUpdate,btnPreLoad;
    private UserPreference userPreference;

    private boolean isPreferenceEmpty = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        tvAge = findViewById(R.id.tv_age);
        tvEmail = findViewById(R.id.tv_email);
        tvLoveMu = findViewById(R.id.tv_love_mu);
        tvPhone = findViewById(R.id.tv_phone);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        userPreference = new UserPreference(this);
        getSupportActionBar().setTitle("My User Preference");
        showExistingPreference();

        btnLatihanReadWrite = findViewById(R.id.btn_read_write);
        btnLatihanReadWrite.setOnClickListener(this);

        btnLatihanAddUpdate = findViewById(R.id.btn_add_update);
        btnLatihanAddUpdate.setOnClickListener(this);

        btnPreLoad = findViewById(R.id.btn_pre_load);
        btnPreLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save){
            Intent intent = new Intent(MainActivity.this,FormUserPreverenceActivity.class);
            if (isPreferenceEmpty){
                intent.putExtra(FormUserPreverenceActivity.EXTRA_TYPE_FORM,FormUserPreverenceActivity.TYPE_ADD);
            }else {
                intent.putExtra(FormUserPreverenceActivity.EXTRA_TYPE_FORM,FormUserPreverenceActivity.TYPE_EDIT);
            }
            startActivityForResult(intent,FormUserPreverenceActivity.REQUEST_CODE);
        }else if (view.getId() == R.id.btn_read_write){
            Intent intent = new Intent(MainActivity.this, ReadWriteActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.btn_add_update){
            Intent intent = new Intent(MainActivity.this, MainFormActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.btn_pre_load){
            Intent intent = new Intent(MainActivity.this,MainPreLoadActivity.class);
            startActivity(intent);
        }
    }
    private void showExistingPreference(){
        if (!TextUtils.isEmpty(userPreference.getName())){
            tvName.setText(userPreference.getName());
            tvAge.setText(String.valueOf(userPreference.getAge()));
            tvLoveMu.setText(userPreference.getLoveMU() ? "Ya" : "Tidak");
            tvEmail.setText(userPreference.getEmail());
            tvPhone.setText(userPreference.getPhoneNumber());

            btnSave.setText("Ubah");
        }else {
            final String TEXT_EMPTY = "Tidak Ada";
            tvName.setText(TEXT_EMPTY);
            tvEmail.setText(TEXT_EMPTY);
            tvLoveMu.setText(TEXT_EMPTY);
            tvAge.setText(TEXT_EMPTY);
            tvPhone.setText(TEXT_EMPTY);

            btnSave.setText("Simpan");
            isPreferenceEmpty = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FormUserPreverenceActivity.REQUEST_CODE){
            showExistingPreference();
        }
    }
}
