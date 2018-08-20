package com.lu.lianchyn.bengrocer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class EditMemberFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EditMemberFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore db;
    private EditText etEmail;
    private EditText etFname;
    private EditText etHpno;
    private EditText etIcno;
    private EditText etLname;
    private EditText etCard;
    private EditText etPoint;
    private EditText etStatus;
    private String mid;
    private String email;
    private String fname;
    private String hpno;
    private String icno;
    private String lname;
    private String card;
    private String point;
    private String status;

    public EditMemberFragment() {
        // Required empty public constructor
    }

    public static AddMemberFragment newInstance(String param1, String param2) {
        AddMemberFragment fragment = new AddMemberFragment();
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
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_edit_member, container, false);

        db = FirebaseFirestore.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setHasOptionsMenu(true);

        Button submit = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        Button reset = (Button) v.findViewById(R.id.reset);
        reset.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        mid = bundle.get("mid").toString();
        email = bundle.get("email").toString();
        fname = bundle.get("fname").toString();
        hpno = bundle.get("hpno").toString();
        icno = bundle.get("icno").toString();
        lname = bundle.get("lname").toString();
        card = bundle.get("card").toString();
        point = bundle.get("point").toString();
        status = bundle.get("status").toString();

        etEmail = v.findViewById(R.id.email);
        etFname = v.findViewById(R.id.fname);
        etHpno = v.findViewById(R.id.hpno);
        etIcno = v.findViewById(R.id.icno);
        etLname = v.findViewById(R.id.lname);
        etCard = v.findViewById(R.id.card);
        etPoint = v.findViewById(R.id.point);
        etStatus = v.findViewById(R.id.status);

        etEmail.setText(email);
        etFname.setText(fname);
        etHpno.setText(hpno);
        etIcno.setText(icno);
        etLname.setText(lname);
        etCard.setText(card);
        etPoint.setText(point);
        etStatus.setText(status);

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

    @Override
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.submit:
                final String email = String.valueOf(etEmail.getText());
                final String fname = String.valueOf(etFname.getText());
                final String hpno = String.valueOf(etHpno.getText());
                final String icno = String.valueOf(etIcno.getText());
                final String lname = String.valueOf(etLname.getText());
                final String card = String.valueOf(etCard.getText());
                final String point = String.valueOf(etPoint.getText());
                final String status = String.valueOf(etStatus.getText());
                if(
                        email == null || email.isEmpty() ||
                                fname == null || fname.isEmpty() ||
                                hpno == null || hpno.isEmpty() ||
                                icno == null || icno.isEmpty() ||
                                lname == null || lname.isEmpty() ||
                                card == null || card.isEmpty() ||
                                point == null || point.isEmpty() ||
                                status == null || status.isEmpty()
                        ) {
                    Toast.makeText(getActivity(), "All fields are required.", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> newMember = new HashMap<>();
                    newMember.put("EMAIL", email);
                    newMember.put("F_NAME", fname);
                    newMember.put("HPNO", hpno);
                    newMember.put("ICNO", icno);
                    newMember.put("L_NAME", lname);
                    newMember.put("M_Card_No", card);
                    newMember.put("POINTS", Long.parseLong(point));
                    newMember.put("STATUS", status);

                    db.collection("Member").document(mid)
                            .set(newMember)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                    android.support.v4.app.FragmentManager fm  = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                                    fm.popBackStack();

                                    FragmentTabHost mTabHost;
                                    mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
                                    mTabHost.setup(getActivity(), ((AppCompatActivity)getActivity()).getSupportFragmentManager(), R.id.realtabcontent);

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
                                    Toast.makeText(getActivity().getBaseContext(), "Member edited successfully.", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
                break;
            case R.id.reset:
                etEmail.setText(this.email);
                etFname.setText(this.fname);
                etHpno.setText(this.hpno);
                etIcno.setText(this.icno);
                etLname.setText(this.lname);
                etCard.setText(this.card);
                etPoint.setText(this.point);
                etStatus.setText(this.status);
                break;
        }
    }

}
