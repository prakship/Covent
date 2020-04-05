package com.example.inevent;

public class User {
    String name;
    String email;
    String permission;
    String city;
    String phone;

    public User(String email, String name, String permission,
                String city, String phone) {
        this.name = name;
        this.email = email;
        this.permission = permission;
        this.city = city;
        this.phone = phone;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPermission() {
        return permission;
    }

    public String getCity() { return city; }

    public String getPhone() { return phone; }



}

