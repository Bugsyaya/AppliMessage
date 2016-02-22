package com.example.e124138h.imago;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class BlaBlaBlaClient implements Runnable
{
    private Socket socket;
    private Handler handler;
    private BlaBlaBlaMessageHandler uiUpdate;
    private PrintStream out;
    private BufferedReader in;

    public BlaBlaBlaClient(Socket socket, Handler handler, BlaBlaBlaMessageHandler uiUpdate) {
        this.socket = socket;
        this.handler = handler;
        this.uiUpdate = uiUpdate;

        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message = "";
            while((message = in.readLine()) != null)
            {
                uiUpdate.setMessage(message);
                handler.post(uiUpdate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object message)
    {
        out.println(message);
        out.flush();
    }
}
