package com.lu.lianchyn.bengrocer;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffFragment extends Fragment {
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
        FirebaseFirestore.getInstance()
                .collection("Staff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            lstSource = new String[myListOfDocuments.size()];
                            for(int i = 0; i < myListOfDocuments.size(); i++) {
                                lstSource[i] = (String) myListOfDocuments.get(i).get("sid") + " " + (String) myListOfDocuments.get(i).get("Name");
                            }
                        }

                        setHasOptionsMenu(true);
                        lstView = (ListView) v.findViewById(R.id.lstView);
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lstSource);
                        lstView.setAdapter(adapter);

                        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                            {
                                for(int a = 0; a < arg0.getChildCount(); a++)
                                {
                                    arg0.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                                }

                                arg1.setBackgroundColor(Color.GREEN);
                            }
                        });
                    }
                });

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
                p.setMargins(0, 0, 0, 0);
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

}
