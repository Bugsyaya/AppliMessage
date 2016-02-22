package com.example.e124138h.imago;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Socket;

public class ContacterActivity extends AppCompatActivity {

    EditText editTextAddress;
    String editTextPort;
    Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacter);


        TextView pseudoAttente = (TextView) findViewById(R.id.pseudoAttente);
        pseudoAttente.setText(MainActivity.getIpUtilisateur());

        final Button annulation = (Button) findViewById(R.id.annulation);

        annulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContacterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editTextAddress = (EditText)findViewById(R.id.ipContact);
        editTextPort = "8080";
        buttonConnect = (Button)findViewById(R.id.lancement);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
    }

    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View arg0) {
            MyClientTask myClientTask = new MyClientTask(ContacterActivity.this, editTextAddress.getText().toString(), Integer.parseInt(editTextPort));
            myClientTask.execute();
        }
    };

    public void onConnected(Socket socket, String pseudo)
    {
        Connexion.getInstance().setSocket(socket);
        Connexion.getInstance().setFriendName(pseudo);
        Intent intent = new Intent(ContacterActivity.this, BlablablaActivity.class);
        startActivity(intent);
    }
}
