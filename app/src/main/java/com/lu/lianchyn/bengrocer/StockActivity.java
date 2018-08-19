package com.lu.lianchyn.bengrocer;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class StockActivity extends AppCompatActivity {

    private ArrayList<String> arrylist = new ArrayList<>();
    private ListView listview;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> mlist = new ArrayList<>();
    private static final String TAG = "StockMgmt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        getSupportActionBar().setTitle("Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listview = (ListView) findViewById(R.id.stockList);


        FabSpeedDial fabSpeedDial = (FabSpeedDial)findViewById(R.id.fabStock1);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Intent intent = new Intent(StockActivity.this, AddStock.class);
                startActivity(intent);
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

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
    public void onBackPressed() {
       // FragmentManager fm = getFragmentManager();
        Intent intent = new Intent(StockActivity.this, StockFragment.class);
        startActivity(intent);
    }

}
