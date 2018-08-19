package com.lu.lianchyn.bengrocer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Manager extends AppCompatActivity {
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
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
                    Toast.makeText(Manager.this,"Profile",Toast.LENGTH_SHORT).show();
                }


                else if(id == R.id.aboutus){
                    Toast.makeText(Manager.this,"About Us",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.logout){
                    FirebaseAuth.getInstance().signOut();

                    Intent i = new Intent(Manager.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(Manager.this,"Logged Out", Toast.LENGTH_LONG).show();

                }

                return true;
            }
        });


        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Staff").setIndicator("Staff"),
                StaffFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Member").setIndicator("Member"),
                MemberFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Stock").setIndicator("Stock"),
                StockFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("POS").setIndicator("POS"),
                PosFragment.class, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AddStaffFragment addStaffFragment = (AddStaffFragment)getSupportFragmentManager().findFragmentByTag("AddStaff");
        if(addStaffFragment != null && addStaffFragment.isVisible() && item.getItemId() == android.R.id.home) {
            android.support.v4.app.FragmentManager fm  = getSupportFragmentManager();
            Fragment prevFrag = new StaffFragment();
            fm.popBackStack();
            fm.beginTransaction()
                    .replace(R.id.realtabcontent, prevFrag)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return  abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Manager.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
