package com.lu.lianchyn.bengrocer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mauth;
    private EditText stockname;
    private EditText stockbarcode;
    private EditText stockDesc;
    private EditText Qtty;
    private EditText priceEdit;
    private EditText cateEdit;
    private EditText suppname;
    private Button saveBtn;
    private Button deleteBtn;
    private String stockid;
    private String namestock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Edit Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        stockid = bundle.get("Stock_ID").toString();


        //Toast.makeText(EditActivity.this, "Value: " + stockid, Toast.LENGTH_LONG).show();
        db = FirebaseFirestore.getInstance();
        stockname = (EditText) findViewById(R.id.StockName2);
        stockbarcode = (EditText) findViewById(R.id.stockBarcode2);
        stockDesc = (EditText) findViewById(R.id.stockDesc2);
        Qtty = (EditText) findViewById(R.id.QttyEdit2);
        priceEdit = (EditText) findViewById(R.id.PriceEditText2);
        cateEdit = (EditText) findViewById(R.id.cateEdit2);
        suppname = (EditText) findViewById(R.id.suppName2);
        saveBtn = (Button) findViewById(R.id.saveButton);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);



        DocumentReference docRef = db.collection("Stock").document(stockid);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("Name").toString();
                            String barcode = documentSnapshot.getString("Stock_ID").toString();
                            String desc = documentSnapshot.getString("Description").toString();
                            int qty = documentSnapshot.getDouble("Qty").intValue();
                            double price = documentSnapshot.getDouble("Price");
                            String cat = documentSnapshot.getString("Category");
                            String supp = documentSnapshot.getString("SupplierName");
                            namestock = name;

                            stockname.setText(namestock);
                            stockbarcode.setText(barcode);
                            stockDesc.setText(desc);
                            Qtty.setText("" + qty);
                            priceEdit.setText(String.format("%.2f", price));
                            cateEdit.setText(cat);
                            suppname.setText(supp);

                            saveBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String name = stockname.getText().toString();
                                    String barcode = stockbarcode.getText().toString();
                                    String desc = stockDesc.getText().toString();
                                    int quantity = Integer.parseInt(Qtty.getText().toString());
                                    double price = Double.parseDouble(priceEdit.getText().toString());
                                    String category = cateEdit.getText().toString();
                                    String supplier = suppname.getText().toString();

                                    if (name == null || name.isEmpty() ||
                                            barcode == null || barcode.isEmpty() ||
                                            desc == null || desc.isEmpty() ||
                                            Qtty.getText() == null || priceEdit.getText() == null ||
                                            quantity == 0 || price == 0 ||
                                            category == null || category.isEmpty() ||
                                            supplier == null || supplier.isEmpty()) {
                                        Toast.makeText(EditActivity.this, "All fields are required.", Toast.LENGTH_LONG).show();

                                    } else {


                                        Map<String, Object> stockMap = new HashMap<>();


                                        stockMap.put("Category", category);
                                        stockMap.put("Description", desc);
                                        stockMap.put("Name", name);
                                        stockMap.put("Price", price);
                                        stockMap.put("Qty", quantity);
                                        stockMap.put("Stock_ID", barcode);
                                        stockMap.put("SupplierName", supplier);


                                        db.collection("Stock").document(barcode).update(stockMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void evoid) {

                                                Toast.makeText(EditActivity.this, "Item added to Stock", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EditActivity.this,StockActivity.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                String error = e.getMessage();

                                                Toast.makeText(EditActivity.this, "Error to add stock: " + error, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });

                            deleteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new AlertDialog.Builder(EditActivity.this)
                                            .setMessage("Are you sure want to delete?").setCancelable(false)
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    db.collection("Stock").document(stockid)
                                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(EditActivity.this, "You have successfully deleted ", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent (EditActivity.this, StockActivity.class);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("No",null).show();
                                    return;
                                }
                            });

                        }
                    }

                });






    }

}

