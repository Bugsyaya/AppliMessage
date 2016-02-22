package com.example.e124138h.imago;

/**
 * Created by Marina on 15/02/2016.
 */
public class Utilisateur {

    public String pseudo;
    public int id;

    public Utilisateur(String pseudo)
    {
        this.setPseudo(pseudo);
    }

    public Utilisateur()
    {
        this.setPseudo("");
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}