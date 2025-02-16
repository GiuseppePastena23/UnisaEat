package com.novab.unisaeat.ui.view.user;

import static com.novab.unisaeat.ui.util.Utilities.showAlertDialog;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.util.Utilities;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText cfEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private Spinner statusSpinner;
    private CheckBox showPasswordCheckBox;
    private ProgressBar registerProgressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        associateUI();

    }

    private void associateUI() {
        registerButton = findViewById(R.id.register_btn);
        nameEditText = findViewById(R.id.name_edit_text);
        surnameEditText = findViewById(R.id.surname_edit_text);
        cfEditText = findViewById(R.id.cf_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);

        statusSpinner = findViewById(R.id.status_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.status_array));
        statusSpinner.setAdapter(arrayAdapter);

        showPasswordCheckBox = findViewById(R.id.show_password_checkbox);
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });


        registerProgressBar = findViewById(R.id.register_progress);
        registerButton.setOnClickListener(v -> {
            // Register user
            User elegibleUser = runForm();
            if (elegibleUser == null) {
                return;
            }

            userViewModel.register(elegibleUser);

            userViewModel.getRegistrationOutcomeLiveData().observe(this, outcome -> {
                if (outcome != null) {
                    showAlertDialog(this,
                            getString(R.string.registration_successful_title),
                            getString(R.string.registration_successful_message),
                            (dialog, which) -> finish(),
                            false);

                }
            });

            userViewModel.getErrorLiveData().observe(this, error -> {
                if (error != null) {
                    showAlertDialog(this,
                            getString(R.string.registration_failed_title),
                            getString(R.string.registration_failed_message));
                }
            });

            userViewModel.getIsLoadingLiveData().observe(this, isLoading -> {
                if (isLoading != null) {
                    if (isLoading) {
                        registerProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        registerProgressBar.setVisibility(View.GONE);
                    }
                }
            });

        });
    }


    private User runForm() {
        User u = new User();

        String nameString = nameEditText.getText().toString().trim();
        if (nameString.isEmpty()) {
            showAlertDialog(this,
                    getString(R.string.empty_name_title),
                    getString(R.string.empty_name_message));
            return null;
        }
        u.setName(nameString);

        String surnameString = surnameEditText.getText().toString().trim();
        if (surnameString.isEmpty()) {
            showAlertDialog(this,
                    getString(R.string.empty_surname_title),
                    getString(R.string.empty_surname_message));
            return null;
        }
        u.setSurname(surnameString);


        String cfString = cfEditText.getText().toString().trim();
        if (cfString.length() != 16) {
            showAlertDialog(this,
                    getString(R.string.empty_cf_title),
                    getString(R.string.empty_cf_message));
            return null;
        }
        u.setCf(cfString);


        String emailString = emailEditText.getText().toString().trim();
        if (!emailString.contains("@")) {
            showAlertDialog(this,
                    getString(R.string.empty_email_title),
                    getString(R.string.empty_email_message));
            return null;
        }
        u.setEmail(emailString);


        String passwordString = passwordEditText.getText().toString().trim();
        if (passwordString.length() < 8) {
            showAlertDialog(this,
                    getString(R.string.empty_password_title),
                    getString(R.string.empty_password_message));
            return null;
        }
        u.setPassword(Utilities.hash(passwordString));


        String phoneString = phoneEditText.getText().toString().trim();
        if (phoneString.isEmpty()) {
            showAlertDialog(this,
                    getString(R.string.empty_phone_title),
                    getString(R.string.empty_phone_message));
            return null;
        }
        u.setPhone(phoneString);


        int statusString = statusSpinner.getSelectedItemPosition();
        switch (statusString) {
            case 0:
                u.setStatus("student");
                break;
            case 1:
                u.setStatus("professor");
                break;
        }

        return u;
    }

}
