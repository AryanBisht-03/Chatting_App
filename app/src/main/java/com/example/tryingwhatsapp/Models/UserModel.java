package com.example.tryingwhatsapp.Models;

public class UserModel {
     String profilpic,userName,email,password,userId,lastMessage;

    public UserModel(String profilpic, String userName, String email, String password, String userId, String lastMessage) {
        this.profilpic = profilpic;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public UserModel()
    {

    }

    public UserModel(String email,String password,String userName)
    {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getProfilpic() {
        return profilpic;
    }

    public String getUserId() {
        return userId;
    }

    public void setProfilpic(String profilpic) {
        this.profilpic = profilpic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
