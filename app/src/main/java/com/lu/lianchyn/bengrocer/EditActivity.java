package com.lu.lianchyn.bengrocer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Edit Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        db = FirebaseFirestore.getInstance();
        stockname = (EditText) findViewById(R.id.StockName2);
        stockbarcode = (EditText) findViewById(R.id.stockBarcode2);
        stockDesc = (EditText) findViewById(R.id.stockDesc2);
        Qtty = (EditText) findViewById(R.id.QttyEdit2);
        priceEdit = (EditText) findViewById(R.id.PriceEditText2);
        cateEdit = (EditText) findViewById(R.id.cateEdit2);
        suppname = (EditText) findViewById(R.id.suppName2);
        saveBtn = (Button) findViewById(R.id.saveButton);




        //stockname.setText();

    }
}
