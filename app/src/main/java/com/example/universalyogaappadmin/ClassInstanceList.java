package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

public class ClassInstanceList extends AppCompatActivity {

    private ListView lv;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.class_instance_list);
        //Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        //setSupportActionBar(appToolbar);
        JSONArray result = dbHelper.getCourseDetails();

        lv = (ListView) findViewById(R.id.instanceListView);

        ClassAdapter classAdapter = new ClassAdapter(getApplicationContext(), result);
        lv.setAdapter(classAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent classDatePage = new Intent(ClassInstanceList.this, ClassInstanceDateActivity.class);
                startActivity(classDatePage);
            }
        });

        FloatingActionButton create = findViewById(R.id.floatingActionButton);
        create.setOnClickListener(v -> {
            Intent createCoursePage = new Intent(ClassInstanceList.this, MainActivity.class);
            startActivity(createCoursePage);
        });
    }
}