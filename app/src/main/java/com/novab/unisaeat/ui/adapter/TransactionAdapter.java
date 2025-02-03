package com.novab.unisaeat.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;

import java.text.SimpleDateFormat;
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
            convertView = inflater.inflate(R.layout.transaction_row, parent, false);
        }

        Transaction transaction = transactions.get(position);
        String transactionModeString = transaction.getMode();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        // Find views once
        TextView transactionId = convertView.findViewById(R.id.transaction_id);
        TextView transactionDate = convertView.findViewById(R.id.transaction_date);
        TextView transactionProducts = convertView.findViewById(R.id.transaction_products);
        TextView transactionAmount = convertView.findViewById(R.id.transaction_amount);
        TextView transactionMode = convertView.findViewById(R.id.transaction_mode);

        // Prepare common variables
        String idLabel = String.format("ID: %s", transaction.getId());
        String formattedDate = sdf.format(transaction.getDatetime());
        String amountLabel = String.format("Amount: %s", Float.parseFloat(transaction.getAmount()));
        String productsLabel = "";
        String modeLabel = "";

        // Check transaction mode and handle cases
        if (transactionModeString.startsWith("order;")) {
            String[] parts = transactionModeString.split(";");
            productsLabel = context.getString(R.string.products_text) + ": " + parts[1];
            modeLabel = context.getString(R.string.order);
            amountLabel = String.format("Amount: %s", Float.parseFloat(transaction.getAmount()) * -1);
        } else if (transactionModeString.equals("topup;cash") || transactionModeString.equals("topup;online")) {
            modeLabel = transactionModeString.equals("topup;cash") ? "Topup Cash" : "Topup Online";
        } else if (transactionModeString.startsWith("topup;")) {
            String topupType = transactionModeString.split(";")[1];
            modeLabel = topupType.equals("cash") ? "Topup cash" : "Topup online";
        } else if (transactionModeString.equals("payment")) {
            modeLabel = "Pagamento Mensa";
        } else {
            modeLabel = transactionModeString;
        }

        // Update the amount for transactions involving topups
        if (!transactionModeString.startsWith("order;")) {
            amountLabel = String.format("Amount: %s", Float.parseFloat(transaction.getAmount()));
        }

        // Set values to the views
        transactionId.setText(idLabel);
        transactionDate.setText(formattedDate);
        transactionProducts.setText(productsLabel);
        transactionMode.setText(modeLabel);
        transactionAmount.setText(amountLabel);

        return convertView;
    }

}
