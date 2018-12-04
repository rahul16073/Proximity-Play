package com.rah.mcpro;

public class ratings {
    String critic, user, rate,id;

    public ratings() {
    }

    public ratings(String critic, String user, String rate, String id) {
        this.critic = critic;
        this.user = user;
        this.rate = rate;
        this.id = id;
    }

    public String getCritic() {
        return critic;
    }

    public void setCritic(String critic) {
        this.critic = critic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
