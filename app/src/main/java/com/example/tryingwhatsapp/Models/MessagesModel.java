package com.example.tryingwhatsapp.Models;

public class MessagesModel {

    String id,message,messageid;
    Long time;

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public MessagesModel(String id, String message, Long time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }

    public MessagesModel(String id,String message)
    {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public MessagesModel()
    {


    }


}
