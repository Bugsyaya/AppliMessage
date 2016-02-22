package com.example.e124138h.imago;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class AttendreActivity extends AppCompatActivity {
    Handler UIUpdateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendre);

        UIUpdateHandler = new Handler();
        Thread serverThread = new Thread(new TCPServer(8080, UIUpdateHandler, new UIUpdate()));
        serverThread.start();
    }

    class UIUpdate implements Runnable
    {
        @Override
        public void run() {
            //Connexion Réussie

            Intent intent = new Intent(AttendreActivity.this, BlablablaActivity.class);
            startActivity(intent);
        }
    }
}
