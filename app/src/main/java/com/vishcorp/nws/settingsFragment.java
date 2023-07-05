package com.vishcorp.nws;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vishcorp.nws.databinding.FragmentSettingsBinding;


public class settingsFragment extends Fragment {
    Context context;

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ListView listView = (ListView) root.findViewById(R.id.settings_list);
        final String[] text = {"About the application", "Privacy policy"};

//      final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, text);
      ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.layout.listview_text_color, text);
        listView.setAdapter(adapter);
        listView.setSelector(R.color.green);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:

                        Intent intent = new Intent(getActivity().getApplication(), about_application.class);
                        startActivity(intent);
                        break;
                    case 1:

                        Intent intent1 = new Intent(getActivity().getApplication(), privacypolicy.class);
                        startActivity(intent1);
                        break;


                }
            }
        });




        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}