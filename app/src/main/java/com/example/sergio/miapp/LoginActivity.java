package com.example.sergio.miapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.w3c.dom.Text;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {


    @NotEmpty(message = "No puede estar en blanco")
    @Email(message = "Debe ingresar un email válido")
    private EditText eTLemail;

    @Password(message = "Debe contener Mayus, num y minuscula", min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText eTLpass;
    private CheckBox checkBox;
    private Button login;
    private TextView txtToda, txtReg;
    Validator validator;
    private static String PREFS = "PREFS";
    LottieAnimationView lottieAnimationView;
    Handler setDelay;
    Runnable startDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.loadingAnim);
        lottieAnimationView.useExperimentalHardwareAcceleration(true);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        eTLemail = (EditText) findViewById(R.id.eTLemail);
        eTLpass = (EditText) findViewById(R.id.eTLpass);
        login = (Button) findViewById(R.id.btnLogin);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        txtToda = (TextView) findViewById(R.id.txtBajoLogin);
        txtReg = (TextView) findViewById(R.id.Registrar);

        setDelay = new Handler();


        validator = new Validator(this);
        validator.setValidationListener(this);

        lottieAnimationView.setVisibility(View.INVISIBLE);
        txtToda.setVisibility(View.VISIBLE);
        txtReg.setVisibility(View.VISIBLE);

        receiveData();

        //Botón login
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
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
                setDelay.postDelayed(startDelay, 3550);



            }
        });
    }

    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    /////funciones para guardar email en login///////
    /////////////////////////////////////////////////
    /////////////////////////////////////////////////
    public void saveData(){
        String username = eTLemail.getText().toString();

        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public void receiveData(){
        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        String username = preferences.getString("USERNAME", null);

        eTLemail.setText(username);
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
    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Correcto!", Toast.LENGTH_SHORT).show();
        if(checkBox.isChecked()){
            saveData();
            Toast.makeText(LoginActivity.this,"Recordar usuario guardado", Toast.LENGTH_SHORT).show();
        }

        //Introducir aquí código login//
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }


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
