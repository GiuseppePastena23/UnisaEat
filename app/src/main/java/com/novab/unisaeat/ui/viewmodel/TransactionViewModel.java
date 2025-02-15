package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;
import com.novab.unisaeat.data.util.DayInfo;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.util.NotificationHelper;
import com.novab.unisaeat.ui.view.user.HomeActivity;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {


    SharedPreferencesManager sharedPreferencesManager =
            new SharedPreferencesManager(getApplication());

    private final TransactionRepository transactionRepository;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<DayInfo> dayLiveData = new MutableLiveData<>();

    public TransactionViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository();
    }

    // ORDERS FOR EMPLOYEE
    private final MutableLiveData<List<Transaction>> ordersLiveData = new MutableLiveData<>();

    public void getOrders() {
        isLoadingLiveData.setValue(true);

        transactionRepository.getOrders(new TransactionRepository.OrdersCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                isLoadingLiveData.setValue(false);
                ordersLiveData.setValue(transactions);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public LiveData<List<Transaction>> getOrdersLiveData() {
        return ordersLiveData;
    }


    // RECHARGE WALLET
    private final MutableLiveData<String> transactionOutcome = new MutableLiveData<>();

    public void doTransaction(int userId, float amount, String mode) {
        isLoadingLiveData.setValue(true);
        transactionRepository.doTransaction(userId, amount, mode, new TransactionRepository.WalletRechargeCallback() {
            @Override
            public void onSuccess(String result) {
                isLoadingLiveData.setValue(false);
                transactionOutcome.setValue(result);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void doTransaction(float amount, String mode) {
        doTransaction(sharedPreferencesManager.getUser().getId(), amount, mode);
    }

    public LiveData<String> getTransactionOutcome() {
        return transactionOutcome;
    }


    // GET USER TRANSACTIONS
    private final MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();

    private void getUserTransactions(int userId) {
        isLoadingLiveData.setValue(true);
        transactionRepository.getUserTransaction(userId, new TransactionRepository.TransactionsCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                isLoadingLiveData.setValue(false);
                transactionsLiveData.setValue(transactions);

                if (sharedPreferencesManager.getLastTransaction() != transactions.get(0).getId()) {
                    // send notification
                    NotificationHelper.showNotification(getApplication(),
                            getApplication().getString(R.string.new_transaction_notification_title),
                            getApplication().getString(R.string.new_transaction_notification_body
                            ) + "\n" + transactions.get(0).getAmount() + "€"
                    );
                }

                sharedPreferencesManager.saveLastTransaction(transactions.get(0).getId());
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });

    }

    public void getDay() {
        transactionRepository.getDay(new TransactionRepository.DayCallback() {

            @Override
            public void onSuccess(DayInfo dayInfo) {
                dayLiveData.setValue(dayInfo);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public LiveData<DayInfo> getDayLiveData() {
        return dayLiveData;
    }

    /**
     * Fetches transactions for the current logged user from the repository.
     */
    public void getUserTransactions() {
        getUserTransactions(sharedPreferencesManager.getUser().getId());
    }

    public LiveData<List<Transaction>> getTransactionsLiveData() {
        return transactionsLiveData;
    }


    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

}
