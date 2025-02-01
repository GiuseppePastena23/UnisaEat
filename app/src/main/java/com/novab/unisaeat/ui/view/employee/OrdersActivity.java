package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.adapter.TransactionAdapter;
import com.novab.unisaeat.ui.viewmodel.LoginViewModel;
import com.novab.unisaeat.ui.viewmodel.OrdersViewModel;

import java.util.Collections;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private TextView ordersText;
    private ListView ordersListView;
    private OrdersViewModel ordersViewModel = new OrdersViewModel();

    private void associateUI() {
        ordersText = findViewById(R.id.orders_text);
        ordersListView = findViewById(R.id.orders_list_view);
    }


    private void setAdapter(List<Transaction> transactions) {
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        ordersListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }



        ordersViewModel.getOrdersLiveData().observe(this, orders -> {
            if (orders != null && !orders.isEmpty()){

                setAdapter(orders);
                Log.d("OrdersActivity", "Orders: " + orders);
            }
            else {
                ordersText.setText(R.string.no_order_present_text);
            }
        });

        ordersViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                ordersText.setText("Error: " + errorMessage);
                Log.e("OrdersActivity", "Error: " + errorMessage);
            }
        });

        ordersViewModel.getOrders();


        associateUI();




    }
}
