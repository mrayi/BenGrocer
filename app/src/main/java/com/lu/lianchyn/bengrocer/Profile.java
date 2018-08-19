package com.lu.lianchyn.bengrocer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.lu.lianchyn.bengrocer.MainActivity.name;
import static com.lu.lianchyn.bengrocer.MainActivity.position;
import static com.lu.lianchyn.bengrocer.MainActivity.icno;
import static com.lu.lianchyn.bengrocer.MainActivity.salary;
import static com.lu.lianchyn.bengrocer.MainActivity.sid;
import static com.lu.lianchyn.bengrocer.MainActivity.address;


public class Profile extends AppCompatActivity {

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final TextView nameID = (TextView)findViewById(R.id.nameID);
        final TextView positionID = (TextView)findViewById(R.id.positionID);
        final TextView icnoID = (TextView)findViewById(R.id.icnoID);
        final TextView salaryID = (TextView)findViewById(R.id.salaryID);
        final TextView sidID = (TextView)findViewById(R.id.sidID);
        final TextView addressID = (TextView)findViewById(R.id.addressID);

        nameID.setText(name);
        positionID.setText(position);
        icnoID.setText(icno);
        salaryID.setText(salary);
        sidID.setText(sid);
        addressID.setText(address);

    }

    public void btnChangePassword(View v){
        Intent i = new Intent(Profile.this, ChangePassword.class);
        startActivity(i);
    }
}
