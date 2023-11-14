package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
        new AlertDialog.Builder(this).setTitle("Details Entered").setMessage(
                "Detals Entered:\n" + strWeek + "\n " + strTime + "\n " + strCapacity + "\n " +
                        strPrice + "\n " + strRadio + "\n " + strDesc
        ).setNegativeButton("Confrim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
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
        displayNextAlert(strWeek, strTime,strCapacity, strPrice, strRadio, strDesc);
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
}