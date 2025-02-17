package com.novab.unisaeat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Transaction> transactions;

    public OrderAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions != null ? transactions.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.order_row, parent, false);
        }

        Transaction transaction = transactions.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String formattedDate = sdf.format(transaction.getDatetime());

        TextView transactionId = convertView.findViewById(R.id.transaction_id);
        TextView transactionDate = convertView.findViewById(R.id.transaction_date);
        TextView transactionProducts = convertView.findViewById(R.id.transaction_products);

        String idLabel = String.format(("ID: %s"), transaction.getId());
        String productDbName = transaction.getMode().replace("order;", "");

        String outputProduct;
        switch (productDbName) {
            case "Salad":
                outputProduct = context.getString(R.string.salad);
                break;
            case "Basket":
                outputProduct = context.getString(R.string.basket);
                break;
            case "Sandwich":
                outputProduct = context.getString(R.string.sandwich);
                break;
            default:
                outputProduct = context.getString(R.string.error);
                break;
        }


        String productsLabel = context.getString(R.string.products_text) + String.format(": %s",
                outputProduct);


        transactionId.setText(idLabel);
        transactionDate.setText(formattedDate);
        transactionProducts.setText(productsLabel);

        return convertView;
    }
}
