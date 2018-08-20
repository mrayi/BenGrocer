package com.lu.lianchyn.bengrocer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddStock extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mauth;
    private EditText stockname;
    private EditText stockbarcode;
    private EditText stockDesc;
    private EditText Qtty;
    private EditText priceEdit;
    private EditText cateEdit;
    private EditText suppname;
    private Button addBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);



        getSupportActionBar().setTitle("Add Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        db = FirebaseFirestore.getInstance();
        stockname = (EditText) findViewById(R.id.StockName);
        stockbarcode = (EditText) findViewById(R.id.stockBarcode);
        stockDesc = (EditText) findViewById(R.id.stockDesc);
        Qtty = (EditText) findViewById(R.id.QttyEdit);
        priceEdit = (EditText) findViewById(R.id.PriceEditText);
        cateEdit = (EditText) findViewById(R.id.cateEdit);
        suppname = (EditText) findViewById(R.id.suppName);
        addBtn = (Button) findViewById(R.id.doneButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = stockname.getText().toString();
                String barcode = stockbarcode.getText().toString();
                String desc = stockDesc.getText().toString();
                int quantity = Integer.parseInt(Qtty.getText().toString());
                double price = Double.parseDouble(priceEdit.getText().toString());
                String category = cateEdit.getText().toString();
                String supplier = suppname.getText().toString();

                if(     name == null || name.isEmpty() ||
                        barcode == null || barcode.isEmpty() ||
                        desc == null || desc.isEmpty() ||
                        Qtty.getText() == null || priceEdit.getText() == null ||
                        quantity == 0 || price == 0 ||
                        category == null || category.isEmpty() ||
                        supplier == null || supplier.isEmpty())
                {
                    Toast.makeText(AddStock.this, "All fields are required.", Toast.LENGTH_LONG).show();

                }else {


                    Map<String, Object> stockMap = new HashMap<>();


                    stockMap.put("Category", category);
                    stockMap.put("Description", desc);
                    stockMap.put("Name", name);
                    stockMap.put("Price", price);
                    stockMap.put("Qty", quantity);
                    stockMap.put("Stock_ID", barcode);
                    stockMap.put("SupplierName", supplier);


                    db.collection("Stock").document(barcode).set(stockMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void evoid) {
                            Toast.makeText(AddStock.this, "Item added to Stock", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddStock.this, StockActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(AddStock.this, "Error to add stock: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }
}
