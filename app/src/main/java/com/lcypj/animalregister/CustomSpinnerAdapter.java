package com.lcypj.animalregister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater inflater;
    List<String> spinnerItems;


    public CustomSpinnerAdapter(Context context, int resource, List<String> spinnerItems) {
        super(context, resource, spinnerItems);
        this.spinnerItems = spinnerItems;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {




        return super.getView(position, convertView, parent);
    }
}
