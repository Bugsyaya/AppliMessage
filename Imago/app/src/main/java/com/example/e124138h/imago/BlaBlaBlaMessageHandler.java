package com.example.e124138h.imago;

/**
 * Created by Marina on 21/02/2016.
 */
public abstract class BlaBlaBlaMessageHandler implements Runnable
{
    protected String message;

    public void setMessage(String message) {
        this.message = message;
    }
}
