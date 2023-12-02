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
import java.time.DayOfWeek;
import java.util.HashMap;

public class CreateClassInstance extends AppCompatActivity {
    LocalDate selectedDate;
    int courseId;
    String dayOfWeek;
    private DatabaseHelper dbHelper;

    private final HashMap<String, DayOfWeek> daysOfWeek = new HashMap<String, DayOfWeek> () {
        {
            put("Monday", DayOfWeek.MONDAY);
            put("Tuesday", DayOfWeek.TUESDAY);
            put("Wednesday", DayOfWeek.WEDNESDAY);
            put("Thursday", DayOfWeek.THURSDAY);
            put("Friday", DayOfWeek.FRIDAY);
            put("Saturday", DayOfWeek.SATURDAY);
            put("Sunday", DayOfWeek.SUNDAY);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class_instance);

        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        dbHelper = new DatabaseHelper(this);

        Button setDateButton = findViewById(R.id.dateButton);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        dayOfWeek = intent.getStringExtra("dayOfWeek");
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

    private void createAlert(String message) {
        new AlertDialog.Builder(this).setTitle("Details Entered").setMessage(message)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent classInstancePage = new Intent(CreateClassInstance.this, CourseList.class);
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
        createAlert("Details Entered:\n" + date + "\n" + tutor + "\n " + comments);
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
                // Check if the selected date is a Monday
                checkDateAndInsertClass(selectedDate, strTutor, strComments);
            } else {
                checkDateAndInsertClass(LocalDate.now(), strTutor, strComments);
            }
        }
    }

    private void checkDateAndInsertClass(LocalDate date, String strTutor, String strComments) {
        if (date.getDayOfWeek() != daysOfWeek.get(dayOfWeek)) {
            // Display alert if day of week is wrong
            createErrorAlert("Date Selection", "The selected day must be "+ dayOfWeek);
        } else if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
            createErrorAlert("Date Selection", "The selected date must be a future date");
        }
        else {
            displayNextAlert(LocalDate.now().toString(), strTutor, strComments);
            dbHelper.insertClassDetails(null, "evuk31", LocalDate.now().toString(), strTutor, strComments, courseId, false);
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
            Intent classListPage = new Intent(CreateClassInstance.this, CourseList.class);
            startActivity(classListPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}