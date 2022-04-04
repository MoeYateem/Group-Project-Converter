package com.example.currencyconverter;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.math.BigDecimal;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView BuyView;
    TextView SellView;
    public class DownloadTask extends AsyncTask<String, Void, String> {

        int b_Rate;
        int s_Rate;
        protected String doInBackground(String... urls){
            String result = "";
            URL url;
            HttpURLConnection http;

            try{
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection();

                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while( data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

            return result;
        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);
                b_Rate=json.getInt("buy_dude");
                s_Rate=json.getInt("sell_dude");
                BuyView= findViewById(R.id.textView);
                SellView=findViewById(R.id.textView2);
                BuyView.setText(b_Rate);
                SellView.setText(s_Rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class API_Add extends AsyncTask<String, String, String> {
        HttpURLConnection http;
        HttpURLConnection con;
        URL url;
        @Override
        protected String doInBackground(String... p) {
            String urlString = p[0];
            String data = p[1];
            OutputStream out = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                out = new BufferedOutputStream(con.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(data);
                con.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "http://10.21.128.147/mobile/scrap.php";
        DownloadTask first_api = new DownloadTask();
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