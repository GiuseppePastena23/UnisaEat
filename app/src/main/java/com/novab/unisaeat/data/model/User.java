package com.novab.unisaeat.data.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String cf;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String status;
    private String phone;
    private String credit;

    public User(String id, String cf, String name, String surname, String email, String password, String status, String phone, String credit) {
        this.id = id;
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.status = status;
        this.phone = phone;
        this.credit = credit;
    }

    public User(String cf, String name, String surname, String email, String password, String status, String phone, String credit) {
        this.cf = cf;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.status = status;
        this.phone = phone;
        this.credit = credit;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", cf='" + cf + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", phone='" + phone + '\'' +
                ", credit='" + credit + '\'' +
                '}';
    }


}
