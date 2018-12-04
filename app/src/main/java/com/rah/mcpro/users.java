package com.rah.mcpro;

public class users {
   // String id;
    String username;
    String password;
    String phone;
    String blocked;
    String rating;
//    String reports;

    public users() {
    }

    public users(String username, String password, String phone, String blocked, String rating) {
        //this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.blocked = blocked;
        this.rating = rating;
    }

   /* public String getId() {
        return id;
    }*/

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getBlocked() {
        return blocked;
    }

    /*public void setId(String id) {
        this.id = id;
    }*/

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
