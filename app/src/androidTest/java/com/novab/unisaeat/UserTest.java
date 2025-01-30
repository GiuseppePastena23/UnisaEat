package com.novab.unisaeat;

import org.junit.Test;

import android.util.Log;

import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UserTest {

    UserRepository userRepository = new UserRepository();

    @Test
    public void testLogin() throws InterruptedException {
        String email = "m.r";
        String password = "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb";

        for (int i = 0; i < 100; i++) {
            int attempt = i + 1;
            Log.d("TEST ATTEMPT", "Login attempt: " + attempt);

            CountDownLatch latch = new CountDownLatch(1);

            userRepository.login(email, password, new UserRepository.LoginCallback() {
                @Override
                public void onSuccess(User user) {
                    System.out.println(user);
                    Log.d("UserTest", "onSuccess: " + user);
                    assert 1 == user.getId();
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

            if (!latch.await(5, TimeUnit.SECONDS)) {
                Log.d("UserTest", "Timeout al tentativo " + attempt);
                assert false;
            }
        }
    }
}
