package com.novab.unisaeat.data.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private int id;
    private String cf;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String status;
    private String phone;
    private float credit;
    private String token;

    private static User instance = null;

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
}
