package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.ui.adapter.OrderAdapter;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private TextView ordersText;
    private ListView ordersListView;
    private TransactionViewModel transactionViewModel;

    private void associateUI() {
        ordersText = findViewById(R.id.orders_text);
        ordersListView = findViewById(R.id.orders_list_view);
    }


    private void setAdapter(List<Transaction> transactions) {
        OrderAdapter adapter = new OrderAdapter(this, transactions);
        ordersListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_employee);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        transactionViewModel.getOrders();

        associateUI();
        fillOrders();
    }

    private void fillOrders() {
        transactionViewModel.getOrdersLiveData().observe(this, orders -> {
            if (orders != null && !orders.isEmpty()) {

                setAdapter(orders);
                Log.d("OrdersActivity", "Orders: " + orders);
            } else {
                ordersText.setText(R.string.no_order_present_text);
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                ordersText.setText("Error: " + errorMessage);
                Log.e("OrdersActivity", "Error: " + errorMessage);
            }
        });
    }


}
