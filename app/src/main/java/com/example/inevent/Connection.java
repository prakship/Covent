package com.example.inevent;

public class Connection {

    String email;
    String gId;

    public Connection(String email, String gId) {
        this.email = email;
        this.gId = gId;
    }

    public Connection() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }
}
