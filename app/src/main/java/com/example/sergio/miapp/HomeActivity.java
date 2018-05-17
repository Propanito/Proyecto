package com.example.sergio.miapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sergio.miapp.Opciones.SettingsActivity;
import com.example.sergio.miapp.Pestañas.ArmasActivity;
import com.example.sergio.miapp.Pestañas.MapaActivity;
import com.example.sergio.miapp.Pestañas.NewsActivity;
import com.example.sergio.miapp.Pestañas.StateActivity;
import com.example.sergio.miapp.Pestañas.TiendaActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity  {

    private Drawer result = null;
    private AccountHeader headerResult = null;
    private EditText buscar;
    private TextView resultado;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        buscar = (EditText) findViewById(R.id.bpEdit);
        resultado = (TextView) findViewById(R.id.bpText);
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        //Titulo superior (se queda
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buscar perfil");
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.header)
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Buscar perfil").withIcon(FontAwesome.Icon.faw_id_card);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Mapa").withIcon(FontAwesome.Icon.faw_map);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Noticias").withIcon(FontAwesome.Icon.faw_newspaper);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Estado de servidor").withIcon(FontAwesome.Icon.faw_server);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("Armas").withIcon(FontAwesome.Icon.faw_fighter_jet);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Tienda").withIcon(FontAwesome.Icon.faw_shopping_cart);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("Opciones").withIcon(FontAwesome.Icon.faw_filter);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("Cerrar sesión").withIcon(FontAwesome.Icon.faw_sign_out_alt);

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,item2,item3,item4,item5,item6,
                        new DividerDrawerItem(),
                        item7,
                        item8
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(HomeActivity.this, MapaActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(HomeActivity.this, NewsActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(HomeActivity.this, StateActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(HomeActivity.this, ArmasActivity.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(HomeActivity.this, TiendaActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(HomeActivity.this, SettingsActivity.class);
                            } else if (drawerItem.getIdentifier() == 8) {
                                SharedPrefManager.getInstance(HomeActivity.this).logout();
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                finish();
                            }
                            if (intent != null) {
                                HomeActivity.this.startActivity(intent);
                                finish();
                            }
                        }

                        return false;
                    }
                })
                .withTranslucentStatusBar(false)
                .withSelectedItem(-1)
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        ////////////////////////////////////////////////////////////////////////////////////////////




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


    private void buscarPerfil(){
        final String name = buscar.getText().toString();
        String url = "https://wastedonfortnite.000webhostapp.com/connect/fortniteapi.php";

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            resultado.setText(json.getString("username"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", name);
                return params;
            }
        };


        MyRequestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void bpClick(View view) {
        buscarPerfil();
    }
}
