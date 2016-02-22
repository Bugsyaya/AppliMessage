package com.example.e124138h.imago;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Marina on 21/02/2016.
 */
public class TCPServer implements Runnable
{
    private Runnable uiUpdate;
    private int port;
    private Handler handler;
    private String pseudo;
    private Socket socket;

    public TCPServer(int port, Handler handler, Runnable uiUpdate)
    {
        this.port = port;
        this.handler = handler;
        this.uiUpdate = uiUpdate;
    }

    @Override
    public void run()
    {
        socket = null;
        pseudo = "";

        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            pseudo = in.readLine();
            out.println(Connexion.getInstance().getMyName());

            Connexion.getInstance().setFriendName(pseudo);
            Connexion.getInstance().setSocket(socket);

            handler.post(uiUpdate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
