package com.example.useroperation_grp6;


import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNewProfile extends AppCompatActivity {

    TextView txtID;
    EditText txtFn, txtLn;
    Spinner spCourse;
    RadioButton rb1, rb2, rb3, rb4;
    Button btnSave, btnCancel;
    DBHelper dbHelper;
    String course;
    int yrlvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        txtID = findViewById(R.id.txtSID);
        txtFn = findViewById(R.id.editFN);
        txtLn = findViewById(R.id.editLN);
        spCourse = findViewById(R.id.spinCourse);
        rb1 = findViewById(R.id.yrFirst);
        rb2 = findViewById(R.id.yrSecond);
        rb3 = findViewById(R.id.yrThird);
        rb4 = findViewById(R.id.yrFourth);
        btnSave = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        txtID.setText(dbHelper.getSID());
        rb1.setChecked(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course = spCourse.getSelectedItem().toString();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btnSave.setOnClickListener(v -> {
            yrlvl = rb1.isChecked() ? 1 : rb2.isChecked() ? 2 : rb3.isChecked() ? 3 : 4;
            String fn = txtFn.getText().toString();
            String ln = txtLn.getText().toString();
            dbHelper.addNewRecord(fn, ln, course, yrlvl);

            new AlertDialog.Builder(AddNewProfile.this)
                    .setMessage("New record added!")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
