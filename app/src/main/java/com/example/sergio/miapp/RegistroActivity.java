package com.example.sergio.miapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class RegistroActivity extends AppCompatActivity implements Validator.ValidationListener{

    @Min(value = 5, message = "Debe contener más de 5 letras")
    @NotEmpty(message = "No puede estar en blanco")

    private EditText eTRuser;
    @Email(message = "Debe ingresar un email válido")
    private EditText eTLemail;

    @Password(message = "Debe contener Mayus, num y minuscula", min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText eTRpass;
    @ConfirmPassword(message = "Las contraseñas no coinciden")
    private EditText eTRrpass;
    @Checked(message = "Acepta los términos para poder continuar")
    private CheckBox checkTerm;
    private Button registrar;

    Validator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        eTRuser = (EditText) findViewById(R.id.eTRuser);
        eTLemail = (EditText) findViewById(R.id.eTRemail);
        eTRpass = (EditText) findViewById(R.id.eTRpass);
        eTRrpass = (EditText) findViewById(R.id.eTRrpass);
        registrar = (Button) findViewById(R.id.btnRegistrar);
        checkTerm = (CheckBox) findViewById(R.id.checkTerm);

        validator = new Validator(this);
        validator.setValidationListener(this);



        //Botón registrar
        registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                validator.validate();
            }
        });
    }

    public void botonLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Bienvenido, inicia sesión para continuar!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
