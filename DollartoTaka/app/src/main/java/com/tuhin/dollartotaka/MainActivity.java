package com.tuhin.dollartotaka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convert(View view){

        EditText myEditText = (EditText) findViewById(R.id.dollarInput);
        String dollarStr = myEditText.getText().toString();
        Double dollar = Double.parseDouble(dollarStr);
        Toast.makeText(getApplicationContext(), String.valueOf(dollar * 80.17) + " taka", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
