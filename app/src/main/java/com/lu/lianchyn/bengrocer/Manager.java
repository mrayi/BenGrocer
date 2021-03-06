package com.lu.lianchyn.bengrocer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
                    Intent i = new Intent(Manager.this, Profile.class);
                    startActivity(i);
                }


                else if(id == R.id.aboutus){
                    Intent i = new Intent(Manager.this, AboutUs.class);
                    startActivity(i);
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
        ViewStaffFragment viewStaffFragment = (ViewStaffFragment)getSupportFragmentManager().findFragmentByTag("ViewStaff");
        EditStaffFragment editStaffFragment = (EditStaffFragment)getSupportFragmentManager().findFragmentByTag("EditStaff");
        AddMemberFragment addMemberFragment = (AddMemberFragment)getSupportFragmentManager().findFragmentByTag("AddMember");
        ViewMemberFragment viewMemberFragment = (ViewMemberFragment)getSupportFragmentManager().findFragmentByTag("ViewMember");
        EditMemberFragment editMemberFragment = (EditMemberFragment)getSupportFragmentManager().findFragmentByTag("EditMember");
        if(
                (addStaffFragment != null && addStaffFragment.isVisible() && item.getItemId() == android.R.id.home) ||
                (viewStaffFragment != null && viewStaffFragment.isVisible() && item.getItemId() == android.R.id.home)
        ) {
            rebuild();
            return true;
        } else if((editStaffFragment != null && editStaffFragment.isVisible() && item.getItemId() == android.R.id.home)) {
            new AlertDialog.Builder(this)
                    .setMessage("Discard change?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            rebuild();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", null).show();
            return true;
        } else if(
                (addMemberFragment != null && addMemberFragment.isVisible() && item.getItemId() == android.R.id.home) ||
                        (viewMemberFragment != null && viewMemberFragment.isVisible() && item.getItemId() == android.R.id.home)

                ) {
            rebuild_m();
            return true;
        } else if((editMemberFragment != null && editMemberFragment.isVisible() && item.getItemId() == android.R.id.home)) {
            new AlertDialog.Builder(this)
                    .setMessage("Discard change?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            rebuild_m();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", null).show();
            return true;
        }
        return  abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AddStaffFragment addStaffFragment = (AddStaffFragment)getSupportFragmentManager().findFragmentByTag("AddStaff");
        ViewStaffFragment viewStaffFragment = (ViewStaffFragment)getSupportFragmentManager().findFragmentByTag("ViewStaff");
        EditStaffFragment editStaffFragment = (EditStaffFragment)getSupportFragmentManager().findFragmentByTag("EditStaff");
        AddMemberFragment addMemberFragment = (AddMemberFragment)getSupportFragmentManager().findFragmentByTag("AddMember");
        ViewMemberFragment viewMemberFragment = (ViewMemberFragment)getSupportFragmentManager().findFragmentByTag("ViewMember");
        EditMemberFragment editMemberFragment = (EditMemberFragment)getSupportFragmentManager().findFragmentByTag("EditMember");
        if(
                (addStaffFragment != null && addStaffFragment.isVisible()) ||
                (viewStaffFragment != null && viewStaffFragment.isVisible())
        ) {
            rebuild();
            return;
        } else if(editStaffFragment != null && editStaffFragment.isVisible()) {
            new AlertDialog.Builder(this)
                    .setMessage("Discard change?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            rebuild();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", null).show();
            return;
        } else if(
                (addMemberFragment != null && addMemberFragment.isVisible()) ||
                (viewMemberFragment != null && viewMemberFragment.isVisible())
                ) {
            rebuild_m();
            return;
        } else if(editMemberFragment != null && editMemberFragment.isVisible()) {
            new AlertDialog.Builder(this)
                    .setMessage("Discard change?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            rebuild_m();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", null).show();
            return;
        }
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

    public void rebuild() {
        android.support.v4.app.FragmentManager fm  = getSupportFragmentManager();
        // Fragment prevFrag = new StaffFragment();

        fm.popBackStack(null, fm.POP_BACK_STACK_INCLUSIVE);
            /*
            fm.beginTransaction()
                    .replace(R.id.realtabcontent, prevFrag)
                    .addToBackStack(null)
                    .commit();
            */
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        mTabHost.addTab(mTabHost.newTabSpec("Staff").setIndicator("Staff"),
                StaffFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Member").setIndicator("Member"),
                MemberFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Stock").setIndicator("Stock"),
                StockFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("POS").setIndicator("POS"),
                PosFragment.class, null);
        mTabHost.setCurrentTab(3);
        mTabHost.setCurrentTab(0);
    };

    public void rebuild_m() {
        android.support.v4.app.FragmentManager fm  = getSupportFragmentManager();
        // Fragment prevFrag = new StaffFragment();

        fm.popBackStack(null, fm.POP_BACK_STACK_INCLUSIVE);
            /*
            fm.beginTransaction()
                    .replace(R.id.realtabcontent, prevFrag)
                    .addToBackStack(null)
                    .commit();
            */
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        mTabHost.addTab(mTabHost.newTabSpec("Staff").setIndicator("Staff"),
                StaffFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Member").setIndicator("Member"),
                MemberFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Stock").setIndicator("Stock"),
                StockFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("POS").setIndicator("POS"),
                PosFragment.class, null);
        mTabHost.setCurrentTab(3);
        mTabHost.setCurrentTab(1);
    };
}
