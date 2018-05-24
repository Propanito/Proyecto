package com.example.sergio.miapp.Pestañas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sergio.miapp.Constants;
import com.example.sergio.miapp.HomeActivity;
import com.example.sergio.miapp.LoginActivity;
import com.example.sergio.miapp.Model.Card;
import com.example.sergio.miapp.R;
import com.example.sergio.miapp.RequestHandler;
import com.example.sergio.miapp.SharedPrefManager;
import com.example.sergio.miapp.adaptadores.CardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mCardRecyclerView;
    private CardAdapter mAdapter;
    private ArrayList<Card> mCardCollection;
    Toolbar toolbar;
    String sendTitle;
    String sendBody;
    String sendImgUrl;
    String sendFecha;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Si la sesion no está iniciada se abrirá automaticamente el login//
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        ////////////////////////////////////////////////////////////////////


        toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        //Titulo superior
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Noticias");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        init();
        new FetchDataTask().execute();

    }

    private void init() {
        mCardRecyclerView = (RecyclerView) findViewById(R.id.card_recycler);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardRecyclerView.setHasFixedSize(true);
        mCardCollection = new ArrayList<>();
        mAdapter = new CardAdapter(mCardCollection, this);
        mCardRecyclerView.setAdapter(mAdapter);

    }

    public class FetchDataTask extends AsyncTask<Void, Void, Void> {
                private String mNewsString;
                @Override
                protected Void doInBackground(Void... params) {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    Uri builtUri = Uri.parse(getString(R.string.news_api));
                    URL url;
                    try {
                        String title;
                        String body;
                        String imgUrl;
                        String date;
                        url = new URL(builtUri.toString());
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Accept-Language", "es-ES,es;q=0.5");
                        urlConnection.connect();

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null) {
                            //Nothing to do
                            return null;
                        }

                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line + "\n");
                        }

                        if (buffer.length() == 0) {
                            return null;
                        }

                        mNewsString = buffer.toString();
                        JSONObject e = new JSONObject(mNewsString).getJSONObject("battleroyalenews");
                        date = e.getString("lastModified");
                        String fechasubString = date.substring(0, 10);

                        JSONObject f = e.getJSONObject("news");
                        JSONArray cardsArray = f.getJSONArray("messages");
                        //list = new ArrayList<>();
                        for (int i = 0; i < cardsArray.length(); i++) {

                            Log.v("BRAD_", i + "");
                            JSONObject jCard = (JSONObject) cardsArray.get(i);
                            title = jCard.getString("title");
                            body = jCard.getString("body");
                            imgUrl = jCard.getString("image");

                            Card card = new Card();
                            card.setTitle(title);
                            card.setBody(body);
                            card.setImg(imgUrl);
                            card.setDate(fechasubString);
                            mCardCollection.add(card);

                        }
                        //Guardar noticias
                        for (int i = 0; i < 3;i++) {

                            Log.v("BRAD_", i + "");
                            JSONObject mJsonObject = cardsArray.getJSONObject(i);
                            sendTitle = mJsonObject.getString("title");
                            sendBody = mJsonObject.getString("body");
                            sendImgUrl = mJsonObject.getString("image");
                            String recogerFecha = e.getString("lastModified").toString();
                            sendFecha = recogerFecha.substring(0, 10);

                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (final IOException e) {
                                Log.e("NewsActivity", "Error closing stream", e);
                            }
                        }
                    }
                    return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
            enviarNoticias();


        }
    }

    public void enviarNoticias(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_STORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(NewsActivity.this,"Datos guardados", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", sendTitle);
                params.put("body", sendBody);
                params.put("imgUrl", sendImgUrl);
                params.put("date", sendFecha);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
