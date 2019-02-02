package com.yusriyusron.practice.shared_preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yusriyusron.practice.R;

public class FormUserPreverenceActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputName,inputEmail,inputPhone,inputAge;
    private RadioGroup rgLoveMu;
    private RadioButton rbYes,rbNo;
    private Button btnSave;

    public static String EXTRA_TYPE_FORM = "extra_type_form";
    public static int REQUEST_CODE = 100;

    public static int TYPE_ADD = 1;
    public static int TYPE_EDIT = 2;
    public int formType;

    public final String FIELD_REQUIRED = "Field tidak boleh kosong";
    public final String FIELD_DIGIT_ONLY = "Hanya boleh angka numerik";
    public final String FIELD_ISNOT_VALID = "Email tidak valid";

    private UserPreference userPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forn_user_preference);

        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone_number);
        inputAge = findViewById(R.id.input_age);
        rgLoveMu = findViewById(R.id.rb_love_mu);
        rbYes = findViewById(R.id.rb_yes);
        rbNo = findViewById(R.id.rb_no);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        formType = getIntent().getIntExtra(EXTRA_TYPE_FORM,0);

        userPreference = new UserPreference(this);

        String actionBarTitle = null;
        String btnTitle = null;

        if (formType == 1){
            actionBarTitle = "Tambar Baru";
            btnTitle = "Simpan";
        }else {
            actionBarTitle = "Ubah Data";
            btnTitle = "Ubah";
            showPreferenceInForm();
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave.setText(btnTitle);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save){
            String name = inputName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();
            String age = inputAge.getText().toString().trim();
            boolean isLoveMu = rgLoveMu.getCheckedRadioButtonId() == R.id.rb_yes;

            boolean isEmpty = false;

            if (TextUtils.isEmpty(name)){
                isEmpty = true;
                inputAge.setError(FIELD_REQUIRED);
            }

            if (TextUtils.isEmpty(email)){
                isEmpty = true;
                inputEmail.setError(FIELD_REQUIRED);
            }else if (!isValidEmail(email)){
                isEmpty = true;
                inputEmail.setError(FIELD_ISNOT_VALID);
            }

            if (TextUtils.isEmpty(phone)){
                isEmpty = true;
                inputPhone.setError(FIELD_REQUIRED);
            }

            if (TextUtils.isEmpty(age)){
                isEmpty = true;
                inputAge.setError(FIELD_REQUIRED);
            }else if (!TextUtils.isDigitsOnly(phone)){
                isEmpty = true;
                inputAge.setError(FIELD_DIGIT_ONLY);
            }

            if (!isEmpty){
                userPreference.setName(name);
                userPreference.setEmail(email);
                userPreference.setPhoneNumber(phone);
                userPreference.setAge(Integer.parseInt(age));
                userPreference.setLoveMU(isLoveMu);

                Toast.makeText(this,"Data tersimpan",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isValidEmail(CharSequence email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPreferenceInForm(){
        inputName.setText(userPreference.getName());
        inputEmail.setText(userPreference.getEmail());
        inputPhone.setText(userPreference.getPhoneNumber());
        inputAge.setText(String.valueOf(userPreference.getAge()));

        if (userPreference.getLoveMU()){
            rbYes.setChecked(true);
        }else {
            rbNo.setChecked(false);
        }
    }
}
