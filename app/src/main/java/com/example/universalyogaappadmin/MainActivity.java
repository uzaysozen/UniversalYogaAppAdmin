package com.example.universalyogaappadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import java.time.DayOfWeek;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerDayOfWeek;
    private Spinner Time;
    private EditText editTextCapacity;
    private EditText editDuration;
    private EditText editPrice;
    private RadioGroup editClass_type;
    private EditText editDescription;
    private RadioButton FlowYoga;
    private RadioButton ArealYoga;
    private RadioButton FamilyYoga;
    private RadioButton Meditation;
    private Button buttonAdd, buttonDelete;

    private void displayNextAlert(String strWeek, String strTime, String strCapacity,
                                  String strPrice, String strRadio, String strDesc){
        createAlert("Details Entered", "Detals Entered:\n" + strWeek + "\n " + strTime + "\n " + strCapacity + "\n " +
                strPrice + "\n " + strRadio + "\n " + strDesc);
    }

    private void createAlert(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    private void getInputs(){
        Spinner weekInput = (Spinner)findViewById(R.id.DaySpinner);
        Spinner timeInput = (Spinner)findViewById(R.id.TimeSpinner);
        EditText capacityInput = (EditText)findViewById(R.id.ClassCapacityInputText);
        EditText priceInput = (EditText)findViewById(R.id.PriceInputText);
        EditText descriptionInput = (EditText)findViewById(R.id.DescriptionText);
        RadioGroup group = (RadioGroup)findViewById(R.id.classTypeGroup);
        RadioButton radioButtonInput = (RadioButton)findViewById(group.getCheckedRadioButtonId());

        String strWeek = weekInput.getSelectedItem().toString(),
                strTime = timeInput.getSelectedItem().toString(),
               strCapacity = capacityInput.getText().toString(),
                strPrice = priceInput.getText().toString(),
                strRadio = radioButtonInput.getText().toString(),
                strDesc = descriptionInput.getText().toString();

        if (TextUtils.isEmpty(strWeek)
                || TextUtils.isEmpty(strTime)
                || TextUtils.isEmpty(strCapacity)
                || TextUtils.isEmpty(strPrice)
                || TextUtils.isEmpty(strRadio)) {
            createErrorAlert("Error Occurred", "Please fill all the required fields.");
        } else {
            displayNextAlert(strWeek, strTime,strCapacity, strPrice, strRadio, strDesc);
        }
    }
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        spinnerDayOfWeek = findViewById(R.id.DaySpinner);
        Time = findViewById(R.id.TimeSpinner);
        editTextCapacity = findViewById(R.id.ClassCapacityInputText);
        //editDuration = findViewById(R.id.classcapacity);
        editPrice = findViewById(R.id.PriceInputText);
        editClass_type = findViewById(R.id.classTypeGroup);
        editDescription = findViewById(R.id.DescriptionText);

        FlowYoga = findViewById(R.id.FlowYogaRB);
        ArealYoga = findViewById(R.id.ArealYogaRB);
        FamilyYoga = findViewById(R.id.MeditationRB);
        Meditation = findViewById(R.id.FamilyYogaRB);

        Button create = (Button)findViewById(R.id.CreateSessionButton);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);

        Button create = (Button)findViewById(R.id.button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void saveDetails() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        EditText dayTxt = findViewById(R.id.Dayoftheweek);
        EditText capacityTxt = findViewById(R.id.classcapacity);
        //EditText timeTxt = findViewById(R.id.);
        EditText priceTxt = findViewById(R.id.PriceInputText);
        EditText class_typeTxt = findViewById(R.id.classTypeGroup);
        EditText descriptionTxt = findViewById(R.id.DescriptionText);

        String day = dayTxt.getText().toString();
        String capacity = capacityTxt.getText().toString();
        String price = priceTxt.getText().toString();
        String class_type = class_typeTxt.getText().toString();
        String description = descriptionTxt.getText().toString();

        long courseId = dbHelper.insertDetails(day, capacity, price, class_type, description);

        Toast.makeText(this, "Class has been created with id:" + courseId, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemExit) {
            Intent landingPage = new Intent(MainActivity.this, LandingPage.class);
            startActivity(landingPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}