package com.example.universalyogaappadmin;

import java.util.Arrays;
import static com.example.universalyogaappadmin.DatabaseHelper.CAPACITY_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.CLASS_TYPE_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.COLUMN_NAME_TIME;
import static com.example.universalyogaappadmin.DatabaseHelper.DAY_OF_WEEK_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.DESCRIPTION_COLUMN;
import static com.example.universalyogaappadmin.DatabaseHelper.PRICE_COLUMN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kotlinx.coroutines.flow.Flow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.DayOfWeek;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    private int courseId;

    private void displayNextAlert(String strWeek, String strTime, String strCapacity,
                                  String strPrice, String strRadio, String strDesc){
        createAlert("Details Entered", "Details Entered:\n" + strWeek + "\n " + strTime + "\n " + strCapacity + "\n " +
                strPrice + "\n " + strRadio + "\n " + strDesc);
    }

    private String escapeJson (String json){
        return json.replace("\"", "\\\"");
    }

    private void createAlert(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent classInstancePage = new Intent(MainActivity.this, ClassInstanceList.class);
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

    private void getInputs(){
        RadioButton radioButtonInput = (RadioButton)findViewById(editClass_type.getCheckedRadioButtonId());

        String strWeek = spinnerDayOfWeek.getSelectedItem().toString(),
                strTime = Time.getSelectedItem().toString(),
               strCapacity = editTextCapacity.getText().toString(),
                strPrice = editPrice.getText().toString(),
                strRadio = radioButtonInput.getText().toString(),
                strDesc = editDescription.getText().toString();

        if (TextUtils.isEmpty(strWeek)
                || TextUtils.isEmpty(strTime)
                || TextUtils.isEmpty(strCapacity)
                || TextUtils.isEmpty(strPrice)
                || TextUtils.isEmpty(strRadio)) {
            createErrorAlert("Error Occurred", "Please fill all the required fields.");

        } else {
            if (courseId == -1)
                dbHelper.insertCourseDetails(null, strWeek, strTime, strCapacity, strPrice, strRadio, strDesc, false);
            else
                dbHelper.insertCourseDetails(courseId, strWeek, strTime, strCapacity, strPrice, strRadio, strDesc, true);

            //Toast.makeText(this, "Class has been created with id:" + courseId, Toast.LENGTH_SHORT).show();

            //Intent intent = new Intent(this, DetailsActivity.class);
            //startActivity(intent);
            displayNextAlert(strWeek, strTime,strCapacity, strPrice, strRadio, strDesc);
        }
    }

    private WebView browser;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        browser = (WebView) findViewById(R.id.webkit);
        String result = dbHelper.getCourseDetails();

        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        spinnerDayOfWeek = findViewById(R.id.DaySpinner);
        Time = findViewById(R.id.TimeSpinner);
        editTextCapacity = findViewById(R.id.ClassCapacityInputText);
        editPrice = findViewById(R.id.PriceInputText);
        editClass_type = findViewById(R.id.classTypeGroup);
        editDescription = findViewById(R.id.DescriptionText);
        Resources res = getResources();
        String[] daysOfWeek = res.getStringArray(R.array.daysOfTheWeek);
        String[] timeList = res.getStringArray(R.array.timeOfTheCourse);
        JSONObject courseJson;

        if (courseId > -1) {
            courseJson = dbHelper.getCourseById(courseId);
            try {
                spinnerDayOfWeek.setSelection(Arrays.asList(daysOfWeek).indexOf(courseJson.getString(DAY_OF_WEEK_COLUMN)));
                Time.setSelection(Arrays.asList(timeList).indexOf(courseJson.getString(COLUMN_NAME_TIME)));
                editTextCapacity.setText(courseJson.getString(CAPACITY_COLUMN));
                editPrice.setText(courseJson.getString(PRICE_COLUMN));
                editDescription.setText(courseJson.getString(DESCRIPTION_COLUMN));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


        FlowYoga = findViewById(R.id.FlowYogaRB);
        ArealYoga = findViewById(R.id.ArealYogaRB);
        FamilyYoga = findViewById(R.id.MeditationRB);
        Meditation = findViewById(R.id.FamilyYogaRB);

        Button create = (Button) findViewById(R.id.CreateSessionButton);
        Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appToolbar);
        String escapedJson = escapeJson(result);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });

        try {
            URL pageURL = new URL(getString(R.string.url));
            trustAllHosts();
            HttpURLConnection con = (HttpURLConnection) pageURL.openConnection();

            //String jsonString = getString(R.string.json);

            JsonThread myTask = new JsonThread(this, con, escapedJson);
            System.out.println("anan" + escapedJson);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();
            //Toast.makeText(this, "debug", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
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
            Intent landingPage = new Intent(MainActivity.this, ClassInstanceList.class);
            startActivity(landingPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class JsonThread implements Runnable
    {
        private AppCompatActivity activity;
        private HttpURLConnection con;
        private String jsonPayLoad;

        public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayload)
        {
            this.activity = activity;
            this.con = con;
            this.jsonPayLoad = jsonPayload;
        }

        @Override
        public void run()
        {
            String response = "";
            if (prepareConnection()) {
                response = postJson();
            } else {
                response = "Error preparing the connection";
            }
            showResult(response);
        }


        private void showResult(String response) {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    String page = generatePage(response);
                    ((MainActivity)activity).browser.loadData(page, "text/html", "UTF-8");
                }
            });
        }

        private String postJson() {
            String response = "";
            try {
                String postParameters = "jsonpayload=" + URLEncoder.encode(jsonPayLoad, "UTF-8");
                con.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(postParameters);
                out.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = readStream(con.getInputStream());
                    System.out.println("Selamin Aleykum" + response);
                } else {
                    response = "Error contacting server: " + responseCode;
                }
            } catch (Exception e) {
                response = e.toString();//"Error executing code";
                System.out.println("Selamin Aleykum" + response);
            }
            return response;
        }

        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String generatePage(String content) {
            return "<html><body><p>" + content + "</p></body></html>";
        }


        private boolean prepareConnection() {
            try {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                return true;

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}