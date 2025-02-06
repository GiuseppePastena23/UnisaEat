package com.novab.unisaeat.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.view.employee.PaymentActivity;
import com.novab.unisaeat.ui.view.employee.Product;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final ArrayList<Product> products;

    public ProductAdapter(Context context, int resource, int textViewResourceId, ArrayList<Product> products) {
        super(context, resource, textViewResourceId, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("ProductAdapter", "getView: " + position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        }

        Product product = products.get(position);

        TextView productTextView = convertView.findViewById(R.id.product_name);
        productTextView.setText(product.getName());

        Button removeButton = convertView.findViewById(R.id.remove_product_btn);
        removeButton.setOnClickListener(v -> {
            if (context instanceof PaymentActivity) {
                ((PaymentActivity) context).removeProduct(product);
            }
        });

        return convertView;
    }


}
