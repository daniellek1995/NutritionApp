package com.example.nutritionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nutritionapp.model.InformationDataItem;

import java.util.List;

public class InformationListFragment extends Fragment {

    private List<InformationDataItem> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_list, container, false);
        return view;

    }
}