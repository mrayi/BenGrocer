package com.lu.lianchyn.bengrocer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.annotation.Nullable;

import io.opencensus.tags.Tag;

public class StockMgmt extends AppCompatActivity {


    private ArrayList<String> arrylist = new ArrayList<>();
    private ListView listview;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> mlist = new ArrayList<>();
    private static final String TAG = "StockMgmt";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_mgmt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setTitle("Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


                listview = (ListView) findViewById(R.id.stocklist);

                mAuth = FirebaseAuth.getInstance();
                /*db.collection("Stock").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            DocumentSnapshot documentSnapshot = ;

                            String java= documentSnapshot.getString("Stock_ID");
                        if (task.isSuccessful()){
                            List<DocumentSnapshot> listofDocument = task.getResult().getDocuments();

                        }
                    }
                })*/

             db.collection("Stock").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        for(DocumentSnapshot snapshot : documentSnapshots){

                            mlist.add(snapshot.getString("Stock_ID") + " " +snapshot.getString("Name") + "\n RM"  +snapshot.getDouble("Price") + "\t Qty: "
                            + snapshot.getDouble("Qty"));
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mlist);
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);
                    }
                });


            }
    class Item {
        private String id;
        private String name;
        private int quantity;
        private double price;

        private Item(String id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public void addQuantity(int qty) {
            quantity = quantity+qty;
        }
    }
        }





