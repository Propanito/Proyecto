package com.example.sergio.miapp.Pestañas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.sergio.miapp.HomeActivity;
import com.example.sergio.miapp.LoginActivity;
import com.example.sergio.miapp.R;
import com.example.sergio.miapp.SharedPrefManager;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.mikepenz.materialdrawer.Drawer;

public class MapaActivity extends AppCompatActivity {


    Toolbar toolbar;
    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Si la sesion no está iniciada se abrirá automaticamente el login
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        toolbar = (Toolbar) findViewById(R.id.toolbarCofres);
        //Titulo superior
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mapa");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        // Any implementation of ImageView can be used!
        mImageView = (ImageView) findViewById(R.id.mapView);

        // Set the Drawable displayed
        Drawable bitmap = getResources().getDrawable(R.drawable.map);
        mImageView.setImageDrawable(bitmap);

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(mImageView);
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
