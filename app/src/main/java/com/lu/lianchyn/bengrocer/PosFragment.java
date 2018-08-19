package com.lu.lianchyn.bengrocer;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText editTextStaffID;
    private EditText editTextStaffName;
    private EditText editTextMemberID;
    private EditText editTextMemberName;
    private ListView listViewItem;
    private ArrayList<Item> itemList;
    private EditText editTextItemID;
    private EditText editTextQuantity;
    private Button buttonInsertItem;


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
        buttonInsertItem = v.findViewById(R.id.buttonInsertItem);

        //get and show the staff id and name
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                                    } else {
                                        editTextMemberName.setText("Member Not Found");
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
        final ItemAdapter itemAdapter = new ItemAdapter();
        listViewItem.setAdapter(itemAdapter);



        //onclick insert item
        buttonInsertItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(editTextQuantity.getText().toString().matches("")||editTextQuantity.getText().toString().matches("0")||editTextItemID.getText().toString().matches(""))){
                    boolean exist=false;
                    for(int i =0; i<itemList.size();i++) {
                        if (editTextItemID.getText().toString().matches(itemList.get(i).getId())) {
                            itemList.get(i).addQuantity(Integer.parseInt(editTextQuantity.getText().toString()));
                            itemAdapter.notifyDataSetChanged();
                            exist = true;
                        }
                    }
                    if(!exist) {
                        DocumentReference docRe = db.collection("Stock").document(editTextItemID.getText().toString());
                        docRe.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            if (documentSnapshot.getDouble("Qty") != 0) {
                                                String itemID = documentSnapshot.getString("Stock_ID");
                                                String itemName = documentSnapshot.getString("Name");
                                                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                                                double price = documentSnapshot.getDouble("Price");
                                                Item item = new Item(itemID, itemName, quantity, price);
                                                itemList.add(item);
                                                itemAdapter.notifyDataSetChanged();
                                                Toast.makeText(getActivity(), itemID + itemName + quantity, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Item is Out of Stock", Toast.LENGTH_SHORT).show();
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
                    }
                }else{
                    if (editTextItemID.getText().toString().matches(""))
                        Toast.makeText(getActivity(), "Please key in item id", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Item Quantity cannot be empty or 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
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
            view = getLayoutInflater().inflate(R.layout.item_list,null);

            TextView textViewShowItemID = view.findViewById(R.id.textViewShowItemID);
            TextView textViewShowItemName = view.findViewById(R.id.textViewShowName);
            TextView textViewShowQuantity = view.findViewById(R.id.textViewShowQuantity);
            TextView textViewShowPrice = view.findViewById(R.id.textViewShowPrice);
            TextView textViewShowSubTotal = view.findViewById(R.id.textViewShowSubTotal);

            textViewShowItemID.setText(itemList.get(i).getId());
            textViewShowItemName.setText(itemList.get(i).getName());
            textViewShowQuantity.setText(Integer.toString(itemList.get(i).getQuantity()));
            textViewShowPrice.setText(String.format("%.2f",itemList.get(i).getPrice()));
            textViewShowSubTotal.setText(String.format("%.2f",itemList.get(i).getPrice()*itemList.get(i).getQuantity()));

            return view;
        }
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