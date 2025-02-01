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
import com.novab.unisaeat.data.repository.TransactionRepository;

import java.util.List;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
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
            convertView = inflater.inflate(R.layout.row, parent, false);
        }

        Transaction transaction = transactions.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String formattedDate = sdf.format(transaction.getDatetime());

        TextView transactionId = convertView.findViewById(R.id.transaction_id);
        TextView transactionDate = convertView.findViewById(R.id.transaction_date);
        TextView transactionProducts = convertView.findViewById(R.id.transaction_products);

        String idLabel = String.format(("ID: %s"), transaction.getId());
        String productsLabel = context.getString(R.string.products_text) + String.format(": %s", transaction.getMode().replace("order;", ""));


        transactionId.setText(idLabel);
        transactionDate.setText(formattedDate);
        transactionProducts.setText(productsLabel);

        return convertView;
    }
}
