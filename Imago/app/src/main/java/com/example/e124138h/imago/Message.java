package com.example.e124138h.imago;

import java.util.Date;

/**
 * Created by Marina on 20/02/2016.
 */
public class Message {
    private int id;
    private String message;
    private Date date;
    private int idUtilisateurFrom;
    private int idUtilisateurTo;

    public Message(int id, String message, Date date, int idUtilisateurFrom, int idUtilisateurTo)
    {
        this.id = id;
        this.message = message;
        this.date = date;
        this.idUtilisateurFrom = idUtilisateurFrom;
        this.idUtilisateurTo = idUtilisateurTo;
    }

    public Message(int id, String message, int idUtilisateurFrom, int idUtilisateurTo)
    {
        this.id = id;
        this.message = message;
        this.idUtilisateurFrom = idUtilisateurFrom;
        this.idUtilisateurTo = idUtilisateurTo;
        this.date = new Date();
    }

    public Message()
    {
        this.id = 0;
        this.message = "";
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdUtilisateurFrom()
    {
        return idUtilisateurFrom;
    }

    public void setIdUtilisateurFrom(int idUtilisateurFrom) {
        this.idUtilisateurFrom = idUtilisateurFrom;
    }

    public int getIdUtilisateurTo() {
        return idUtilisateurTo;
    }

    public void setIdUtilisateurTo(int idUtilisateurTo) {
        this.idUtilisateurTo = idUtilisateurTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
