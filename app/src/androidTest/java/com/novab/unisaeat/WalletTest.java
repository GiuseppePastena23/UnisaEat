package com.novab.unisaeat;

import android.util.Log;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WalletTest {
    private TransactionRepository transactionRepository = new TransactionRepository();

    @Test
    public void testGetUserTransactions() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        transactionRepository.getUserTransaction(1, new TransactionRepository.TransactionsCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                System.out.println(transactions);
                Log.d("WalletTest", "onSuccess: " + transactions);
                assert !transactions.isEmpty();
                latch.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
                Log.d("WalletTest", "onError: " + errorMessage);
                assert false;
                latch.countDown();
            }
        });

        assert latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void rechargeWallet() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        transactionRepository.doTransaction(1, 5, "debug", new TransactionRepository.WalletRechargeCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Log.d("WalletTest", "onSuccess: " + result);
                assert true;
                latch.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
                Log.d("WalletTest", "onError: " + errorMessage);
                assert false;
                latch.countDown();
            }
        });

        assert latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void notEnoughMoney() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        transactionRepository.doTransaction(1, -1000000, "debug", new TransactionRepository.WalletRechargeCallback() {
            @Override
            public void onSuccess(String result) {
                assert false;
                latch.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
                Log.d("WalletTest", "onError: " + errorMessage);
                assert true;
                latch.countDown();
            }
        });

        assert latch.await(5, TimeUnit.SECONDS);
    }
}
