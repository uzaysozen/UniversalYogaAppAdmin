package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;

public class InstanceList extends AppCompatActivity {

    private ListView lv;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_list);

        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.class_instance_list);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        JSONArray result = dbHelper.getClassDetails();

        lv = (ListView) findViewById(R.id.instanceListView);

        InstanceAdapter instanceAdapter = new InstanceAdapter(getApplicationContext(), result);
        lv.setAdapter(instanceAdapter);
    }
}