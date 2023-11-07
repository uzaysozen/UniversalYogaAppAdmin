package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button createClass = (Button) findViewById(R.id.button2);
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createClassIntent = new Intent(LandingPage.this, MainActivity.class);
                startActivity(createClassIntent);
            }
        });

        Button addToSchedule = (Button) findViewById(R.id.button3);
        addToSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}