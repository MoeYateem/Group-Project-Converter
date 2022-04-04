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
    TextView BuyView;//Initializing the views for the JSON Objects from the scrap API
    TextView SellView;
    int b_Rate;//Initializing the variables that will hold the values of the json objects
    int s_Rate;
    public class DownloadTask extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... urls){ //HTTP Connection to API1
            String result = "";
            URL url;
            HttpURLConnection http;

            try{
                url = new URL(urls[0]);
                http = (HttpURLConnection) url.openConnection(); //Initializing the connection

                InputStream in = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while( data != -1){
                    char current = (char) data; //reading the data from the scrap api
                    result += current; //appending the data
                    data = reader.read();

                }
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

            return result; //returning the object
        }


        protected void onPostExecute(String s) { //Retrieving and formatting the JSON objects
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s); //creating a new instance of a json object
                b_Rate=json.getInt("buy_dude"); //Hooking the returned jsonobjects to a java variable
                s_Rate=json.getInt("sell_dude"); //same here
                BuyView= findViewById(R.id.textView);
                SellView=findViewById(R.id.textView2);
                BuyView.setText(b_Rate);//Appending the values to the text views for the user to view
                SellView.setText(s_Rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class API_Add extends AsyncTask<String, String, String> { //HTTP/URL connection function for the database API
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
                con.setRequestMethod("Post");
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
        String url = "http://10.21.128.147/mobile/scrap.php"; //Linking to the first API that retrieves data from Lirarate
        DownloadTask first_api = new DownloadTask();//Initializing connection to it
        first_api.execute(url); //Retrieving the JSON objects to the frontend
        String Url_API = "http://10.21.128.147/mobile/api2.php";//Linking to the second API that sends data to the database
        API_Add do_it = new API_Add();//Initializing connection to this API
        String str = "amount ,currency ,rate";
        do_it.execute(Url_API, str);




    }


    public void onClickConvert(View view) { //A function for the button that converts
        TextView conversionTextView = findViewById(R.id.output);

        EditText lbpEditText = findViewById(R.id.lbp_edit_text);
        EditText usdEditText = findViewById(R.id.usd_edit_text);
        if (!lbpEditText.getText().toString().equals("")) { //Checks if there is input in the LBP section
            String lbpValue = lbpEditText.getText().toString();
            BigDecimal usd = new BigDecimal(Double.parseDouble(lbpValue) / b_Rate);
            conversionTextView.setText(lbpValue + "LBP = " + String.valueOf(usd) + "$"); //Converts to USD
        }
        else if(lbpEditText.getText().toString().equals("") && usdEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            conversionTextView.setText("");
            //sends a toast if the user tried pressing the button without any input
        }
        else {

            String usdValue = usdEditText.getText().toString();
            BigDecimal lbp = new BigDecimal(Double.parseDouble(usdValue) * s_Rate);
            conversionTextView.setText(usdValue + "$ = " + String.valueOf(lbp) + "LBP"); //Converts to LBP
        }
    }
}