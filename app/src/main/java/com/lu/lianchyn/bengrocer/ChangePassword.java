package com.lu.lianchyn.bengrocer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private EditText newpasswordID;
    private EditText confirmpasswordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        newpasswordID = (EditText)findViewById(R.id.newpassowrdID);
        confirmpasswordID = (EditText)findViewById(R.id.confirmpasswordID);

    }

    public void btnSubmit(View v){
        final ProgressDialog progressDialog = ProgressDialog.show(ChangePassword.this, "Please Wait...","Processing...", true);

        if(newpasswordID.getText().toString().equals(confirmpasswordID.getText().toString())){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(newpasswordID.getText().toString());
            progressDialog.dismiss();
            Toast.makeText(ChangePassword.this,"Change Password Successful", Toast.LENGTH_LONG).show();

            finish();
        }else{
            progressDialog.dismiss();
            Toast.makeText(ChangePassword.this,"Please Make Sure Two Password Input Are The Same", Toast.LENGTH_LONG).show();

        }
    }



}
