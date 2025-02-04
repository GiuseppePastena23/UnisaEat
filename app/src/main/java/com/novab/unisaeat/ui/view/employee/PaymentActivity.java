package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    private TextView userNameText;
    private TextView userSurnameText;
    private TextView userCfText;
    private TextView userEmailText;
    private TextView userCreditText;

    private Button makePaymentButton;
    private Button cancelPaymentButton;


    private Button completeMealButton;
    private Button pizzaMealButton;
    private Button saladMealButton;
    private Button reducedMealButtonA;
    private Button reducedMealButtonB;
    private Button basketMealButton;

    private UserViewModel userViewModel;

    private void associateUI() {
        userNameText = findViewById(R.id.user_name_text);
        userSurnameText = findViewById(R.id.user_surname_text);
        userCfText = findViewById(R.id.user_cf_text);
        userEmailText = findViewById(R.id.user_email_text);
        userCreditText = findViewById(R.id.user_credit_text);
        makePaymentButton = findViewById(R.id.make_payment_btn);
        cancelPaymentButton = findViewById(R.id.cancel_payment_btn);
        completeMealButton = findViewById(R.id.complete_meal_btn);
        pizzaMealButton = findViewById(R.id.pizza_meal_btn);
        saladMealButton = findViewById(R.id.salad_meal_btn);
        reducedMealButtonA = findViewById(R.id.reduced_meal_a_btn);
        reducedMealButtonB = findViewById(R.id.reduced_meal_b_btn);
        basketMealButton = findViewById(R.id.basket_meal_btn);
    }

    private void setUserData(User user) {
        userNameText.setText(user.getName());
        userSurnameText.setText(user.getSurname());
        userCfText.setText(user.getCf());
        userEmailText.setText(user.getEmail());
        userCreditText.setText(String.valueOf(user.getCredit()));
    }

    private void setButtonFunctions() {
        makePaymentButton.setOnClickListener(v -> {
            Toast.makeText(this, "Payment", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_employee);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        int userId = Integer.valueOf(Objects.requireNonNull(getIntent().getStringExtra("user_id")));
        userViewModel.getUserById(userId);

        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });


        associateUI();
        setButtonFunctions();
        userViewModel.getUserLiveData().observe(this, user -> {
            user = userViewModel.getUserLiveData().getValue();
            if (user == null) {
                Log.d("PaymentActivity", "User is null");
                finish();
            }
            if (user.getCf().equals(getIntent().getStringExtra("cf"))) {
                if (user.getToken().equals(getIntent().getStringExtra("token"))) {
                    Log.d("PaymentActivity", "CF and token match");
                } else {
                    Log.d("PaymentActivity", "Token does not match");
                    finish();
                }
                setUserData(user);
            } else {
                Log.d("PaymentActivity", "CF does not match");
                finish();
            }
        });
    }


}
