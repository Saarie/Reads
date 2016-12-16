package com.example.saar.reads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Saar on 16-12-2016.
 */

public class BookSight extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // firebase
    private static final String TAG = "BookSight";
    private FirebaseAuth auth;
    GoogleApiClient myclient;

    // layout of main screen
    private RecyclerView recyclerView;
    private GridLayoutManager gridManager;
    private CustomAdapter adapter;
    private List<BookData> list;
    static View.OnClickListener myOCL;
    String info;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksearch);

        // user creds
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        myclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Onclicklistener on a cardview
        myOCL = new MyOnClickListener(this);

        // Setting the viewer
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();


        // get url
        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");
        getData(url);

        gridManager = new GridLayoutManager(this);
        recyclerView.setLayoutManager(gridManager);

        adapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void getData(String url) {
        try {
            info = new dataTask().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(info);
            JSONArray jsonArray = jsonObject.getJSONArray("books#volumes");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                JSONObject volInfo = jsonObj.getJSONObject("volumeInfo");
                JSONObject jsonImg = jsonObj.getJSONObject("imageLinks");

                String bookid = jsonObj.getString("id");
                Double rating = jsonObj.getDouble("averageRating");
                String title = volInfo.getString("title");
                String plot = volInfo.getString("description");
                String author = volInfo.getJSONArray("authors").getString(0);
                String imgUrl = jsonImg.getString("small");


                BookData data = new BookData(bookid, title, author, plot, imgUrl, rating);

                list.add(data);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
