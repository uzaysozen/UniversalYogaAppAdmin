package com.example.universalyogaappadmin;

import static com.example.universalyogaappadmin.DatabaseHelper.CAPACITY_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.CLASS_TYPE_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.COLUMN_NAME_TIME;
import static com.example.universalyogaappadmin.DatabaseHelper.COURSE_ID_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DAY_OF_WEEK_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DESCRIPTION_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.PRICE_COLUMN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddingClassInstance extends AppCompatActivity {

    private ListView lv;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_adding_class_instance);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        String result = dbHelper.getCourseDetails();

        lv = (ListView) findViewById(R.id.listview);
        List<String> ls = new ArrayList<String>();
        JSONArray arr;
        try {
            arr = new JSONArray(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i =0; i < arr.length(); i++) {
            JSONObject jsonObject;
            int id;
            String dayOfWeek, time, capacity, price, classType, description;
            try {
                jsonObject = arr.getJSONObject(i);
                id = jsonObject.getInt("id");
                dayOfWeek = jsonObject.getString(DAY_OF_WEEK_COLUMN);
                time = jsonObject.getString(COLUMN_NAME_TIME);
                capacity = jsonObject.getString(CAPACITY_COLUMN);
                price = jsonObject.getString(PRICE_COLUMN);
                classType = jsonObject.getString(CLASS_TYPE_COLUMN);
                description = jsonObject.getString(DESCRIPTION_COLUMN);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            ls.add("Day of the Week: " + dayOfWeek + "\n" +
                    "Time: " + time + "\n" +
                    "Capacity: " + capacity + "\n" +
                    "Price: £" + price + "\n" +
                    "Class: " + classType + "\n" +
                    "Description: " + description);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                ls
        );
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        FloatingActionButton create = findViewById(R.id.floatingActionButton);
        create.setOnClickListener(v -> {
            Intent createCoursePage = new Intent(AddingClassInstance.this, MainActivity.class);
            startActivity(createCoursePage);
        });
    }
}