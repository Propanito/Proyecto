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
import com.example.sergio.miapp.R;
import java.util.ArrayList;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private ArrayList<Card> mData;
    private Activity mACtivity;

    public CardAdapter(ArrayList<Card> data, Activity activity) {
        this.mData = data;
        this.mACtivity = activity;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Card card = mData.get(position);

        holder.setTitle(card.getTitle());
        holder.setBody(card.getBody());
        holder.setDate(card.getDate());

        Glide.with(mACtivity)
                .load(card.getImg())
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
        TextView titleTextView, dateTextView, bodyTextView;



        public CardHolder(View itemView) {
            super(itemView);

            imgView = (ImageView) itemView.findViewById(R.id.imgView);
            titleTextView = (TextView) itemView.findViewById(R.id.textview_card_name);
            bodyTextView = (TextView) itemView.findViewById(R.id.card_body_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.fecha);
        }

        public void setTitle(String title) {
            titleTextView.setText(title);
        }

        public void setBody(String body) {
            bodyTextView.setText(body);
        }

        public void setDate(String date){
            dateTextView.setText(date);
        }




    }
}
