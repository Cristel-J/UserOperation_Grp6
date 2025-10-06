package com.example.useroperation_grp6;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnNew, btnPrint;
    ListView studListView;
    ArrayList<String> studentList;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getApplicationContext());
        btnNew = findViewById(R.id.btnNew);
        btnPrint = findViewById(R.id.btnPrint);
        studListView = findViewById(R.id.studlistview);

        loadStudListView();

        studListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = studentList.get(i);
            String[] parts = item.split(":");
            String sid = parts[0];
            Intent intent = new Intent(MainActivity.this, EditProfile.class);
            intent.putExtra("sid", sid);
            startActivity(intent);
        });

        studListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            String item = studentList.get(i);
            String[] parts = item.split(":");
            String sid = parts[0];

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("Do you really want to delete this record?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        dbHelper.deleteRecord(sid);
                        Toast.makeText(MainActivity.this, "Record Deleted!", Toast.LENGTH_LONG).show();
                        loadStudListView();
                    })
                    .setNegativeButton("NO", null)
                    .show();
            return true;
        });

        btnNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNewProfile.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudListView(); // Refresh list when returning
    }

    private void loadStudListView() {
        studentList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllRecords();

        if (cursor.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("No record found!")
                    .setPositiveButton("Close", null)
                    .show();
        } else {
            while (cursor.moveToNext()) {
                String record = cursor.getString(0) + ":" + cursor.getString(1) + " " + cursor.getString(2);
                studentList.add(record);
            }
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        studListView.setAdapter(adapter);
    }
}
