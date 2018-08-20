package com.lu.lianchyn.bengrocer;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MaterialSearchView searchView;
    private String[] lstSource;
    private ListView lstView;
    private FirebaseFirestore db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private FragmentTabHost mTabHost;
    private int staff_count = 0;
    private String[][] fullDat;
    private String[] selectedDat = null;

    public StaffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffFragment newInstance(String param1, String param2) {
        StaffFragment fragment = new StaffFragment();
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
        final View v =  inflater.inflate(R.layout.fragment_staff, container, false);

        db = FirebaseFirestore.getInstance();
        loadList();

        // Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        // ((AppCompatActivity)getActivity()).setTitle("Material Search");
        // toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) v.findViewById(R.id.search_view);
        searchView.setVisibility(View.INVISIBLE);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setVisibility(View.VISIBLE);
                TypedValue tv = new TypedValue();
                int actionBarHeight = 0;
                if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
                {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }
                ListView.MarginLayoutParams p = (ListView.MarginLayoutParams) v.findViewById(R.id.lstView).getLayoutParams();
                p.setMargins(0, actionBarHeight, 0, 0);
                v.requestLayout();
            }

            @Override
            public void onSearchViewClosed() {
                searchView.setVisibility(View.INVISIBLE);
                ListView.MarginLayoutParams p = (ListView.MarginLayoutParams) v.findViewById(R.id.lstView).getLayoutParams();
                p.setMargins(0, 0, 0, 70);
                v.requestLayout();
                lstView = (ListView) v.findViewById(R.id.lstView);
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstSource);
                lstView.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()) {
                    List<String> lstFound = new ArrayList<String>();
                    for(String item:lstSource) {
                        if(item.toLowerCase().contains(newText.toLowerCase()))
                            lstFound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstFound);
                    lstView.setAdapter(adapter);
                } else {
                    // if search text is null
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstSource);
                    lstView.setAdapter(adapter);
                }
                return true;
           }
        });

        ImageButton addStaff = (ImageButton) v.findViewById(R.id.addStaff);
        addStaff.setOnClickListener(this);
        ImageButton viewStaff = (ImageButton) v.findViewById(R.id.viewStaff);
        viewStaff.setOnClickListener(this);
        ImageButton editStaff = (ImageButton) v.findViewById(R.id.editStaff);
        editStaff.setOnClickListener(this);

        dl = (DrawerLayout)getActivity().findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(getActivity(),dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }

    @Override
    public void onClick(View v){
        FragmentManager fm  = getFragmentManager();
        switch(v.getId()) {
            case R.id.addStaff:
                Bundle bundle = new Bundle();
                bundle.putString("staff_count", Integer.toString(staff_count));

                mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
                mTabHost.setup(getActivity(), ((AppCompatActivity)getActivity()).getSupportFragmentManager(), R.id.realtabcontent);

                mTabHost.clearAllTabs();

                fm  = getFragmentManager();
                // StaffFragment thisFrag = new StaffFragment();
                AddStaffFragment nextFrag = new AddStaffFragment();
                nextFrag.setArguments(bundle);
                /*
                fm.beginTransaction()
                        .add(R.id.realtabcontent, thisFrag)
                        .commit();
                */
                fm.beginTransaction()
                        .replace(R.id.realtabcontent, nextFrag, "AddStaff")
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.viewStaff:
                if(selectedDat != null) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("address", selectedDat[1]);
                    bundle2.putString("email", selectedDat[2]);
                    bundle2.putString("ic", selectedDat[3]);
                    bundle2.putString("name", selectedDat[4]);
                    bundle2.putString("position", selectedDat[5]);
                    bundle2.putString("salary", selectedDat[6]);
                    bundle2.putString("sid", selectedDat[7]);

                    mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
                    mTabHost.setup(getActivity(), ((AppCompatActivity)getActivity()).getSupportFragmentManager(), R.id.realtabcontent);

                    mTabHost.clearAllTabs();

                    fm  = getFragmentManager();
                    ViewStaffFragment nextFrag2= new ViewStaffFragment();
                    nextFrag2.setArguments(bundle2);

                    fm.beginTransaction()
                            .replace(R.id.realtabcontent, nextFrag2, "ViewStaff")
                            .addToBackStack(null)
                            .commit();
                }

                break;
            case R.id.editStaff:
                if(selectedDat != null) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("uid", selectedDat[0]);
                    bundle3.putString("address", selectedDat[1]);
                    bundle3.putString("email", selectedDat[2]);
                    bundle3.putString("ic", selectedDat[3]);
                    bundle3.putString("name", selectedDat[4]);
                    bundle3.putString("position", selectedDat[5]);
                    bundle3.putString("salary", selectedDat[6]);
                    bundle3.putString("sid", selectedDat[7]);

                    mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
                    mTabHost.setup(getActivity(), ((AppCompatActivity)getActivity()).getSupportFragmentManager(), R.id.realtabcontent);

                    mTabHost.clearAllTabs();

                    fm  = getFragmentManager();
                    EditStaffFragment nextFrag3= new EditStaffFragment();
                    nextFrag3.setArguments(bundle3);

                    fm.beginTransaction()
                            .replace(R.id.realtabcontent, nextFrag3, "EditStaff")
                            .addToBackStack(null)
                            .commit();
                }

                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadList() {
        FirebaseFirestore.getInstance()
                .collection("Staff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            lstSource = new String[myListOfDocuments.size()];
                            fullDat = new String[myListOfDocuments.size()][8];
                            for(int i = 0; i < myListOfDocuments.size(); i++) {
                                lstSource[i] = (String) myListOfDocuments.get(i).get("sid") + " " + (String) myListOfDocuments.get(i).get("Name");
                                fullDat[i][0] = (String) myListOfDocuments.get(i).getId();
                                fullDat[i][1] = (String) myListOfDocuments.get(i).get("Address");
                                fullDat[i][2] = (String) myListOfDocuments.get(i).get("Email");
                                fullDat[i][3] = (String) myListOfDocuments.get(i).get("IC_NO");
                                fullDat[i][4] = (String) myListOfDocuments.get(i).get("Name");
                                fullDat[i][5] = (String) myListOfDocuments.get(i).get("Position");
                                try {
                                    fullDat[i][6] = Double.toString((Double) myListOfDocuments.get(i).get("Salary"));
                                } catch(ClassCastException e) {
                                    fullDat[i][6] = Long.toString((Long) myListOfDocuments.get(i).get("Salary"));
                                }
                                fullDat[i][7] = (String) myListOfDocuments.get(i).get("sid");
                            }
                            // Toast.makeText(getActivity(), Integer.toString(myListOfDocuments.size()), Toast.LENGTH_LONG).show();
                            staff_count = myListOfDocuments.size();
                        }

                        setHasOptionsMenu(true);
                        lstView = (ListView) getActivity().findViewById(R.id.lstView);
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstSource);
                        lstView.setAdapter(adapter);
                        adapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return lhs.compareTo(rhs);   //or whatever your sorting algorithm
                            }
                        });
                        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                            {
                                for(int a = 0; a < arg0.getChildCount(); a++)
                                {
                                    arg0.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                                }

                                arg1.setBackgroundColor(Color.GREEN);
                                // Toast.makeText(getActivity(), lstView.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                                for(int i = 0; i < fullDat.length; i++) {
                                    if(fullDat[i][7].equals(lstView.getItemAtPosition(position).toString().substring(0,8))) {
                                        selectedDat = Arrays.copyOf(fullDat[i], fullDat[i].length);
                                        // Toast.makeText(getActivity(), selectedDat[6], Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });
    }

}
