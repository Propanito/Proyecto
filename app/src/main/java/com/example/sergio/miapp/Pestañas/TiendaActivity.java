package com.example.sergio.miapp.Pestañas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.sergio.miapp.HomeActivity;
import com.example.sergio.miapp.LoginActivity;
import com.example.sergio.miapp.Model.CardTienda;
import com.example.sergio.miapp.R;
import com.example.sergio.miapp.SharedPrefManager;
import com.example.sergio.miapp.adaptadores.TiendaAdapter;

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
import java.util.ArrayList;

public class TiendaActivity extends AppCompatActivity {
    private RecyclerView mTiendaRecyclerView;
    private TiendaAdapter mAdapter;
    private ArrayList<CardTienda> mTiendaCollection;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarTienda);
        //Titulo superior
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tienda de aspectos");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //Cargamos datos y mostramos recycler
        init();
        new FetchDataTask().execute();
    }

    private void init() {
        mTiendaRecyclerView = (RecyclerView) findViewById(R.id.tienda_recycler);
        mTiendaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTiendaRecyclerView.setHasFixedSize(true);
        mTiendaCollection = new ArrayList<>();
        mAdapter = new TiendaAdapter(mTiendaCollection, this);
        mTiendaRecyclerView.setAdapter(mAdapter);
    }

    public class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private String mTiendaString;

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri builtUri = Uri.parse(getString(R.string.tienda_api));
            URL url;
            try {
                url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-Key", "8b0evZtSS45Ocqc4Gw2B");
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
                mTiendaString = buffer.toString();
                JSONObject e = new JSONObject(mTiendaString).getJSONObject("br");
                JSONArray cardsArray = e.getJSONArray("weekly");
                for (int i = 0; i < cardsArray.length(); i++) {
                    Log.v("BRAD_", i + "");
                    String imgUrl;
                    JSONObject jCard = (JSONObject) cardsArray.get(i);
                    imgUrl = jCard.getString("imgURL");
                    CardTienda card = new CardTienda();
                    card.setImg(imgUrl);
                    mTiendaCollection.add(card);
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
                        Log.e("TiendaActivity", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
        }
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
