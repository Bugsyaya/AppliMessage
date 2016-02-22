package com.example.e124138h.imago;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.e124138h.imago.ContacterActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Marina on 21/02/2016.
 */
public class MyClientTask extends AsyncTask<Void, Void, Void> {
    String dstAddress;
    int dstPort;
    ContacterActivity contacterActivity;
    Socket socket;
    String pseudo;

    MyClientTask(ContacterActivity contacterActivity, String addr, int port) {
        this.dstAddress = addr;
        this.dstPort = port;
        this.contacterActivity = contacterActivity;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            out.println(Connexion.getInstance().getMyName());
            pseudo = in.readLine();

        } catch (IOException e) {
            Log.e("NETWORK", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        contacterActivity.onConnected(socket, pseudo);
    }
}

