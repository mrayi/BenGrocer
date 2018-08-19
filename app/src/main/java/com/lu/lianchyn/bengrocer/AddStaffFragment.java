package com.lu.lianchyn.bengrocer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddStaffFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStaffFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirm;
    private EditText etAddress;
    private EditText etIc;
    private EditText etName;
    private EditText etSalary;

    public AddStaffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStaffFragment newInstance(String param1, String param2) {
        AddStaffFragment fragment = new AddStaffFragment();
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
        final View v =  inflater.inflate(R.layout.fragment_add_staff, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setHasOptionsMenu(true);

        // position spinner
        Spinner position = (Spinner) v.findViewById(R.id.position);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String> (getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.positions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(myAdapter);

        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        // Toast.makeText(getActivity(), bundle.get("staff_count").toString(), Toast.LENGTH_LONG).show();

        etEmail = v.findViewById(R.id.email);
        etPassword = v.findViewById(R.id.password);
        etConfirm = v.findViewById(R.id.confirm);
        etAddress = v.findViewById(R.id.address);
        etIc = v.findViewById(R.id.ic);
        etName = v.findViewById(R.id.name);
        etSalary = v.findViewById(R.id.salary);

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
            case R.id.create:
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPassword.getText());
                String confirm = String.valueOf(etConfirm.getText());
                String address = String.valueOf(etAddress.getText());
                String ic = String.valueOf(etIc.getText());
                String name = String.valueOf(etName.getText());
                String salary = String.valueOf(etSalary.getText());
                if(
                        email == null || email.isEmpty() ||
                        password == null || password.isEmpty() ||
                        confirm == null || confirm.isEmpty() ||
                        address == null || address.isEmpty() ||
                        ic == null || ic.isEmpty() ||
                        name == null || name.isEmpty() ||
                        salary == null || salary.isEmpty()
                ) {
                    Toast.makeText(getActivity(), "All fields are required.", Toast.LENGTH_LONG).show();
                } else if(!password.equals(confirm)) {
                    Toast.makeText(getActivity(), "Your password and confirm password are different.", Toast.LENGTH_LONG).show();
                } else {
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
                    mTabHost.setCurrentTab(1);
                    mTabHost.setCurrentTab(0);
                    Toast.makeText(getActivity().getBaseContext(), "New staff created successfully.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
