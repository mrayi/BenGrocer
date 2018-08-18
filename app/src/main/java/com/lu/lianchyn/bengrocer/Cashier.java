package com.lu.lianchyn.bengrocer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Cashier extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.profile){
                    Toast.makeText(Cashier.this,"Profile",Toast.LENGTH_SHORT).show();
                }


                else if(id == R.id.aboutus){
                    Toast.makeText(Cashier.this,"About Us",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.logout){
                    FirebaseAuth.getInstance().signOut();

                    Intent i = new Intent(Cashier.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(Cashier.this,"Logged Out", Toast.LENGTH_LONG).show();

                }

                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return  abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Cashier.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
