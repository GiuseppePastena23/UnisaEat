package com.novab.unisaeat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.novab.unisaeat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private HashMap<String, Float> products;
    private List<String> productNames;

    public ProductSpinnerAdapter(Context context, HashMap<String, Float> products) {
        super(context, R.layout.spinner_item, new ArrayList<>(products.keySet()));
        this.context = context;
        this.products = products;
        this.productNames = new ArrayList<>(products.keySet());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.spinnerItemText);
        String productName = productNames.get(position);
        Float price = products.get(productName);
        textView.setText(productName + " - €" + String.format("%.2f", price));
        return convertView;
    }
}

