package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import com.novab.unisaeat.R;

public class RitiroActivity extends AppCompatActivity {

    private Spinner productsSpinner;
    private TextView priceText;
    private Button orderButton;
    private TimePicker hourPicker;
    private PrenotazioneViewModel prenotazioneViewModel;

    private void associateUI() {

        // prezzoText = findViewById(R.id.textViewPrezzo);
        orderButton = findViewById(R.id.order_btn);
        hourPicker = findViewById(R.id.timePicker);
        productsSpinner = findViewById(R.id.lista_prodotti);

    }

    private void setSpinner() {
        ArrayList<String> productsList = new ArrayList<>();
        productsList.add(getString(R.string.salad));
        productsList.add(getString(R.string.sandwich));
        productsList.add(getString(R.string.basket));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productsSpinner.setAdapter(adapter);
    }

    private void associateButtons() {
        hourPicker.setIs24HourView(true);

            orderButton.setOnClickListener(v -> {
                // Ottieni l'orario selezionato
                int hourOfDay = hourPicker.getHour();
                int minute = hourPicker.getMinute();

                // Ottieni la data attuale
                Calendar currentCalendar = Calendar.getInstance();
                int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

                // Verifica che la prenotazione sia tra le 9:00 e le 20:00
                if (currentHour >= 20) {
                    showAlertDialog("Errore", "Le prenotazioni non sono più possibili dopo le 20:00.");
                } else if (hourOfDay < 9) {
                    showAlertDialog("Errore", "La prenotazione non è possibile prima delle 9:00.");
                } else if (hourOfDay > 20) {
                    showAlertDialog("Errore", "Le prenotazioni non sono possibili dopo le 20:00.");
                } else {
                    prenotazioneViewModel.controllaCredito(importoPrenotazione);

                }


            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ritiro);
        prenotazioneViewModel = new ViewModelProvider(this).get(PrenotazioneViewModel.class);


        associateUI();
        associateButtons();
        setSpinner();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(title.equals("Prenotazione Confermata")) {

                        }
                    }
                })
                .create()
                .show();
    }
}
