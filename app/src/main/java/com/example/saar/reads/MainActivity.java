package com.example.saar.reads;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";

    GoogleApiClient myclient;
    String query = "https://www.googleapis.com/books/v1/volumes?q=";
    String key = "&printType=books&key=AIzaSyDvcVdbh1inZz8xWSUl3SAWfQFItXQ9Tuw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // user creds
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        myclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // personalisation
        SharedPreferences prefs = getSharedPreferences("Username", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hello " + name + "!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFiled:" + connectionResult);
    }

    @Override
    protected void onStart() {
        myclient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        myclient.disconnect();
        super.onStop();
    }

    public void bookFind(View view) {
        EditText bookFind = (EditText) findViewById(R.id.field);
        String bookString = bookFind.getText().toString();
        bookFind.setText("");

        if (bookString.length() == 0) {
            Toast.makeText(this, "Enter an identifier please!", Toast.LENGTH_SHORT).show();
        }

        else {
            bookString = bookString.replaceAll(" ", "%20");
            String url = query + bookString + key;

            Intent look = new Intent(MainActivity.this, BookSight.class);
            look.putExtra("url", url);
            startActivity(look);
        }
    }

    public void shelf(View view){
        Intent shelf = new Intent(MainActivity.this, BookShelf.class);
        shelf.putExtra("activity", "main");
        startActivity(shelf);
    }
}
