package com.example.universalyogaappadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private void displayNextAlert(String strWeek, String strTime, String strCapacity,
                                  String strPrice, String strRadio, String strDesc){
        new AlertDialog.Builder(this).setTitle("Details Entered").setMessage(
                "Detals Entered:\n" + strWeek + "\n " + strTime + "\n " + strCapacity + "\n " +
                        strPrice + "\n " + strRadio + "\n " + strDesc
        ).setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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