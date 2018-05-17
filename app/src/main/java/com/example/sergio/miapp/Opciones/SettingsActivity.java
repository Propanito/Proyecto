package com.example.sergio.miapp.Opciones;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sergio.miapp.HomeActivity;
import com.example.sergio.miapp.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingsActivity extends AppCompatActivity {


    private Button btnReset;
    private EditText etreset;
    private ProgressBar progressBar;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etreset = findViewById(R.id.eTreset);
        btnReset = findViewById(R.id.btnReset);
        progressBar = findViewById(R.id.progressBar);

        //Barra superior
        toolbar = (Toolbar) findViewById(R.id.toolbarOpciones);
        toolbar.setTitle("Opciones");

        //Sobre nosotros
        simulateDayNight(/* DAY */ 0);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");
        Element adsElement = new Element();
        adsElement.setTitle("Sobre nosotros");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .addItem(versionElement)
                .addItem(adsElement)
                .addEmail("wastedonfortnite@gmail.com")
                .addWebsite("http://imprintseries/sergio/index.html")
                .addTwitter("WastedOnFortnite")
                .addPlayStore("com.example.sergio.miapp")
                .create();

        setContentView(aboutPage);
    }

    //Metodo para comprobar si es de dia o de noche y pondrá un estilo claro u oscuro.//
    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
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
