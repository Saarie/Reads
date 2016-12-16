package com.example.saar.reads;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Saar on 16-12-2016.
 */

public class dataTask extends AsyncTask<String, Void, String> {
    private HttpURLConnection connection;

    @Override
    protected String doInBackground(String...params){
        try {
            URL url = new URL(params[0]);
            try{
                connection = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder data = new StringBuilder();
                String row;

                while ((row = reader.readLine())!= null){
                    data.append(row + "\n");
                }
                reader.close();

                return data.toString();
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        // return the books to main
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
