package com.tuhin.downloadingimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView cartoon;

    public void downloadImage(View view){

        //https://encrypted-tbn3.gstatic.com/images?
        // q=tbn:ANd9GcROrT6-Qv7Km_7B78yn6tbfrlZkQlNokeSnBwxmNs38h3drgo4XEA
        String imageUrl = "https://encrypted-tbn3.gstatic.com/images?" +
                "q=tbn:ANd9GcROrT6-Qv7Km_7B78yn6tbfrlZkQlNokeSnBwxmNs38h3drgo4XEA";
        DownloadImage down = new DownloadImage();
        try {
            Bitmap myBitmap = down.execute(imageUrl).get();
            cartoon.setImageBitmap(myBitmap);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartoon = (ImageView) findViewById(R.id.imageView);
    }
}

class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... urls) {
        URL url ;
        HttpURLConnection connection;

        try {
            url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            //connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;

        } catch (IOException e) {
//            e.printStackTrace();
        } catch (Exception e) {

        }

        return null;
    }
}
