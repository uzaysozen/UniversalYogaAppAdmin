package com.example.universalyogaappadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.time.LocalDate;

public class ClassInstanceDateActivity extends AppCompatActivity {
    LocalDate selectedDate;
    int courseId;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_instance_date);

        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        dbHelper = new DatabaseHelper(this);

        Button setDateButton = findViewById(R.id.dateButton);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDate = LocalDate.of(year, ++month, day);
            }
        });
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });
    }

    private void createAlert(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent classInstancePage = new Intent(ClassInstanceDateActivity.this, ClassInstanceList.class);
                        startActivity(classInstancePage);
                    }
                }).setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
    }

    private void createErrorAlert(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void displayNextAlert(String date, String tutor, String comments){
        createAlert("Details Entered", "Details Entered:\n" + date + "\n" + tutor + "\n " + comments);
    }

    private void getInputs(){
        EditText tutorInput = (EditText)findViewById(R.id.teacherText);
        EditText commentsInput = (EditText)findViewById(R.id.commentsText);

        String strTutor = tutorInput.getText().toString(),
                strComments = commentsInput.getText().toString();

        if (TextUtils.isEmpty(strTutor)) {
            createErrorAlert("Error Occurred", "Please fill all the required fields.");
        } else {
            if (selectedDate != null) {
                displayNextAlert(selectedDate.toString(), strTutor, strComments);
                dbHelper.insertClassDetails(null, "evuk31",selectedDate.toString(), strTutor, strComments, courseId, false);
            } else {
                displayNextAlert(LocalDate.now().toString(), strTutor, strComments);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemExit) {
            Intent classListPage = new Intent(ClassInstanceDateActivity.this, ClassInstanceList.class);
            startActivity(classListPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}