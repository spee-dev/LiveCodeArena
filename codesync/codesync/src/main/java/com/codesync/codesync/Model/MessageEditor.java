package com.codesync.codesync.Model;


public class MessageEditor {

    Integer id;
    String message;
    String sessionId;
    public MessageEditor(Integer id ,String message, String sessionId){
        this.id=id;
        this.message=message;
        this.sessionId=sessionId;
    }
    public Integer getId(){return id;}
    public String getMessage(){return message;}
    public String getSessionId(){return sessionId;}
    public void setId(Integer id){this.id=id;}
    public void setMessage(String message){this.message=message;}
    public void setSessionId(String sessionId){this.sessionId=sessionId;}



}
