package com.yusriyusron.practice.sqllite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.sqllite.database.NoteHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputTitle,inputDescription;
    private Button btnSubmit;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private Note note;
    private int position;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);

        inputTitle = findViewById(R.id.et_judul);
        inputDescription = findViewById(R.id.et_description);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        }else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit){
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            if (note != null){
                inputTitle.setText(note.getTitle());
                inputDescription.setText(note.getDescription());
            }
        }else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit){
            String title = inputTitle.getText().toString().trim();
            String desc = inputDescription.getText().toString().trim();

            // Jika Fieldnya masih kosong maka tampilkan error
            if (TextUtils.isEmpty(title)){
                inputTitle.setError("Field can not be blank");
                return;
            }

            note.setTitle(title);
            note.setDescription(desc);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE,note);
            intent.putExtra(EXTRA_POSITION,position);

            // Jika EDIT (SET RESULT-nya UPDATE) dan juka bukan EDIT maka SET RESULT-nya ADD
            if (isEdit){
                long result = noteHelper.update(note);
                if (result>0){
                    setResult(RESULT_UPDATE,intent);
                    finish();
                }else {
                    Toast.makeText(this,"Gagal Mengupdate Data",Toast.LENGTH_SHORT).show();
                }
            }else {
                note.setDate(getCurrentDate());
                long result = noteHelper.insert(note);
                if (result>0){
                    note.setId((int) result);
                    setResult(RESULT_ADD,intent);
                    finish();
                }else {
                    Toast.makeText(this,"Gagal Menambahkan Data",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    // Konfirmasi dialog sebelum proses batal atau hapus dieksekusi
    // Close = 10
    // Delete = 20
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private void showAlertDialog(int type){
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose){
            dialogTitle = "Batal";
            dialogMessage = "Apakah Anda ingin membatalkan perubahan form?";
        }else {
            dialogMessage = "Apakah Anda yakin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isDialogClose){
                            finish();
                        }else {
                            long result = noteHelper.delete(note.getId());
                            if (result>0){
                                Intent intent = new Intent();
                                intent.putExtra(EXTRA_POSITION,position);
                                setResult(RESULT_DELETE,intent);
                                finish();
                            }else {
                                Toast.makeText(FormAddUpdateActivity.this,"Gagal Menambahkan Data",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return  dateFormat.format(date);
    }
}
