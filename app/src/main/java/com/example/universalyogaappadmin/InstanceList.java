package com.example.universalyogaappadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;

public class InstanceList extends AppCompatActivity {

    private ListView lv;
    private DatabaseHelper dbHelper;

    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_list);

        dbHelper = new DatabaseHelper(this);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(appToolbar);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        JSONArray result = dbHelper.getClassDetails(courseId);

        lv = (ListView) findViewById(R.id.instanceListView);

        InstanceAdapter instanceAdapter = new InstanceAdapter(getApplicationContext(), result);
        lv.setAdapter(instanceAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemExit) {
            Intent classListPage = new Intent(InstanceList.this, ClassInstanceList.class);
            startActivity(classListPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}