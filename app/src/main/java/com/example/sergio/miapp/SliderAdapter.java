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
            "Bievenido a FortniteApp",
            "Usa el buscador",
            "¿Eres de consolas?",
            "Gracias por elegirnos"
    };
    public String[] slide_descs = {
            "Te damos la bievenida a FortniteApp",
            "Para descubrir tus estadisticas usa el buscador situado en la parte superior de la app",
            "Desliza el menú de la izquierda y selecciona tu plataforma",
            "Disfruta de nuestra aplicación"
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
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);


        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

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
