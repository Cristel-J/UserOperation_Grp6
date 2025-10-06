package com.example.useroperation_grp6;

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

public class EditProfile extends AppCompatActivity {

    TextView txtID;
    EditText txtFn, txtLn;
    Spinner spCourse;
    RadioButton rb1, rb2, rb3, rb4;
    Button btnUpdate, btnCancel;
    DBHelper dbHelper;
    int yrlvl;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getApplicationContext());
        txtID = findViewById(R.id.txtSID);
        txtFn = findViewById(R.id.editFN);
        txtLn = findViewById(R.id.editLN);
        spCourse = findViewById(R.id.spinCourse);
        rb1 = findViewById(R.id.yrFirst);
        rb2 = findViewById(R.id.yrSecond);
        rb3 = findViewById(R.id.yrThird);
        rb4 = findViewById(R.id.yrFourth);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);

        Intent intent = getIntent();
        String sid = intent.getStringExtra("sid");
        txtID.setText(sid);

        Cursor cursor = dbHelper.getRecord(sid);
        if (cursor.moveToNext()) {
            txtFn.setText(cursor.getString(1));
            txtLn.setText(cursor.getString(2));
            course = cursor.getString(3);
            yrlvl = cursor.getInt(4);

            for (int i = 0; i < spCourse.getCount(); i++) {
                if (course.equals(spCourse.getItemAtPosition(i))) {
                    spCourse.setSelection(i);
                    break;
                }
            }

            if (yrlvl == 1) rb1.setChecked(true);
            else if (yrlvl == 2) rb2.setChecked(true);
            else if (yrlvl == 3) rb3.setChecked(true);
            else rb4.setChecked(true);
        }
        cursor.close();

        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                course = spCourse.getSelectedItem().toString();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btnUpdate.setOnClickListener(v -> {
            yrlvl = rb1.isChecked() ? 1 : rb2.isChecked() ? 2 : rb3.isChecked() ? 3 : 4;
            String fn = txtFn.getText().toString();
            String ln = txtLn.getText().toString();

            new AlertDialog.Builder(EditProfile.this)
                    .setTitle("Confirmation")
                    .setMessage("Do you really want to update this record?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        dbHelper.editRecord(sid, fn, ln, course, yrlvl);
                        Toast.makeText(EditProfile.this, "Record Updated!", Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .setNegativeButton("NO", null)
                    .show();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
