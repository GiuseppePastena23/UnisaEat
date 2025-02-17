package com.novab.unisaeat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends BaseAdapter {

    private final Context context;
    private final List<Transaction> transactions;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault());

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions != null ? transactions.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.transaction_row, parent, false);

            holder = new ViewHolder();
            holder.transactionId = convertView.findViewById(R.id.transaction_id);
            holder.transactionDate = convertView.findViewById(R.id.transaction_date);
            holder.transactionProducts = convertView.findViewById(R.id.transaction_products);
            holder.transactionAmount = convertView.findViewById(R.id.transaction_amount);
            holder.transactionMode = convertView.findViewById(R.id.transaction_mode);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Transaction transaction = transactions.get(position);
        String transactionModeString = transaction.getMode();
        String[] modeParts = transactionModeString.split(";");

        // ID e data
        holder.transactionId.setText(String.format("ID: %s", transaction.getId()));
        holder.transactionDate.setText(sdf.format(transaction.getDatetime()));

        // Modalità transazione e importo
        String modeLabel;
        float amount = Float.parseFloat(transaction.getAmount());
        String productsLabel = ""; // Default vuoto, verrà valorizzato solo per ordini

        switch (modeParts[0]) {
            case "order":
                modeLabel = context.getString(R.string.order);
                amount *= -1; // Gli ordini scalano il credito

                if (modeParts.length > 1) {
                    String prodottoEngDb = modeParts[1];
                    int resIdProdotto;
                    switch (prodottoEngDb) {
                        case "Salad":
                            resIdProdotto = R.string.salad;
                            break;
                        case "Basket":
                            resIdProdotto = R.string.basket;
                            break;
                        case "Sandwich":
                            resIdProdotto = R.string.sandwich;
                            break;
                        default:
                            resIdProdotto = R.string.unknown;
                    }
                    productsLabel = context.getString(R.string.products_text) + ": " + context.getString(resIdProdotto);
                }
                break;

            case "topup":
                modeLabel = modeParts.length > 1 && "cash".equals(modeParts[1])
                        ? context.getString(R.string.topup_cash)
                        : context.getString(R.string.topup_online);
                break;

            case "payment":
                modeLabel = context.getString(R.string.payment);
                break;

            default:
                modeLabel = transactionModeString;
                break;
        }

        // Imposta le TextView
        holder.transactionMode.setText(modeLabel);
        holder.transactionAmount.setText(String.format("%s: %.2f", context.getString(R.string.amount_text), amount));
        holder.transactionProducts.setText(productsLabel); // Sarà vuoto per le ricariche e i pagamenti

        return convertView;
    }

    static class ViewHolder {
        TextView transactionId;
        TextView transactionDate;
        TextView transactionProducts;
        TextView transactionAmount;
        TextView transactionMode;
    }
}
