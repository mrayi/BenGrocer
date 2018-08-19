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
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddStaffFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewStaffFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AddStaffFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String address;
    private String email;
    private String ic;
    private String name;
    private String position;
    private String salary;
    private String sid;
    private TextView tvAddress;
    private TextView tvEmail;
    private TextView tvIc;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvSalary;
    private TextView tvSid;

    public ViewStaffFragment() {
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
        final View v =  inflater.inflate(R.layout.fragment_view_staff, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        // Toast.makeText(getActivity(), bundle.get("staff_count").toString(), Toast.LENGTH_LONG).show();
        address = bundle.get("address").toString();
        email = bundle.get("email").toString();
        ic = bundle.get("ic").toString();
        name = bundle.get("name").toString();
        position = bundle.get("position").toString();
        salary = bundle.get("salary").toString();
        sid = bundle.get("sid").toString();

        tvAddress = v.findViewById(R.id.address);
        tvEmail = v.findViewById(R.id.email);
        tvIc = v.findViewById(R.id.ic);
        tvName = v.findViewById(R.id.name);
        tvPosition = v.findViewById(R.id.position);
        tvSalary = v.findViewById(R.id.salary);
        tvSid = v.findViewById(R.id.sid);

        tvAddress.setText("Address: " + address);
        tvEmail.setText("Email: " + email);
        tvIc.setText("  IC_NO: " + ic);
        tvName.setText("Name: " + name);
        tvPosition.setText("Position: " + position);
        tvSalary.setText("Salary: RM" + String.format("%.2f", Double.parseDouble(salary)));
        // tvSalary.setText("Salary: RM" + salary);
        tvSid.setText("Sid: " + sid);

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

    }

}
