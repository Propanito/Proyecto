package com.example.sergio.miapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergio.miapp.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PcFragment extends Fragment implements View.OnClickListener {

    private Button boton;
    private TextView result;
    private OkHttpClient client;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pc, container, false);
        result = (TextView) v.findViewById(R.id.result);
        boton = (Button) v.findViewById(R.id.getBtn);
        boton.setOnClickListener(this);
        client = new OkHttpClient();

        return v;
    }

    @Override
    public void onClick(View view) {
            Toast.makeText(getActivity(), "Esto funciona hasta aqui", Toast.LENGTH_SHORT).show();
            getWebservice();

    }

    private void getWebservice(){
        final Request request = new Request.Builder().url("https://api.fortnitetracker.com/v1/profile/pc/propanito").header("TRN-Api-Key", "f85627ed-3968-41ec-b182-bf5af72cb54d").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("Fallo!");
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
                            result.setText("Error recogiendo body");
                        }
                    }
                });
            }
        });
    }
}
