package com.lu.lianchyn.bengrocer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText editTextStaffID;
    private EditText editTextStaffName;
    private EditText editTextMemberID;
    private EditText editTextMemberName;
    private ListView listViewItem;
    private ArrayList<Item> itemList;
    private EditText editTextItemID;
    private EditText editTextQuantity;
    private Button buttonAdd;
    private Button buttonRemove;
    private TextView textViewTotal;
    private Button buttonPay;
    private EditText editTextPayment;
    private String nextID;
    private int nextInvoiceItemID;
    private ItemAdapter itemAdapter;
    private Button buttonClear;
    private String receipt;
    private int remain;
    private int memberPoint;
    private boolean memberExist;
    private String memberEmail;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public PosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PosFragment newInstance(String param1, String param2) {
        PosFragment fragment = new PosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_pos, container, false);

        editTextStaffID = v.findViewById(R.id.editTextStaffID);
        editTextStaffName = v.findViewById(R.id.editTextStaffName);
        editTextMemberID = v.findViewById(R.id.editTextMemberID);
        editTextMemberName = v.findViewById(R.id.editTextMemberName);
        listViewItem = v.findViewById(R.id.listViewItem);
        editTextItemID = v.findViewById(R.id.editTextItemID);
        editTextQuantity = v.findViewById(R.id.editTextQuantity);
        buttonAdd = v.findViewById(R.id.buttonAdd);
        buttonRemove = v.findViewById(R.id.buttonRemove);
        textViewTotal = v.findViewById(R.id.textViewTotal);
        buttonPay = v.findViewById(R.id.buttonPay);
        editTextPayment = v.findViewById(R.id.editTextPayAmount);
        buttonClear = v.findViewById(R.id.buttonClear);

        memberExist= false;
        memberEmail = "";

        //get and show the staff id and name


        DocumentReference docRef = db.collection("Staff").document(user.getUid());

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            editTextStaffID.setText(documentSnapshot.getString("sid"));
                            editTextStaffName.setText(documentSnapshot.getString("Name"));
                        } else {
                            Toast.makeText(getActivity(), "Staff not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        //when member id entered
        editTextMemberID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextMemberName.setText("");
                memberEmail = "";
                memberExist=false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editTextMemberID.getText().toString().matches("")) {
                    DocumentReference docRe = db.collection("Member").document(editTextMemberID.getText().toString());
                    docRe.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        editTextMemberName.setText(documentSnapshot.getString("F_NAME") + " " + documentSnapshot.getString("L_NAME"));
                                        memberPoint = documentSnapshot.getDouble("POINTS").intValue();
                                        memberExist = true;
                                        memberEmail = documentSnapshot.getString("EMAIL");
                                    } else {
                                        editTextMemberName.setText("Member Not Found");
                                        memberExist = false;
                                        memberEmail = "";
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        //init item list view
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter();
        listViewItem.setAdapter(itemAdapter);


        //onclick insert item
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(editTextQuantity.getText().toString().matches("") || editTextQuantity.getText().toString().matches("0") || editTextItemID.getText().toString().matches(""))) {

                    DocumentReference docRe = db.collection("Stock").document(editTextItemID.getText().toString());
                    docRe.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {
                                        int stock = documentSnapshot.getDouble("Qty").intValue();
                                        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                                        boolean exist = false;
                                        for (int i = 0; i < itemList.size(); i++) {
                                            if (editTextItemID.getText().toString().matches(itemList.get(i).getId())) {
                                                if ((quantity + itemList.get(i).getQuantity()) > stock) {
                                                    int left = stock - itemList.get(i).getQuantity();
                                                    Toast.makeText(getActivity(), "Quantity entered is exceed", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getActivity(), "The stock have " + left + " left", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    itemList.get(i).addQuantity(Integer.parseInt(editTextQuantity.getText().toString()));
                                                    itemAdapter.notifyDataSetChanged();
                                                    calTotal();
                                                    Toast.makeText(getActivity(), "Successfully added " + editTextQuantity.getText() + " of " + itemList.get(i).getName(), Toast.LENGTH_SHORT).show();
                                                }
                                                exist = true;
                                            }
                                        }
                                        if (!exist) {
                                            if (!(stock == 0 || quantity > stock)) {
                                                String itemID = documentSnapshot.getString("Stock_ID");
                                                String itemName = documentSnapshot.getString("Name");
                                                double price = documentSnapshot.getDouble("Price");
                                                Item item = new Item(itemID, itemName, quantity, price, stock);
                                                itemList.add(item);
                                                itemAdapter.notifyDataSetChanged();
                                                calTotal();
                                                Toast.makeText(getActivity(), "Successfully added " + quantity + " of " + itemName, Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (quantity > stock) {
                                                    Toast.makeText(getActivity(), "Quantity entered is exceed", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getActivity(), "The stock have " + stock + " left", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(getActivity(), "Item is Out of Stock", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Item ID Not Found", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    if (editTextItemID.getText().toString().matches(""))
                        Toast.makeText(getActivity(), "Please key in item id", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Item Quantity cannot be empty or 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //for delete
        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editTextItemID.setText(itemList.get(i).getId());
                editTextQuantity.setText("");
                editTextQuantity.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextQuantity, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(editTextItemID.getText().toString().matches("") || editTextQuantity.getText().toString().matches("") || editTextQuantity.getText().toString().matches("0"))) {
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    boolean exist = false;
                    for (int i = 0; i < itemList.size(); i++) {
                        if (editTextItemID.getText().toString().matches(itemList.get(i).getId())) {
                            if ((quantity > itemList.get(i).getQuantity())) {
                                Toast.makeText(getActivity(), "Quantity entered is exceed", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "There are " + itemList.get(i).getQuantity() + " of " + itemList.get(i).getName() + "in list", Toast.LENGTH_SHORT).show();
                            } else {
                                final int index = i;
                                final int qty = quantity;
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Are you sure to remove " + quantity + " of " + itemList.get(index).getName() + "?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                itemList.get(index).minusQuantity(qty);

                                                itemAdapter.notifyDataSetChanged();
                                                calTotal();
                                                Toast.makeText(getActivity(), "Successfully removed " + editTextQuantity.getText() + " of " + itemList.get(index).getName(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            exist = true;
                        }
                    }
                    if (!exist) {
                        Toast.makeText(getActivity(), "Item ID not in list", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (editTextItemID.getText().toString().matches(""))
                        Toast.makeText(getActivity(), "Please key in item id", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Item Quantity cannot be empty or 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(editTextPayment.getText().toString().matches("") || editTextPayment.getText().toString().matches("0"))) {
                    double payment = Double.parseDouble(editTextPayment.getText().toString());
                    double total = 0.0;
                    for (int i = 0; i < itemList.size(); i++)
                        total += itemList.get(i).getPrice() * itemList.get(i).getQuantity();

                    if (total == 0) {
                        Toast.makeText(getActivity(), "There is no item in the pay list", Toast.LENGTH_SHORT).show();
                    } else if (payment < total) {
                        Toast.makeText(getActivity(), "Pay Amount is not enough", Toast.LENGTH_SHORT).show();
                    } else {
                        Query docRe = db.collection("Invoice").orderBy("Date", Query.Direction.DESCENDING).limit(1);
                        docRe.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    nextID = document.getId();

                                }
                            }
                        });
                        Query docListRef = db.collection("Invoice_List").orderBy("id", Query.Direction.DESCENDING).limit(1);

                        docListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    nextInvoiceItemID = document.getDouble("id").intValue();

                                }
                            }
                        });

                        double left = payment - total;


                        if (nextID != null) {
                            nextID = "POS" + String.format("%05d", Integer.parseInt(nextID.replace("POS", "")) + 1);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            //Receipt init
                            receipt = "BenGrocer"+System.getProperty("line.separator")+df.format(c.getTime())+System.getProperty("line.separator")+"Cashier ID: "+editTextStaffID.getText().toString()+System.getProperty("line.separator")+"Cashier Name: "+editTextStaffName.getText().toString()+System.getProperty("line.separator")+String.format("%8s %30s %5s %10s %10s","Item ID","Name", "Qty", "Price", "Sub Total")+System.getProperty("line.separator");

                            Map<String, Object> invoice = new HashMap<>();
                            invoice.put("Date", FieldValue.serverTimestamp());
                            invoice.put("Staff_ID", user.getUid());
                            invoice.put("Total_Amount", total);

                            db.collection("Invoice").document(nextID)
                                    .set(invoice)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Success insert invoice", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed insert invoice", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            for (int i = 0; i < itemList.size(); i++) {
                                nextInvoiceItemID++;

                                receipt += String.format("%8s %30s %5d %10.2f %10.2f",itemList.get(i).getId(),itemList.get(i).getName(), itemList.get(i).getQuantity(), itemList.get(i).getPrice(), itemList.get(i).getQuantity() * itemList.get(i).getPrice())+System.getProperty("line.separator");

                                Map<String, Object> invoiceList = new HashMap<>();
                                invoiceList.put("Invoice_ID", nextID);
                                invoiceList.put("Price", itemList.get(i).getPrice());
                                invoiceList.put("Qty", itemList.get(i).getQuantity());
                                invoiceList.put("Stock_ID", itemList.get(i).getId());
                                invoiceList.put("id", nextInvoiceItemID);

                                db.collection("Invoice_List").document(Integer.toString(nextInvoiceItemID))
                                        .set(invoiceList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Success insert invoice list", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Failed insert invoice list", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                remain = itemList.get(i).getStock() - itemList.get(i).getQuantity();
                                Map<String, Object> stock = new HashMap<>();
                                stock.put("Qty",remain);

                                db.collection("Stock").document(itemList.get(i).getId())
                                        .set(stock, SetOptions.merge());



                                receipt += System.getProperty("line.separator") +"Total: "+ total+System.getProperty("line.separator");

                                if(memberExist){

                                     int point = (int) (memberPoint + total);
                                    Map<String, Object> member = new HashMap<>();
                                    member.put("POINTS",point);


                                    db.collection("Member").document(editTextMemberID.getText().toString())
                                            .set(member, SetOptions.merge());

                                    receipt+= System.getProperty("line.separator")+"Member ID: "+editTextMemberID.getText().toString()+System.getProperty("line.separator")+"Member Name: "+editTextMemberName.getText().toString() +System.getProperty("line.separator")+ "Points Earned: " +(int) total ;





                                }

                                Toast.makeText(getActivity(), receipt, Toast.LENGTH_LONG).show();

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto",memberEmail, null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BenGrocer Receipt");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, receipt);
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                            }
                            resetLayout();

                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "Payment cannot be empty or 0", Toast.LENGTH_SHORT).show();

                }
            }
        });


        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLayout();
            }
        });
        return v;
    }

    //update total view
    public void calTotal() {
        double total = 0;
        for (int i = 0; i < itemList.size(); i++)
            total += itemList.get(i).getPrice() * itemList.get(i).getQuantity();

        textViewTotal.setText("Total: " + String.format("%.2f", total));


    }

    public void resetLayout(){
        editTextMemberID.setText("");
        editTextMemberName.setText("");
        itemList.clear();
        itemAdapter.notifyDataSetChanged();
        editTextItemID.setText("");
        editTextQuantity.setText("");
        textViewTotal.setText("Total: ");
        editTextPayment.setText("");
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_list, null);

            TextView textViewShowItemID = view.findViewById(R.id.textViewShowItemID);
            TextView textViewShowItemName = view.findViewById(R.id.textViewShowName);
            TextView textViewShowQuantity = view.findViewById(R.id.textViewShowQuantity);
            TextView textViewShowPrice = view.findViewById(R.id.textViewShowPrice);
            TextView textViewShowSubTotal = view.findViewById(R.id.textViewShowSubTotal);

            textViewShowItemID.setText(itemList.get(i).getId());
            textViewShowItemName.setText(itemList.get(i).getName());
            textViewShowQuantity.setText(Integer.toString(itemList.get(i).getQuantity()));
            textViewShowPrice.setText(String.format("%.2f", itemList.get(i).getPrice()));
            textViewShowSubTotal.setText(String.format("%.2f", itemList.get(i).getPrice() * itemList.get(i).getQuantity()));

            return view;
        }
    }


    class Item {
        private String id;
        private String name;
        private int quantity;
        private double price;

        public int getStock() {
            return stock;
        }

        private int stock;

        private Item(String id, String name, int quantity, double price, int stock) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.stock = stock;
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
            quantity = quantity + qty;
        }

        public void minusQuantity(int qty) {
            quantity = quantity - qty;
        }
    }

}