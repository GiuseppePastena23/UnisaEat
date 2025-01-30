package com.novab.unisaeat;

import android.util.Log;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OrdersTest {

    private TransactionRepository transactionRepository = new TransactionRepository();

    @Test
    public void testGetOrders() throws InterruptedException {


            CountDownLatch latch = new CountDownLatch(1);


            transactionRepository.getOrders(new TransactionRepository.OrdersCallback() {

                @Override
                public void onSuccess(List<Transaction> transactions) {
                    System.out.println(transactions);
                    Log.d("OrderTest", "onSuccess: " + transactions);
                    assert 2 == transactions.size();
                    latch.countDown();
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);
                    Log.d("UserTest", "onError: " + errorMessage);
                    assert false;
                    latch.countDown();
                }
            });

            if (!latch.await(60, TimeUnit.SECONDS)) {
                assert false;
            }

}

}
