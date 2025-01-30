package com.novab.unisaeat;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;

public class UserTest {

    private UserRepository userRepository;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void testUser() throws InterruptedException {
        userRepository.login("m.r", "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                System.out.println(user);
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });


        // Controlla che il valore sia stato aggiornato
        assertNotNull("Il valore userLiveData non dovrebbe essere null", userLiveData.getValue());
        System.out.println(userLiveData.getValue());
    }
}
