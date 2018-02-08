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

    private TextView nom, puntua, wins,top10;
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
        nom = (TextView)v.findViewById(R.id.nombre);
        puntua = (TextView)v.findViewById(R.id.puntua);
        wins = (TextView)v.findViewById(R.id.wins);
        top10 = (TextView)v.findViewById(R.id.top10);


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
                        nom.setText("Error!");
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
                            //Usuario
                            nom.setText(json.getString("epicUserHandle"));
                            nom.setVisibility(View.VISIBLE);
                            view1.setVisibility(View.VISIBLE);

                            puntua.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("score").getString("displayValue"));
                            puntua.setVisibility(View.VISIBLE);

                            wins.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top1").getString("displayValue"));
                            wins.setVisibility(View.VISIBLE);

                            top10.setText(json.getJSONObject("stats").getJSONObject("p2").getJSONObject("top10").getString("displayValue"));
                            top10.setVisibility(View.VISIBLE);
                        }catch (JSONException  ioe){
                            nom.setText("Error");
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
