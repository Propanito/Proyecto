package com.example.sergio.miapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sergio on 18/01/2018.
 */

public class SliderAdapter extends PagerAdapter {

    //Lista de imagenes
    public int[] slider_images = {
            R.drawable.fort1,
            R.drawable.fort2,
            R.drawable.fort3,
            R.drawable.logo4
    };
    public String[] slide_headings = {
            "¡Empezamos!",
            "Busca tus estadisticas",
            "Últimas noticias",
            "Estado de servidores"
    };
    public String[] slide_descs = {
            "Bienvenido a Wasted On Fortnite",
            "Usa el buscador para conocer tus estadisticas",
            "Conoce las últimas actualizaciones de Fortnite",
            "Averigua si los servidores están caidos"
    };
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);


        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slider_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
