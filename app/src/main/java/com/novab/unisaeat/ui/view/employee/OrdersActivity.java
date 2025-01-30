package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.LoginViewModel;
import com.novab.unisaeat.ui.viewmodel.OrdersViewModel;

public class OrdersActivity extends AppCompatActivity {

    private TextView ordersText;
    private OrdersViewModel ordersViewModel = new OrdersViewModel();

    private void associateUI() {

        ordersText = findViewById(R.id.orders_text);

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
            if (orders != null) {
                ordersText.setText(orders.toString());
                Log.d("OrdersActivity", "Orders: " + orders.toString());
            }
        });

        ordersViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                ordersText.setText("Errore: " + errorMessage);
            }
        });

        ordersViewModel.getOrders();


        associateUI();




    }
}
