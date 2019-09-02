package com.example.abhinav.myrecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class Tab2Fragment extends Fragment implements AdapterView.OnItemClickListener {
    public ListView lv;
    private ListAdapter adapter;
    ArrayList<String> listItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        super.onCreate(savedInstanceState);

        String value = getArguments().getString("name");
        lv = view.findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, addItems(value));
        lv.setAdapter(adapter);
        return view;
    }

    public ArrayList<String> addItems(String v) {
        listItems.add(v);
        return listItems;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}