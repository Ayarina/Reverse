package com.example.reverse.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import com.example.reverse.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reverse.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private Button botonRegister;
    private EditText username, correo, contraseña, contraseñaCheck;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("https://reverse-f3fee-default-rtdb.europe-west1.firebasedatabase.app/");

        username = findViewById(R.id.username_register);
        correo = findViewById(R.id.correo_register);
        contraseña = findViewById(R.id.contraseña_register);
        contraseñaCheck = findViewById(R.id.contraseña_register_check);
        botonRegister = findViewById(R.id.boton_register);

        botonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contraseña.getText().toString().equals(contraseñaCheck.getText().toString()))
                    createAccount(correo.getText().toString(), contraseña.getText().toString());
                else
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            writeNewUser(user.getUid(),username.getText().toString(), email);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            Toast.makeText(RegisterActivity.this, "Se ha registrado satisfactoriamente", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void writeNewUser(String userId, String name, String email) {
        Usuario user = new Usuario(name, email);
        myRef.child("users").child(userId).setValue(user);
    }
}