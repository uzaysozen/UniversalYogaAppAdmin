package com.example.universalyogaappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ClassInstanceList extends AppCompatActivity {

    private ListView lv;
    private DatabaseHelper dbHelper;
    private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        String result = dbHelper.getWebFormat().toString();
        setContentView(R.layout.class_instance_list);
        //Toolbar appToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        //setSupportActionBar(appToolbar);
        JSONArray result_uzay = dbHelper.getCourseDetails();
        browser = (WebView) findViewById(R.id.webkit);
        browser.setBackgroundColor(Color.TRANSPARENT);

        lv = (ListView) findViewById(R.id.instanceListView);

        ClassAdapter classAdapter = new ClassAdapter(getApplicationContext(), result_uzay);
        lv.setAdapter(classAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent classDatePage = new Intent(ClassInstanceList.this, ClassInstanceDateActivity.class);
                startActivity(classDatePage);
            }
        });

        FloatingActionButton create = findViewById(R.id.floatingActionButton);
        create.setOnClickListener(v -> {
            Intent createCoursePage = new Intent(ClassInstanceList.this, MainActivity.class);
            startActivity(createCoursePage);
        });

        FloatingActionButton upload = findViewById(R.id.uploadFloatingBtn);
        upload.setOnClickListener(v -> {
            try {
                URL pageURL = new URL(getString(R.string.url));
                trustAllHosts();
                HttpURLConnection con = (HttpURLConnection) pageURL.openConnection();

                //String jsonString = getString(R.string.json);

                JsonThread myTask = new JsonThread(this, con, result);
                System.out.println("anan" + result);
                Thread t1 = new Thread(myTask, "JSON Thread");
                t1.start();
                //Toast.makeText(this, "debug", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                    browser.loadData(page, "text/html", "UTF-8");
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