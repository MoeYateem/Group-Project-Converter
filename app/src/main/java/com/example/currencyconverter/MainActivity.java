package com.example.currencyconverter;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.math.BigDecimal;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView buyView = findViewById(R.id.textView);
    TextView sellView = findViewById(R.id.textView2);
    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls){
            String result = "";
            URL url;
            HttpURLConnection http;

            try{
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection();

                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                StringBuilder s = new StringBuilder();
                BufferedReader b = new BufferedReader(reader);
                String l;
                while((l=b.readLine())!=null){
                    s.append(l+"\n");
                }
                b.close();
                return s.toString();
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }


        protected void onPostExecute(String values){
            super.onPostExecute(values);

            try{
                DecimalFormat formatter = new DecimalFormat("#,###");
                String[] sv = values.split("]") ;
                String[] sb =sv[0].split(",");
                String br = String.valueOf(formatter.format(Integer.parseInt(sb[1])));
                String[] ss = sv[1].split(",");
                String sR = String.valueOf(formatter.format(Integer.parseInt(ss[1])));
                buyView.setText(br);



            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    DownloadTask first_api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://localhost/mobile/scrap.php";
        first_api = new DownloadTask();
        first_api.execute(url);
    }


    public void onClickConvert(View view) {
        TextView conversionTextView = findViewById(R.id.output);
        EditText lbpEditText = findViewById(R.id.lbp_edit_text);
        EditText usdEditText = findViewById(R.id.usd_edit_text);
        if (!lbpEditText.getText().toString().equals("")) {
            String lbpValue = lbpEditText.getText().toString();
            BigDecimal usd = new BigDecimal(Double.parseDouble(lbpValue) / 20000);
            conversionTextView.setText(lbpValue + "LBP = " + String.valueOf(usd) + "$");
        }
        else if(lbpEditText.getText().toString().equals("") && usdEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            conversionTextView.setText("");
        }
        else {

            String usdValue = usdEditText.getText().toString();
            BigDecimal lbp = new BigDecimal(Double.parseDouble(usdValue) * 22000);
            conversionTextView.setText(usdValue + "$ = " + String.valueOf(lbp) + "LBP");
        }
    }
}