package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Admin_Database dbHelper;
    private Spinner spinnerDayOfWeek;
    private EditText editTextCapacity;
    private RadioGroup radioGroupClassType1;
    private RadioGroup radioGroupClassType2;
    private RadioGroup radioGroupClassType3;
    private RadioGroup radioGroupClassType4;
    private Button buttonAdd, buttonDelete;
    SQLiteDatabase db = dbHelper.getWritableDatabase();

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
        Spinner weekInput = (Spinner)findViewById(R.id.spinner2);
        Spinner timeInput = (Spinner)findViewById(R.id.spinner3);
        EditText capacityInput = (EditText)findViewById(R.id.editTextNumber);
        EditText priceInput = (EditText)findViewById(R.id.editTextNumber2);
        EditText descriptionInput = (EditText)findViewById(R.id.editTextTextMultiLine);
        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
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
        dbHelper = new Admin_Database(this);

        spinnerDayOfWeek = findViewById(R.id.Dayoftheweek);
        editTextCapacity = findViewById(R.id.classcapacity);
        radioGroupClassType1 = findViewById(R.id.radioButton);
        radioGroupClassType2 = findViewById(R.id.radioButton2);
        radioGroupClassType3 = findViewById(R.id.radioButton3);
        radioGroupClassType4 = findViewById(R.id.radioButton4);

        Button create = (Button)findViewById(R.id.button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });
    }
}