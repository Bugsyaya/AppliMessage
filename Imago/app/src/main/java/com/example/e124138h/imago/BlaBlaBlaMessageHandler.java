

package com.example.e124138h.imago;

import android.graphics.Bitmap;

public abstract class BlaBlaBlaMessageHandler implements Runnable
{
    protected String message;
    public String type;
    public Bitmap image;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}