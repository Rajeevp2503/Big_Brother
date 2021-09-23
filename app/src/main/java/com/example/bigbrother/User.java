package com.example.bigbrother;

public class User {
    String name, email, password,pic;

    public User(String name, String pic) {
        this.name = name;
        this.pic = pic;

    }
    public User(String name , String email, String password){
        this.name = name;
        this.email = email;
        this.password=password;
    }
    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}