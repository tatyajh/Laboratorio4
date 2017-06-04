package co.edu.udea.compumovil.gr05_20171.lab4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects

    private EditText etUserRegister;
    private EditText etDateRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText confirmPass;
    private Button buttonSignup;
    private Button buttonCancelar;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        //initializing views
        etUserRegister = (EditText) findViewById(R.id.etUserRegister);
        etDateRegister = (EditText) findViewById(R.id.etDateRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        confirmPass = (EditText) findViewById(R.id.etPasswordConfirmer);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);


        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonCancelar.setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser() {


        String user = etUserRegister.getText().toString().trim();
        String date = etDateRegister.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            Toast pass = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
            pass.show();

        }
        //checking if email and passwords are empty


        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "Por favor ingrese el usuario", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Por favor ingrese la edad", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingrese el email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingrese la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Por favor ingrese nuevamente la contraseña", Toast.LENGTH_LONG).show();
            return;
        }


        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registrando, espere por favor...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            //display some message here
                            Toast.makeText(MainActivity.this, "Error en el registro", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if (view == buttonSignup) {
            registerUser();
        }

        if (view == textViewSignin) {
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view == buttonCancelar) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}