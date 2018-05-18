package com.example.sergio.miapp.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sergio.miapp.Model.Card;
import com.example.sergio.miapp.Model.CardTienda;
import com.example.sergio.miapp.R;

import java.util.ArrayList;


public class TiendaAdapter extends RecyclerView.Adapter<TiendaAdapter.CardHolder> {

    private ArrayList<CardTienda> mData;
    private Activity mACtivity;

    public TiendaAdapter(ArrayList<CardTienda> data, Activity activity) {
        this.mData = data;
        this.mACtivity = activity;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tienda, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        CardTienda cardTienda = mData.get(position);
        Glide.with(mACtivity)
                .load(cardTienda.getImg())
                .into(holder.imgView);
    }
    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        ImageView imgView;

        public CardHolder(View itemView) {
            super(itemView);

            imgView = (ImageView) itemView.findViewById(R.id.imgView);
        }


    }
}
