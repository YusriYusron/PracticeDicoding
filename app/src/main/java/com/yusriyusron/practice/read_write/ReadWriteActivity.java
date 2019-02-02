package com.yusriyusron.practice.read_write;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yusriyusron.practice.R;
import com.yusriyusron.practice.read_write.helper.FileHelper;

import java.io.File;
import java.util.ArrayList;

public class ReadWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNew,btnOpen,btnSave;
    private EditText inputContent,inputTitle;

    private File path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write_main);

        btnNew = findViewById(R.id.btn_new);
        btnOpen = findViewById(R.id.btn_open);
        btnSave = findViewById(R.id.btn_save);
        inputContent = findViewById(R.id.input_content);
        inputTitle = findViewById(R.id.input_title);

        btnNew.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        path = getFilesDir();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new){
            newFile();
        }else if (view.getId() == R.id.btn_open){
            openFile();
        }else if (view.getId() == R.id.btn_save){
            saveFile();
        }
    }

    private void newFile(){
        inputTitle.setText("");
        inputContent.setText("");

        Toast.makeText(this,"Clearing File", Toast.LENGTH_SHORT).show();
    }

    private void loadData(String title){
        String text = FileHelper.readFromFile(this,title);
        inputTitle.setText(title);
        inputContent.setText(text);
        Toast.makeText(this,"Loading "+title+" data ", Toast.LENGTH_SHORT).show();
    }

    private void openFile(){
        showList();
    }

    private void showList(){
        final ArrayList<String> arrayList = new ArrayList<>();
        for (String file: path.list()){
            arrayList.add(file);
        }

        final CharSequence[] items = arrayList.toArray(new CharSequence[arrayList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih file yang diinginkan");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadData(items[i].toString());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveFile(){
        if (inputTitle.getText().toString().isEmpty()){
            Toast.makeText(this,"Title harus diisi terlebih dahulu",Toast.LENGTH_SHORT).show();
        }else {
            String title = inputTitle.getText().toString();
            String text = inputContent.getText().toString();
            FileHelper.writeToFile(title,text,this);
            Toast.makeText(this,"Saving"+inputTitle.getText().toString()+" file",Toast.LENGTH_SHORT).show();
        }
    }
}
