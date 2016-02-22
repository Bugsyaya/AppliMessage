package com.example.e124138h.imago;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_CONTACT = "T_CONTACT";

    public static final String TABLE_MESSAGE = "T_MESSAGE";

    public MyDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(UtilisateurDao.CREATE_CONTACT_BDD);
        db.execSQL(MessageDao.CREATE_MESSAGE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE IF EXISTS " + UtilisateurDao.TABLE_UTILISATEUR + ";");
        db.execSQL("DROP TABLE IF EXISTS " + MessageDao.TABLE_MESSAGE + ";");
        onCreate(db);
    }
}
