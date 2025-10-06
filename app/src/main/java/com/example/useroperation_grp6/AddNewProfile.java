package com.example.useroperation_grp6;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
    Button btn1, btn2;
    int yrlvl;
    String course;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -
                > {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                systemBars.bottom);
        return insets;
});
        dbHelper = new DBHelper(this);
        txtID = findViewById(R.id.txtSID);
        txtID.setText(dbHelper.getSID());
        txtFn = findViewById(R.id.editFN);
        txtLn = findViewById(R.id.editLN);
        btn1 = findViewById(R.id.btnUpdate);
        btn2 = findViewById(R.id.btnCancel);
        spCourse = findViewById(R.id.spinCourse);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.courses,

                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i,
                                       long l) {
                course = spCourse.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        rb1 = findViewById(R.id.yrFirst);
        rb1.setChecked(true);
        rb2 = findViewById(R.id.yrSecond);
        rb3 = findViewById(R.id.yrThird);
        rb4 = findViewById(R.id.yrFourth);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb1.isChecked()){
                    yrlvl = 1;
                }
                else if(rb2.isChecked()){
                    yrlvl = 2;
                }
                else if(rb3.isChecked()){
                    yrlvl = 3;
                }
                if(rb4.isChecked()){
                    yrlvl = 4;
                }
                String fn = txtFn.getText().toString();
                String ln = txtLn.getText().toString();
                dbHelper.addNewRecord(fn, ln, course, yrlvl);
                AlertDialog.Builder info = new AlertDialog.Builder(AddNewProfile.this);
                info.setMessage("New record added!");
                info.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                        MainActivity main = new MainActivity();
                        main.loadStudListView();
                        finish();
                    }
                });
                AlertDialog box = info.create();
                box.show();
            }
        });
    }
}