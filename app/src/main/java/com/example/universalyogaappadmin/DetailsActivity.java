package com.example.universalyogaappadmin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_main));

        DatabaseHelper db = new DatabaseHelper(this);

        String details = db.getCourseDetails();

        TextView detailsTxt = findViewById(R.id.DescriptionText);

        detailsTxt.setText(details);
    }
}
