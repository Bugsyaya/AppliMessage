package com.example.e124138h.imago;

import java.net.Socket;

/**
 * Created by Marina on 21/02/2016.
 */
public class Connexion {
    private Socket socket;
    private String friendName;
    private int friendId;
    private String myName;
    private int myId;

    private static Connexion instance;

    private Connexion(){
        socket = null;
        friendName = "";
    }

    public synchronized static Connexion getInstance()
    {
        return instance == null ? instance = new Connexion() : instance;
    }

    public synchronized Socket getSocket(){
        return socket;
    }

    public synchronized String getFriendName(){
        return friendName;
    }

    public synchronized void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public synchronized void setFriendName(String pseudo)
    {
        this.friendName = pseudo;
    }

    public synchronized String getMyName()
    {
        return myName;
    }

    public synchronized void setMyName(String myName)
    {
        this.myName = myName;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }
}
