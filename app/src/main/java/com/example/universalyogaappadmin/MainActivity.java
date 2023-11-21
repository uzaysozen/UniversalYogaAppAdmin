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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.telecom.Call;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.DayOfWeek;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
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
            long courseId = dbHelper.insertDetails(strWeek, strTime, strCapacity, strPrice, strRadio, strDesc);

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

        spinnerDayOfWeek = findViewById(R.id.DaySpinner);
        Time = findViewById(R.id.TimeSpinner);
        editTextCapacity = findViewById(R.id.ClassCapacityInputText);
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

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });

        try{
            URL pageURL = new URL(getString(R.string.url));
            trustAllHosts();
            HttpURLConnection con = (HttpURLConnection)pageURL.openConnection();
            GetAndDisplayThread myThread = new GetAndDisplayThread(this, con);

            String jsonString = getString(R.string.json);

            JsonThread myTask = new JsonThread(this, con, jsonString);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains  TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{

                };
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }catch (Exception e){
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
            Intent landingPage = new Intent(MainActivity.this, LandingPage.class);
            startActivity(landingPage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class GetAndDisplayThread implements Runnable{
        private final HttpURLConnection con;
        private final AppCompatActivity activity;

        public GetAndDisplayThread(AppCompatActivity activity, HttpURLConnection con){
            this.con = con;
            this.activity = activity;
        }

        @Override
        public void run() {
            String response = "";
            try{
                response = readStream(con.getInputStream());
            }
            catch(IOException e){
                {e.printStackTrace();
            }

                String requiredData = "";
                try {
                    requiredData = extractRequiredData(response);
                }
                catch (Exception exception) {
                    e.printStackTrace();
                }

                showResult(requiredData);
            }
        }

        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private void showResult(String response) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String page = generatePage(response);
                    Log.i("xxxx", page);
                    ((MainActivity)activity).browser.loadData(page,"text/html","UTF-8");
                }
            });
        }

        private String generatePage(String content) {
            return "<html><body><h1>" + content + "</h1></body></html>";
        }

        private String extractRequiredData(String responseBody) throws Exception{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document embeddedDoc = builder.parse(new InputSource(new StringReader(responseBody)));

            NodeList titleNodes = embeddedDoc.getElementsByTagName("title");
            if (titleNodes != null){
                Element aTitleElement = (Element)titleNodes.item(0);
                aTitleElement.normalize();
                Node titleContent = aTitleElement.getFirstChild();
                return titleContent.getNodeValue();
            }

            return "";
        }

        class JsonThread implements Runnable{
            private AppCompatActivity activity;
            private HttpURLConnection con;
            private String jsonPayLoad;

            public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayLoad){
                this.activity = activity;
                this.con = con;
                this.jsonPayLoad = jsonPayLoad;
            }

            @Override
            public void run() {
                String response = "";
                if (prepareConnection()){
                    response = postJson();
                }
                else{
                    response = "Error preparing the connection";
                }
                showResult(response);
            }

            private boolean prepareConnection(){
                try{
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    return true;
                }catch(ProtocolException e){
                 e.printStackTrace();
                }
                return false;
            }
        }
    }
}