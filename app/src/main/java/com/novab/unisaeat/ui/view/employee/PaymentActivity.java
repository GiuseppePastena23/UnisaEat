package com.novab.unisaeat.ui.view.employee;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.adapter.ProductAdapter;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    private static final float COMPLETE_MEAL_PRICE = 4.5f;
    private static final float PIZZA_MEAL_PRICE = 4.5f;
    private static final float SALAD_MEAL_PRICE = 2.5f;
    private static final float REDUCED_MEAL_A_PRICE = 3.0f;
    private static final float REDUCED_MEAL_B_PRICE = 2.5f;
    private static final float BASKET_MEAL_PRICE = 3.0f;
    private UserViewModel userViewModel;
    private TextView userNameText, userSurnameText, userCfText, userEmailText, userCreditText, amountText;
    private ListView productsList;
    private Button makePaymentButton, cancelPaymentButton;
    private ArrayList<Product> products;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_employee);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        int userId = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("user_id")));
        userViewModel.getUserById(userId);

        associateUI();
        setButtonFunctions(userId);

        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            finish();
        });

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user == null || !user.getCf().equals(getIntent().getStringExtra("cf")) || !user.getToken().equals(getIntent().getStringExtra("token"))) {
                Toast.makeText(this, "Invalid user or token", Toast.LENGTH_SHORT).show();
                finish();
            }
            setUserData(user);
        });
    }

    private void associateUI() {
        userNameText = findViewById(R.id.user_name_text);
        userSurnameText = findViewById(R.id.user_surname_text);
        userCfText = findViewById(R.id.user_cf_text);
        userEmailText = findViewById(R.id.user_email_text);
        userCreditText = findViewById(R.id.user_credit_text);
        productsList = findViewById(R.id.product_list);
        amountText = findViewById(R.id.amount_text);

        makePaymentButton = findViewById(R.id.make_payment_btn);
        cancelPaymentButton = findViewById(R.id.cancel_payment_btn);


        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, R.layout.product_row, R.id.product_name, products);
        productsList.setAdapter(productAdapter);
    }

    private void setButtonFunctions(int userId) {
        makePaymentButton.setOnClickListener(v -> handlePayment(userId));

        cancelPaymentButton.setOnClickListener(v -> finish());

        setMealButtonOnClick(R.id.complete_meal_btn, COMPLETE_MEAL_PRICE);
        setMealButtonOnClick(R.id.pizza_meal_btn, PIZZA_MEAL_PRICE);
        setMealButtonOnClick(R.id.salad_meal_btn, SALAD_MEAL_PRICE);
        setMealButtonOnClick(R.id.reduced_meal_a_btn, REDUCED_MEAL_A_PRICE);
        setMealButtonOnClick(R.id.reduced_meal_b_btn, REDUCED_MEAL_B_PRICE);
        setMealButtonOnClick(R.id.basket_meal_btn, BASKET_MEAL_PRICE);
    }

    private void setMealButtonOnClick(int buttonId, float price) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> addProduct(button.getText().toString(), price));
    }

    private void handlePayment(int userId) {
        String amountString = amountText.getText().toString().substring(1); // Remove the "€" symbol
        if (isEmpty(amountString)) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        amountString = amountString.replace(',', '.');
        float amount = (Float.parseFloat(amountString)) * -1;
        if (amount == 0) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.doTransaction(userId, amount, "payment");

        transactionViewModel.getTransactionOutcome().observe(this, outcome -> {
            Toast.makeText(this, outcome, Toast.LENGTH_SHORT).show();
            finish();
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void addProduct(String name, float price) {
        products.add(new Product(name, price));
        updateAmount();
        Log.d("PaymentActivity", products.toString());
        runOnUiThread(() -> productAdapter.notifyDataSetChanged());

    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            updateAmount();
            runOnUiThread(() -> productAdapter.notifyDataSetChanged());
        }
    }

    private void updateAmount() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        amountText.setText(String.format("€%.2f", total));
    }

    private void setUserData(User user) {
        userNameText.setText(user.getName());
        userSurnameText.setText(user.getSurname());
        userCfText.setText(user.getCf());
        userEmailText.setText(user.getEmail());
        userCreditText.setText(String.valueOf(user.getCredit()));
    }
}
