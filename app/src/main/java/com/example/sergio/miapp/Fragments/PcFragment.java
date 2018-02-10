package com.example.sergio.miapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergio.miapp.Peticiones.Peticiones;
import com.example.sergio.miapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //Variables Escuadrón
    private TextView puntuaEs, winsEs,top10Es,top25Es, kdEs, partiEs, bajasEs, jugadoEs;

    //Variables Totales
    private TextView puntuaTo, winsTo, partiTo, bajasTo, kdTo, jugadoTo,top3To,top25To;

    ImageView imagenRandom;

    int[] images = new int[] {R.drawable.nom1, R.drawable.nom2, R.drawable.nom3,R.drawable.nom4, R.drawable.nom5, R.drawable.nom6};

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
        //Arranque de search
        inflater.inflate(R.menu.search_menu, menu);
        //Variables solo
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

        //Variables Escuadrón
        puntuaEs = (TextView)v.findViewById(R.id.puntuaEs);
        winsEs = (TextView)v.findViewById(R.id.winsEs);
        top10Es = (TextView)v.findViewById(R.id.top10Es);
        top25Es = (TextView)v.findViewById(R.id.top25Es);
        kdEs = (TextView)v.findViewById(R.id.kdEs);
        partiEs = (TextView)v.findViewById(R.id.partiEs);
        bajasEs = (TextView)v.findViewById(R.id.bajasEs);
        jugadoEs = (TextView)v.findViewById(R.id.jugadoEs);

        //Variables Totales
        puntuaTo = (TextView)v.findViewById(R.id.puntuaTo);
        winsTo = (TextView)v.findViewById(R.id.winsTo);
        partiTo = (TextView)v.findViewById(R.id.partiTo);
        bajasTo = (TextView)v.findViewById(R.id.bajasTo);
        kdTo = (TextView)v.findViewById(R.id.kdTo);
        jugadoTo = (TextView)v.findViewById(R.id.jugadoTo);
        top3To = (TextView)v.findViewById(R.id.top3To);
        top25To = (TextView)v.findViewById(R.id.top25To);

        //barras blancas
        view1 = (View) v.findViewById(R.id.barra1);

        //Imagen random
         imagenRandom = (ImageView)v.findViewById(R.id.imageRandom);


        MenuItem searchItem = menu.findItem(R.id.searchview);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar usuario");
        super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getWebservice(final String s) {
        final Request request = new Request.Builder().url("https://api.fortnitetracker.com/v1/profile/pc/"+s).header("TRN-Api-Key", "f85627ed-3968-41ec-b182-bf5af72cb54d").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        nomSolo.setText("No hay internet");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response)throws IOException  {
                final String myResponse = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
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

                            int imageId = (int)(Math.random() * images.length);
                            imagenRandom.setBackgroundResource(images[imageId]);
                            imagenRandom.setVisibility(View.VISIBLE);

                            //Variables SOLO
                            //------------------------------
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
                            //------------------------------
                            puntuaDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("score").getString("displayValue"));
                            puntuaDuo.setVisibility(View.VISIBLE);

                            //Victorias duo
                            winsDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("top1").getString("displayValue"));
                            winsDuo.setVisibility(View.VISIBLE);

                            //Top 10 duo
                            top10Duo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("top10").getString("displayValue"));
                            top10Duo.setVisibility(View.VISIBLE);

                            //Top 25 duo
                            top25Duo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("top25").getString("displayValue"));
                            top25Duo.setVisibility(View.VISIBLE);

                            //K/d duo
                            kdDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("kd").getString("displayValue"));
                            kdDuo.setVisibility(View.VISIBLE);

                            //Partidas jugadas duo
                            partiDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("matches").getString("displayValue"));
                            partiDuo.setVisibility(View.VISIBLE);

                            //Bajas duo
                            bajasDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("kills").getString("displayValue"));
                            bajasDuo.setVisibility(View.VISIBLE);

                            //Tiempo jugado duo
                            jugadoDuo.setText(json.getJSONObject("stats").getJSONObject("p10").getJSONObject("minutesPlayed").getString("displayValue"));
                            jugadoDuo.setVisibility(View.VISIBLE);

                            //Variables Escuadrón
                            //------------------------------
                            puntuaEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("score").getString("displayValue"));
                            puntuaEs.setVisibility(View.VISIBLE);

                            //Victorias Escuadrón
                            winsEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("top1").getString("displayValue"));
                            winsEs.setVisibility(View.VISIBLE);

                            //Top 10 Escuadrón
                            top10Es.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("top10").getString("displayValue"));
                            top10Es.setVisibility(View.VISIBLE);

                            //Top 25 Escuadrón
                            top25Es.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("top25").getString("displayValue"));
                            top25Es.setVisibility(View.VISIBLE);

                            //K/d Escuadrón
                            kdEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("kd").getString("displayValue"));
                            kdEs.setVisibility(View.VISIBLE);

                            //Partidas jugadas Escuadrón
                            partiEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("matches").getString("displayValue"));
                            partiEs.setVisibility(View.VISIBLE);

                            //Bajas Escuadrón
                            bajasEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("kills").getString("displayValue"));
                            bajasEs.setVisibility(View.VISIBLE);

                            //Tiempo jugado Escuadrón
                            jugadoEs.setText(json.getJSONObject("stats").getJSONObject("p9").getJSONObject("minutesPlayed").getString("displayValue"));
                            jugadoEs.setVisibility(View.VISIBLE);

                            //Variables Totales (Tiene que reccorrer un array)
                            //------------------------------
                            JSONArray lifeTimeStats = json.getJSONArray("lifeTimeStats");
                            for (int i = 0; i < lifeTimeStats.length(); i++) {
                                if (lifeTimeStats.getJSONObject(i).getString("key").equals("Score")){
                                    puntuaTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    puntuaTo.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Wins")){
                                    winsTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    winsTo.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Top 3")){
                                    top3To.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    top3To.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Top 25s")){
                                    top25To.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    top25To.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Kills")){
                                    bajasTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    bajasTo.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("K/d")){
                                    kdTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    kdTo.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Matches Played")){
                                    partiTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    partiTo.setVisibility(View.VISIBLE);
                                }else if(lifeTimeStats.getJSONObject(i).getString("key").equals("Time Played")){
                                    jugadoTo.setText(lifeTimeStats.getJSONObject(i).getString("value"));
                                    jugadoTo.setVisibility(View.VISIBLE);
                                }


                            }

                        }catch (JSONException  ioe){
                            nomSolo.setText("Jugador no encontrado!");
                            nomSolo.setVisibility(View.VISIBLE);
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
