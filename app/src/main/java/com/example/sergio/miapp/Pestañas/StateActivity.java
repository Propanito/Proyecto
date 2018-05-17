package com.example.sergio.miapp.Pestañas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.sergio.miapp.HomeActivity;
import com.example.sergio.miapp.Model.Card;
import com.example.sergio.miapp.R;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

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


public class StateActivity extends AppCompatActivity {

    TextView funcionaa;
    private Flashbar flashbar = null;
    Toolbar toolbar;
    RequestQueue requestQueue;
    Button check;
    String JsonURL = "https://lightswitch-public-service-prod06.ol.epicgames.com/lightswitch/api/service/bulk/status?serviceId=Fortnite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new KeyboardUtil(this, findViewById(R.id.prueba));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        toolbar = (Toolbar) findViewById(R.id.toolbarState);
        //Titulo superior
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Estado de servidor");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);



        requestQueue = Volley.newRequestQueue(this);
        funcionaa = (TextView) findViewById(R.id.up);
        check = (Button) findViewById(R.id.show);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchDataTask().execute();
            }
        });


    }


    public class FetchDataTask extends AsyncTask<Void, Void, Void> {
        private String mNewsString;
        String status=null;
        String mantenimiento=null;
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri builtUri = Uri.parse(getString(R.string.state_api));
            URL url;
            try {
                url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
                JSONArray estado = new JSONArray(mNewsString);
                //list = new ArrayList<>();
                for (int i = 0; i < estado.length(); i++) {

                    JSONObject state = (JSONObject) estado.get(i);
                    status = state.getString("status");
                    mantenimiento = state.getString("maintenanceUri");
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
                        Log.e("StateActivity", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //Si el servidor funciona
           if (status.equals("UP")){
               if (flashbar == null) {
                   flashbar = serverUP();
               }
               flashbar.show();
            //Si el servidor no funciona
           }else{
               if (flashbar == null) {
                   flashbar = serverDown();
               }
               flashbar.show();
           }
        }
    }
    //
    private Flashbar serverUP() {
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(5000)
                .backgroundDrawable(R.drawable.bg_gradient)
                .title("Online")
                .message(
                        "Los servidores funcionan correctamente")
                .showIcon()
                .icon(R.drawable.check_circle)
                .iconColorFilterRes(R.color.colorWhite)
                .iconAnimation(FlashAnim.with(this)
                        .animateIcon()
                        .pulse()
                        .alpha()
                        .duration(750)
                        .accelerate())
                .build();
    }
    private Flashbar serverDown() {
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(5000)
                .backgroundDrawable(R.drawable.error_gradient)
                .title("Offline")
                .message(
                        "Los servidores están en mantenimiento hasta nuevo aviso")
                .showIcon()
                .icon(R.drawable.error_server)
                .iconColorFilterRes(R.color.colorWhite)
                .iconAnimation(FlashAnim.with(this)
                        .animateIcon()
                        .pulse()
                        .alpha()
                        .duration(750)
                        .accelerate())
                .build();
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
