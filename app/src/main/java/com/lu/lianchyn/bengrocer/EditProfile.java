package com.lu.lianchyn.bengrocer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.lu.lianchyn.bengrocer.MainActivity.name;
import static com.lu.lianchyn.bengrocer.MainActivity.address;


public class EditProfile extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = FirebaseFirestore.getInstance();


        final EditText editnameID = (EditText) findViewById(R.id.editnameID);
        final EditText editaddressID = (EditText) findViewById(R.id.editaddressID);
        editnameID.setText(name.toString());
        editaddressID.setText(address.toString());

    }
    public void btnEditProfile(View v){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final EditText editnameID = (EditText) findViewById(R.id.editnameID);
        final EditText editaddressID = (EditText) findViewById(R.id.editaddressID);

        db.collection("Staff").document(user.getUid()).update(
          "Name",editnameID.getText().toString(),
          "Address",editaddressID.getText().toString()
        );

        DocumentReference docRef = db.collection("Staff").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Intent i;

                        name = (String) document.get("Name");
                        address = (String) document.get("Address");
                    }
                }
            }
        });
        Toast.makeText(EditProfile.this, "Edit Profile Successful", Toast.LENGTH_LONG).show();
        finish();
    }
}
