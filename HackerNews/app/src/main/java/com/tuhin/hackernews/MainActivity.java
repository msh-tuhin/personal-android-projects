package com.tuhin.hackernews;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase storyDB;
    ListView listView;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        try {
            storyDB = this.openOrCreateDatabase("Stories", MODE_PRIVATE, null);
            storyDB.execSQL("CREATE TABLE IF NOT EXISTS stories (title VARCHAR, url VARCHAR);");
            //createDB();
            createList();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), YourStory.class);
                    i.putExtra("urlStory", urlList.get(position));
                    startActivity(i);
                }
            });
            Log.i("finish", "app done");
        } catch(Exception e) {
            Log.i("ERROR:", "database error");
        }

        //top stories
        //https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty
        //specified story
        //https://hacker-news.firebaseio.com/v0/item/14118290.json?print=pretty

    }
    public void createDB(){
        String result = "";
        JSONArray arr = null;
        try {
            result = new DownloadData()
                    .execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            arr = new JSONArray(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //storyDB.execSQL("DELETE * FROM stories;");
        storyDB.delete("stories", null, null);
        if(arr != null) {
            for(int i=0;i<arr.length();i++){
                try{
                    String story = new DownloadData().execute("https://hacker-news.firebaseio.com/v0/item/"
                            + arr.getString(i) + ".json?print=pretty").get();
                    JSONObject jo = new JSONObject(story);
                    String title = jo.getString("title");
                    String url = jo.getString("url");
                    Log.i("title:", title);
                    Log.i("url:", url);
                    String command = "INSERT INTO stories (title,url) VALUES ('" +title + "','" + url + "');";
                    storyDB.execSQL(command);

                }catch (Exception e){
                    Log.i("story:", "not found");
                }
            }
        }
    }

    public void createList(){
        try {
            Cursor c = storyDB.rawQuery("SELECT * FROM stories", null);
            int titleIndex = c.getColumnIndex("title");
            int urlIndex = c.getColumnIndex("url");
            c.moveToFirst();
            Log.i("message", "before while");
            while(c != null){
                titleList.add(c.getString(titleIndex));
                urlList.add(c.getString(urlIndex));
                c.moveToNext();
            }
            Log.i("message", "out of while");

        }catch (Exception e) {
            Log.i("error:", "in createList");
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titleList);
            listView.setAdapter(arrayAdapter);
            storyDB.close();
        }
    }
}

class DownloadData extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String...params) {
        URL url;
        HttpURLConnection connection;
        String result = "";
        try{
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while(data != -1) {
                result += (char)data;
                data = reader.read();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Failed";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
