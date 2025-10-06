package com.example.useroperation_grp6;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnNew, btnPrint;
    ListView studlistview;
    ArrayList<String> studentlist;
    ArrayAdapter<String> adapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -
                > {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                systemBars.bottom);
        return insets;
});
//dbHelper = new DBHelper(this);
        dbHelper = new DBHelper(getApplicationContext());
        btnNew = findViewById(R.id.btnNew);
        btnPrint = findViewById(R.id.btnPrint);
        studlistview = findViewById(R.id.studlistview);
        loadStudListView();
        studlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long
                    l) {
                String items = studentlist.get(i);
                String item[] = items.split(":");
                String sid = item[0];
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("sid",sid);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        studlistview.setOnItemLongClickListener(new
                                                        AdapterView.OnItemLongClickListener() {
                                                            @Override
                                                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i,
                                                                                           long l) {
                                                                String items = studentlist.get(i);

                                                                String item[] = items.split(":");
                                                                String sid = item[0];
                                                                AlertDialog.Builder info = new AlertDialog.Builder(MainActivity.this);
                                                                info.setTitle("Confirmation");
                                                                info.setMessage("Do you really want to delete this record?");
                                                                info.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                    @Override

                                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                                        dbHelper.deleteRecord(sid);
                                                                        Toast.makeText(MainActivity.this, "Record Deleted!",
                                                                                Toast.LENGTH_LONG).show();
                                                                        loadStudListView();
                                                                    }
                                                                });
                                                                info.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                    }
                                                                });
                                                                AlertDialog box = info.create();
                                                                box.show();
                                                                return false;
                                                            }
                                                        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, AddNewProfile.class);
                startActivity(intent);
            }
        });
    }
    public void loadStudListView(){
        studentlist = new ArrayList<>();
        studentlist.clear();
        Cursor cursor =dbHelper.getAllRecords();
        if(cursor.getCount() == 0){
            AlertDialog.Builder info = new AlertDialog.Builder(this);
            info.setMessage("No record found! ");
            info.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog box = info.create();
            box.show();
        }else{
            while (cursor.moveToNext()){
                String record = cursor.getString(0) + ":" + cursor.getString(1) + " " +
                        cursor.getString(2);
                studentlist.add(record);
            };
        }
        cursor.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                studentlist);

        studlistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}