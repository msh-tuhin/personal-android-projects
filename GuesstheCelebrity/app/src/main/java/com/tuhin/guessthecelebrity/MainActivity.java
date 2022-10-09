package com.tuhin.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //<div class="sidebarContainer">
    //http://www.posh24.se/kandisar

    ArrayList<String> celebURL = new ArrayList<String>();
    ArrayList<String> celebName = new ArrayList<String>();
    ImageView imageView;
    Button options[] = new Button[4];
    Bitmap myBitmap;
    String answer;
    Random random;
    int chosenCeleb, answerPosition;

    public void buttonClicked(View view) {
        String input = view.getTag().toString();
        int inputInt = Integer.parseInt(input);
        if((inputInt - 1) == answerPosition){
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Oops!!Correct answer is: " + answer, Toast.LENGTH_SHORT).show();
        }
        chosenCeleb = random.nextInt(celebURL.size());
        ImageLoad imageTask = new ImageLoad();
        try{
            myBitmap = imageTask.execute(celebURL.get(chosenCeleb)).get();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Some Problem!!!", Toast.LENGTH_SHORT).show();
        }
        imageView.setImageBitmap(myBitmap);
        answer = celebName.get(chosenCeleb);
        answerPosition = createOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        options[0] = (Button)findViewById(R.id.button1);
        options[1] = (Button) findViewById(R.id.button2);
        options[2] = (Button) findViewById(R.id.button3);
        options[3] = (Button) findViewById(R.id.button4);
        String result = null;
        DownloadTask task = new DownloadTask();
        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();
        }
        String[] stripResult = result.split("<div class=\"sidebarContainer\">");

        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(stripResult[0]);
        while(m.find()) {
            celebURL.add(m.group(1));
        }
        Pattern p1 = Pattern.compile("alt=\"(.*?)\"");
        Matcher m1 = p1.matcher(stripResult[0]);

        while (m1.find()){
            celebName.add(m1.group(1));
        }

        random = new Random();
        chosenCeleb = random.nextInt(celebURL.size());
        ImageLoad imageTask = new ImageLoad();
        try {
            myBitmap = imageTask.execute(celebURL.get(chosenCeleb)).get();
        } catch (Exception e) {

        }

        imageView.setImageBitmap(myBitmap);
        answer = celebName.get(chosenCeleb);
        answerPosition = createOptions();

    }

    int createOptions(){

        int randomCeleb;
        int position = random.nextInt(4);
        for(int i=0; i<=3; i++){
            if(i == position) {
                options[i].setText(answer);
            } else {
                do{
                    randomCeleb = random.nextInt(celebURL.size());
                }while (randomCeleb == chosenCeleb);
                options[i].setText(celebName.get(randomCeleb));
            }
        }
        return position;
    }
}

class DownloadTask extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... urls) {
        URL url;
        HttpURLConnection connection;
        String result = "";
        try{
            url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1){
                result += (char) data;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            return null;
        }

    }
}

class ImageLoad extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... urls) {

        URL url;
        HttpURLConnection connection;
        try{
            url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (Exception e) {

        }
        return null;
    }


}
