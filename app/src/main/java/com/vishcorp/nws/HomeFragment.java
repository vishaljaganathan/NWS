package com.vishcorp.nws;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vishcorp.nws.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements Itemsearch {

    private RecyclerView rview1, rview2;
    private DatabaseReference myRef, myRef2;
    private ArrayList<Title> titleArrayList;
    private ArrayList<worker> workerArrayList;
    private TitleAdapter titleAdapter;
    private FragmentHomeBinding binding;
    private EmpAdapter empAdapter;
    private Context mcontext;
    private SearchView searchView;

    public HomeFragment() {
        // require a empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //https://gist.github.com/codinginflow/2330259bd0aa53e6adacf9a24920f44f
        searchView = view.findViewById(R.id.searcgView1);
        searchView.clearFocus();
        searchviewfun();

        rview1 = (RecyclerView) view.findViewById(R.id.horiview);
        rview2 = (RecyclerView) view.findViewById(R.id.verical_rview);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rview1.setLayoutManager(linearLayoutManager);
        rview1.setHasFixedSize(true);

        GridLayoutManager layoutManager2 = new GridLayoutManager(this.getActivity(), 2);
        rview2.setLayoutManager(layoutManager2);
        rview2.setHasFixedSize(true);

        //firebase
        titleArrayList = new ArrayList<>();
        workerArrayList = new ArrayList();
        ClearAll();

        titleAdapter = new TitleAdapter(getActivity(), titleArrayList,this);
        empAdapter = new EmpAdapter(getActivity(), workerArrayList);

        myRef = FirebaseDatabase.getInstance().getReference("Title");
        myRef2 = FirebaseDatabase.getInstance().getReference("worker");

        //Title firebase
        GetDataFromFirebase();

        //Employee
        GetEmployeedatabase();

        return view;
    }
    private void GetDataFromFirebase() {

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ClearAll();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Title title1 = datasnapshot.getValue(Title.class);
                    title1.getTurl();
                    titleArrayList.add(title1);
                }
                rview1.setAdapter(titleAdapter);
                titleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void GetEmployeedatabase() {

        myRef2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {

                    String name = datasnapshot.child("name").getValue().toString();
                    String job = datasnapshot.child("jobname").getValue().toString();
                    String image = datasnapshot.child("pimage").getValue().toString();

                    worker worker1 = datasnapshot.getValue(worker.class);
                    workerArrayList.add(worker1);
                }
                rview2.setAdapter(empAdapter);
                empAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mcontext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClearAll() {
        if (titleArrayList != null) {
            titleArrayList.clear();
            if (titleAdapter != null) {
                titleAdapter.notifyDataSetChanged();
            }
        }
        titleArrayList = new ArrayList<>();
    }

    private void searchviewfun() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterList(newText);

                return true;
            }
        });
    }

    private void FilterList(String text) {
        ArrayList<worker> filteredList = new ArrayList<>();
        for (worker worker : workerArrayList) {
            if (worker.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(worker);
            } else if (worker.getJobname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(worker);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            empAdapter.setFilteredList(filteredList);
        }
    }


    @Override
    public void getdata(String search) {
        FilterList(search);

    }
}


