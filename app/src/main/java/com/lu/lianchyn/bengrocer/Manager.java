package com.lu.lianchyn.bengrocer;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Manager extends AppCompatActivity {
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

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
