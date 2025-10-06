package com.example.useroperation_grp6;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfile extends AppCompatActivity {
    //general declaration
    TextView txtID;
    EditText txtFn, txtLn;
    Spinner spCourse;
    RadioButton rb1, rb2, rb3, rb4;
    Button btn1, btn2;
    DBHelper dbHelper;
    int yrlvl;

    String course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -
                > {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                systemBars.bottom);
        return insets;
});
        dbHelper = new DBHelper(getApplicationContext());
        txtID = findViewById(R.id.txtSID);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String sid = bundle.getString("sid");
//assigned the sid transfer from the MainActivity
//into the txtID of the EditProfile
        txtID.setText(sid);
//call the getRecord() of the DBHelper class thru dbhelper object

//and put the retrieve record to the cursor object
        Cursor cursor = dbHelper.getRecord(sid);
        if(cursor.moveToNext()){
//get the data from the FN column and assign to the txtFN
            txtFn.setText(cursor.getString(1));
//get the data from the LN column and assign to the txtFN
            txtLn.setText(cursor.getString(2));
//get the data from the COURSE column and assign to the course variable
            course = cursor.getString(3);
//look for the equivalent item in the spinner using index address
//and compare it to the retrieve course value then set it as the
//selected item
            for(int i = 0; i < spCourse.getCount(); ++i){
                if(course.equals(spCourse.getItemAtPosition(i))){
                    spCourse.setSelection(i);
                    break;
                }
            }
//get the data from the YRLVL column and assign to the yrlvl variable
            yrlvl = cursor.getInt(4);
//compare the value of retrieve YRLVL value and compare yrlvl
// to set checked to the radio button equivalent to it
            if (yrlvl == 1){
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
            }
            else if (yrlvl == 2){
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                rb4.setChecked(false);
            }
            else if (yrlvl == 3){
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                rb4.setChecked(false);
            }
            else if (yrlvl == 4){
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(true);
            }
        }
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
                AlertDialog.Builder info = new AlertDialog.Builder(EditProfile.this);
                info.setTitle("Confirmation");
                info.setMessage("Do you really want to update this record?");
                info.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                        dbHelper.editRecord(sid, fn, ln, course, yrlvl);
                        Toast.makeText(EditProfile.this, "Record Updated!",
                                Toast.LENGTH_LONG).show();
                        MainActivity main = new MainActivity();
                        main.loadStudListView();

                        finish();

                    }
                });
                info.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog box = info.create();
                box.show();
            }
        });
    }
}