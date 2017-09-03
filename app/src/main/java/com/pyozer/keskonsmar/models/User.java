package com.pyozer.keskonsmar.models;

public class User {

    public String username;
    public String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String toString() {
        return "@" + username;
    }
}
