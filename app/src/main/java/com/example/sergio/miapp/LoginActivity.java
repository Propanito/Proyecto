package com.example.sergio.miapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sergio.miapp.Opciones.SettingsActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {


    private static String PREFS = "PREFS";
    Validator validator;
    LottieAnimationView lottieAnimationView;
    Handler setDelay;
    Runnable startDelay;
    @NotEmpty(message = "No puede estar en blanco")
    @Email(message = "Debe ingresar un email válido")
    private EditText userEmail;
    @Password(message = "Debe contener Mayus, num y minuscula", min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText userPass;
    private CheckBox checkBox;
    private Button login;
    private TextView txtToda, txtReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lottieAnimationView = findViewById(R.id.loadingAnim);
        lottieAnimationView.useExperimentalHardwareAcceleration(true);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        userEmail = findViewById(R.id.eTLemail);
        userPass = findViewById(R.id.eTLpass);
        login = findViewById(R.id.btnLogin);
        checkBox = findViewById(R.id.checkBox);
        txtToda = findViewById(R.id.txtBajoLogin);
        txtReg = findViewById(R.id.Registrar);
        setDelay = new Handler();


        validator = new Validator(this);
        validator.setValidationListener(this);

        lottieAnimationView.setVisibility(View.INVISIBLE);
        txtToda.setVisibility(View.VISIBLE);
        txtReg.setVisibility(View.VISIBLE);
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        receiveData();


        //Botón login
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                lottieAnimationView.setVisibility(View.VISIBLE);
                txtToda.setVisibility(View.INVISIBLE);
                txtReg.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                startDelay = new Runnable() {
                    @Override
                    public void run() {

                        if (lottieAnimationView.isAnimating()) {
                            lottieAnimationView.cancelAnimation();
                            login.setText(getString(R.string.play));
                        } else {
                            lottieAnimationView.playAnimation();
                            login.setText(getString(R.string.pause));
                        }
                        validator.validate();
                    }
                };
                setDelay.postDelayed(startDelay, 2550);


            }
        });
    }

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    /////funciones para guardar email en login///////
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    public void saveData() {
        String usernamee = userEmail.getText().toString();

        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", usernamee);
        editor.commit();
    }

    public void receiveData() {
        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        String usernamee = preferences.getString("USERNAME", null);

        userEmail.setText(usernamee);
    }
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////


    //Botón en texto para ir a la pestaña de registro
    public void botonRegistrar(View view) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////


    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    ///////////funciones para validar login//////////
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    //si la validación es buena
    @Override
    public void onValidationSucceeded() {
        //Al pasar la validacion guarda los datos del email
        if (checkBox.isChecked()) {
            saveData();

        }

        final String username = userEmail.getText().toString();
        final String password = userPass.getText().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean success = obj.getBoolean("success");
                            if(success){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getString("name"),
                                                obj.getString("username")
                                        );
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                lottieAnimationView.setVisibility(View.INVISIBLE);
                                txtToda.setVisibility(View.VISIBLE);
                                txtReg.setVisibility(View.VISIBLE);
                                login.setVisibility(View.VISIBLE);
                                if (lottieAnimationView.isAnimating()) {
                                    lottieAnimationView.cancelAnimation();
                                    login.setText(getString(R.string.play));
                                } else {
                                    lottieAnimationView.playAnimation();
                                    login.setText(getString(R.string.pause));
                                }
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        /* Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        txtToda.setVisibility(View.VISIBLE);
                        txtReg.setVisibility(View.VISIBLE);
                        login.setVisibility(View.VISIBLE);
                        if (lottieAnimationView.isAnimating()) {
                            lottieAnimationView.cancelAnimation();
                            login.setText(getString(R.string.play));
                        } else {
                            lottieAnimationView.playAnimation();
                            login.setText(getString(R.string.pause));
                        }
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);*/
    }


    //Si la validacion falla.
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                txtToda.setVisibility(View.VISIBLE);
                txtReg.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                if (lottieAnimationView.isAnimating()) {
                    lottieAnimationView.cancelAnimation();
                    login.setText(getString(R.string.play));
                } else {
                    lottieAnimationView.playAnimation();
                    login.setText(getString(R.string.pause));
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////

}
