package com.example.e124138h.imago;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BlablablaActivity extends Activity {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;
    public UtilisateurDao utilisateurDao = new UtilisateurDao(this);
    public Utilisateur utilisateur = new Utilisateur();
    public MessageDao messageDao = new MessageDao(this);
    public Message messageEnvoyer;
    public Message messageRecus;
    public BlaBlaBlaClient blaBlaBlaClient;

    public ListView list;

    ArrayAdapter adapter;

    private GoogleApiClient client;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blablabla);

//        EditText smsBody = (EditText) findViewById(R.id.smsBody);
//        messageEnvoyer.setMessage(smsBody.getText().toString());


        //messageDao.insertMessage(messageEnvoyer);

        /*List<Message> messages = messageDao.getListMessage(
                Connexion.getInstance().getFriendName(),
                Connexion.getInstance().getMyName()
        );*/

        LinkedList<String> mStrings = new LinkedList<>();

        mStrings.add("Connection réussie");
        //for (Message m : messages) mStrings.add(m.getMessage());

        adapter = new ArrayAdapter<>(this, R.layout.list_black_text, R.id.list_content, mStrings);

        list = (ListView) findViewById(R.id.listMessage);

        list.setAdapter(adapter);

        final ImageButton image = (ImageButton) findViewById(R.id.photo);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.imageView2);



        final Button envoyer = (Button) findViewById(R.id.envoyer);
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText smsBody = (EditText) findViewById(R.id.smsBody);
                envoyer(smsBody.getText().toString());
                smsBody.setText("");
            }
        });

        blaBlaBlaClient = new BlaBlaBlaClient(
                Connexion.getInstance().getSocket(),
                new Handler(), new MessageHandler()
        );

        new Thread(blaBlaBlaClient).start();

        utilisateurDao.createOrRetreive();
        //messageEnvoyer = new Message(0, "", Connexion.getInstance().getMyId(), Connexion.getInstance().getFriendId());
        //messageRecus = new Message(0, "", Connexion.getInstance().getFriendId(), Connexion.getInstance().getMyId());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void envoyer(String message) {
        //messageEnvoyer.setMessage(message);
        //messageEnvoyer.setDate(new Date());

        // messageDao.insertMessage(messageEnvoyer);

        blaBlaBlaClient.sendMessage(message);

        adapter.add("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + Connexion.getInstance().getMyName() + " : " + message);
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);
    }

    private void selectImage()
    {
        final CharSequence[] items = { "Prendre une photo", "Choisir une photo", "Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(BlablablaActivity.this);
        builder.setTitle("Ajouter une photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Prendre une photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choisir une photo")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Choisir une photo"),
                            SELECT_FILE);
                } else if (items[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivImage = (ImageView) findViewById(R.id.imageView2);
                ivImage.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);


                ivImage = (ImageView) findViewById(R.id.imageView2);
                ivImage.setImageBitmap(bm);
            }
        }
    }


        @Override
        public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Blablabla Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.e124138h.imago/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Blablabla Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.e124138h.imago/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    class MessageHandler extends BlaBlaBlaMessageHandler {
        @Override
        public void run() {

            //messageRecus.setDate(new Date());
            //messageRecus.setMessage(message);
            // messageDao.insertMessage(messageRecus);

            adapter.add("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + Connexion.getInstance().getFriendName() + " : " + message);
            adapter.notifyDataSetChanged();
            list.setSelection(adapter.getCount() - 1);
        }
    }
}
