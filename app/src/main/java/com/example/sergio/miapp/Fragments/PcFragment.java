package com.example.sergio.miapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.sergio.miapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PcFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private Button boton;
    private TextView result;
    private OkHttpClient client;
    private Context mContext;
    View v;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pc, container, false);
        result = (TextView) v.findViewById(R.id.result);
        client = new OkHttpClient();
        mContext = getActivity();

        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchview);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar usuario");


        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }
    private void getWebservice(String s){
        final Request request = new Request.Builder().url("https://api.fortnitetracker.com/v1/profile/pc/"+s).header("TRN-Api-Key", "f85627ed-3968-41ec-b182-bf5af72cb54d").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("Error!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            result.setText(response.body().string());
                        }catch (IOException ioe){
                            result.setText("Error");
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        getWebservice(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return false;
    }

    @Override
    public void onClick(View view) {

    }
}
