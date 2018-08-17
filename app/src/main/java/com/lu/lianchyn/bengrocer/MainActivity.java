package com.lu.lianchyn.bengrocer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText etUser;
    private EditText etPass;
    private Button btLogin;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        etUser = (EditText) findViewById(R.id.email);
        etPass = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.login);
    }

    public void login(View v) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
            return;
        }

        email = String.valueOf(etUser.getText());
        password = String.valueOf(etPass.getText());
        if(email == null || email.isEmpty()) {
            Toast.makeText(MainActivity.this, "E-mail is required.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(password == null || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Password is required.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference docRef = db.collection("Staff").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            Intent i;
                                            String position = (String) document.get("Position");
                                            switch(position) {
                                                case "Manager":
                                                    i = new Intent(MainActivity.this, Manager.class);
                                                    startActivity(i);
                                                    finish();
                                                    break;
                                                case "Inventory Staff":
                                                    i = new Intent(MainActivity.this, InventoryStaff.class);
                                                    startActivity(i);
                                                    finish();
                                                    break;
                                                case "Cashier":
                                                    i = new Intent(MainActivity.this, Cashier.class);
                                                    startActivity(i);
                                                    finish();
                                                    break;
                                                default:
                                                    Log.d(TAG, "PositionException: " + document.getData());
                                            }
                                        } else {
                                            // Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        // Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
