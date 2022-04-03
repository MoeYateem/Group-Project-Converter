package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    int sellRate;
    int buyRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickConvert(View view) {
        TextView conversionTextView = findViewById(R.id.output);
        EditText lbpEditText = findViewById(R.id.lbp_edit_text);
        EditText usdEditText = findViewById(R.id.usd_edit_text);
        if (!lbpEditText.getText().toString().equals("")) {
            String lbpValue = lbpEditText.getText().toString();
            BigDecimal usd = new BigDecimal(Double.parseDouble(lbpValue) / sellRate);
            conversionTextView.setText(lbpValue + "LBP = " + String.valueOf(usd) + "$");
        }
        else if(lbpEditText.getText().toString().equals("") && usdEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            conversionTextView.setText("");
        }
        else {

            String usdValue = usdEditText.getText().toString();
            BigDecimal lbp = new BigDecimal(Double.parseDouble(usdValue) * buyRate);
            conversionTextView.setText(usdValue + "$ = " + String.valueOf(lbp) + "LBP");
        }
    }

}
