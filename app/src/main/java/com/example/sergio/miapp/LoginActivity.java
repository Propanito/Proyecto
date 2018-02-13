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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

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
    FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.loadingAnim);
        lottieAnimationView.useExperimentalHardwareAcceleration(true);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        userEmail = (EditText) findViewById(R.id.eTLemail);
        userPass = (EditText) findViewById(R.id.eTLpass);
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

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

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
        String username = userEmail.getText().toString();

        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public void receiveData() {
        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        String username = preferences.getString("USERNAME", null);

        userEmail.setText(username);
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
        if (checkBox.isChecked()) {
            saveData();
            Toast.makeText(LoginActivity.this, "Recordar usuario guardado", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
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
                        }
                    };
                    setDelay.postDelayed(startDelay, 2550);
                    Toast.makeText(LoginActivity.this, "Bienvenido ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
