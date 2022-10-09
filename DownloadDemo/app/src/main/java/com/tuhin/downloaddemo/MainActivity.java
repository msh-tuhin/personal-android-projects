package com.tuhin.downloaddemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    TextView myText;

    public void downloadCtrl(View view){
        DownloadTask task = new DownloadTask();
        String result;
        try {
            result = task.execute("https://www.ecowebhosting.co.uk/").get();
        } catch (Exception e) {
            result = "Can not execute";
        }

        myText.setText(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = (TextView) findViewById(R.id.textView);

    }
}
class DownloadTask extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... urls){
        URL url;
        HttpURLConnection urlConnection;
        String result = "";
        try{
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
//            while( data != -1 ){
//                result += (char) data;
//                data = reader.read();
//            }
            return  result = "Download Successful";
        } catch(Exception e) {
            return "Faults Occured";
        }
    }

}
