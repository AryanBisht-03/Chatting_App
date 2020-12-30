package com.example.tryingwhatsapp.Models;

public class StatusModel {

    String userName,profilpic,status;

    public StatusModel(String userName, String profilpic, String status) {
        this.userName = userName;
        this.profilpic = profilpic;
        this.status = status;
    }

    public StatusModel()
    {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilpic() {
        return profilpic;
    }

    public void setProfilpic(String profilpic) {
        this.profilpic = profilpic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
