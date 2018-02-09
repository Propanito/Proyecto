package com.example.sergio.miapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergio.miapp.Peticiones.Peticiones;
import com.example.sergio.miapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PcFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    //Variables solo
    private TextView nomSolo, puntuaSolo, winsSolo,top10Solo,top25Solo, kdSolo, partiSolo, bajasSolo, jugadoSolo;

    //Variables Duo
    private TextView puntuaDuo, winsDuo,top10Duo,top25Duo, kdDuo, partiDuo, bajasDuo, jugadoDuo;

    //-----------------
    private Context mContext;
    private OkHttpClient client;
    private View view1;
    View v;
    private Context applicationContext;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        client = new OkHttpClient();
        setHasOptionsMenu(true);




    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pc, container, false);
        mContext = getActivity();
        

        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        nomSolo = (TextView)v.findViewById(R.id.nombreSolo);
        puntuaSolo = (TextView)v.findViewById(R.id.puntuaSolo);
        winsSolo = (TextView)v.findViewById(R.id.winsSolo);
        top10Solo = (TextView)v.findViewById(R.id.top10Solo);
        top25Solo = (TextView)v.findViewById(R.id.top25Solo);
        kdSolo = (TextView)v.findViewById(R.id.kdSolo);
        partiSolo = (TextView)v.findViewById(R.id.partiSolo);
        bajasSolo = (TextView)v.findViewById(R.id.bajasSolo);
        jugadoSolo = (TextView)v.findViewById(R.id.jugadoSolo);

        //Variables duo
        puntuaDuo = (TextView)v.findViewById(R.id.puntuaDuo);
        winsDuo = (TextView)v.findViewById(R.id.winsDuo);
        top10Duo = (TextView)v.findViewById(R.id.top10Duo);
        top25Duo = (TextView)v.findViewById(R.id.top25Duo);
        kdDuo = (TextView)v.findViewById(R.id.kdDuo);
        partiDuo = (TextView)v.findViewById(R.id.partiDuo);
        bajasDuo = (TextView)v.findViewById(R.id.bajasDuo);
        jugadoDuo = (TextView)v.findViewById(R.id.jugadoDuo);

        //barras blancas
        view1 = (View) v.findViewById(R.id.view1);
        MenuItem searchItem = menu.findItem(R.id.searchview);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar usuario");


        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getWebservice(String s) {
        final Request request = new Request.Builder().url("https://api.fortnitetracker.com/v1/profile/pc/"+s).header("TRN-Api-Key", "f85627ed-3968-41ec-b182-bf5af72cb54d").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nomSolo.setText("Error!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response)throws IOException  {

                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //Llamadas json
                            JSONObject json = new JSONObject(myResponse);

                            //Barras blancas
                            view1.setVisibility(View.VISIBLE);

                            //Usuario
                            nomSolo.setText(json.getString("epicUserHandle"));
                            nomSolo.setVisibility(View.VISIBLE);

                            //Variables SOLO

                            //Puntuación solo
                            puntuaSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("score").getString("displayValue"));
                            puntuaSolo.setVisibility(View.VISIBLE);

                            //Victorias solo
                            winsSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top1").getString("displayValue"));
                            winsSolo.setVisibility(View.VISIBLE);

                            //Top 10 solo
                            top10Solo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top10").getString("displayValue"));
                            top10Solo.setVisibility(View.VISIBLE);

                            //Top 25 solo
                            top25Solo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top25").getString("displayValue"));
                            top25Solo.setVisibility(View.VISIBLE);

                            //K/d solo
                            kdSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("kd").getString("displayValue"));
                            kdSolo.setVisibility(View.VISIBLE);

                            //Partidas jugadas solo
                            partiSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("matches").getString("displayValue"));
                            partiSolo.setVisibility(View.VISIBLE);

                            //Bajas solo
                            bajasSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("kills").getString("displayValue"));
                            bajasSolo.setVisibility(View.VISIBLE);

                            //Tiempo jugado solo
                            jugadoSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("minutesPlayed").getString("displayValue"));
                            jugadoSolo.setVisibility(View.VISIBLE);

                            //Variables DUO

                            puntuaSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("score").getString("displayValue"));
                            puntuaSolo.setVisibility(View.VISIBLE);

                            //Victorias duo
                            winsSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top1").getString("displayValue"));
                            winsSolo.setVisibility(View.VISIBLE);

                            //Top 10 duo
                            top10Solo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top10").getString("displayValue"));
                            top10Solo.setVisibility(View.VISIBLE);

                            //Top 25 duo
                            top25Solo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top25").getString("displayValue"));
                            top25Solo.setVisibility(View.VISIBLE);

                            //K/d duo
                            kdSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("kd").getString("displayValue"));
                            kdSolo.setVisibility(View.VISIBLE);

                            //Partidas jugadas duo
                            partiSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("matches").getString("displayValue"));
                            partiSolo.setVisibility(View.VISIBLE);

                            //Bajas duo
                            bajasSolo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("kills").getString("displayValue"));
                            bajasSolo.setVisibility(View.VISIBLE);

                            //Tiempo jugado duo
                            jugadoDuo.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("minutesPlayed").getString("displayValue"));
                            jugadoDuo.setVisibility(View.VISIBLE);


                        }catch (JSONException  ioe){
                            nomSolo.setText("Error");
                        }
                    }
                });
            }
        });
    }


    //Codigo searchview
    @Override
    public boolean onQueryTextSubmit(String s) {
        getWebservice(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return false;
    }


    //Esto no se usa
    @Override
    public void onClick(View view) {

    }

    public Context getApplicationContext() {
        return applicationContext;
    }
}
