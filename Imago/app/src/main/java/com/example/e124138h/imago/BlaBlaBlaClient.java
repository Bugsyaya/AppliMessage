

package com.example.e124138h.imago;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class BlaBlaBlaClient implements Runnable
{
    public Socket socket;
    public Handler handler;
    public BlaBlaBlaMessageHandler uiUpdate;
    public PrintStream out;
    public DataOutputStream dout;
    public BufferedReader in;
    public DataInputStream din;

    public BlaBlaBlaClient(Socket socket, Handler handler, BlaBlaBlaMessageHandler uiUpdate) {
        this.socket = socket;
        this.handler = handler;
        this.uiUpdate = uiUpdate;

        try {
            out = new PrintStream(socket.getOutputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            din = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String type = "";
            while((type = in.readLine()) != null)
            {
                uiUpdate.setType(type);

                if(type.contains("text")) {
                    String message = in.readLine();
                    uiUpdate.setMessage(message);
                }
                else {
                    int len = din.readInt();
                    byte[] data = new byte[len];
                    if(len > 0) din.readFully(data, 0, data.length);
                    Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                    uiUpdate.setImage(image);
                }
                handler.post(uiUpdate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message)
    {
        out.println("text");
        out.println(message);
        out.flush();
    }

    public void sendImage(Bitmap image)
    {
        out.println("image");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] data = bos.toByteArray();

        try {
            dout.writeInt(data.length);
            dout.write(data, 0, data.length);
        } catch (IOException e) {
            Log.e("NETWORK", e.getMessage(), e);
        }
    }
}