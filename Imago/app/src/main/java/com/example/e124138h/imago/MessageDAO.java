package com.example.e124138h.imago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class MessageDao
{
    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "messages.db";

    public static final String TABLE_MESSAGE = "T_MESSAGE";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_ID_CONTACT_FROM = "ID_CONTACT_FROM";
    private static final int NUM_COL_ID_CONTACT_FROM = 1;
    private static final String COL_ID_CONTACT_TO = "ID_CONTACT_TO";
    private static final int NUM_COL_ID_CONTACT_TO = 2;
    private static final String COL_MESSAGE = "MESSAGE";
    private static final int NUM_COL_MESSAGE = 3;
    private static final String COL_DATE = "DATE";
    private static final int NUM_COL_DATE = 2;

    public static final String CREATE_MESSAGE_BDD = "CREATE TABLE " + TABLE_MESSAGE + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_ID_CONTACT_FROM + " INT NOT NULL," +
            COL_ID_CONTACT_TO + " INT NOT NULL," +
            COL_MESSAGE + " VARCHAR(255) NOT NULL," +
            COL_DATE + " DATETIME NOT NULL," +
            "FOREIGN KEY (" + COL_ID_CONTACT_FROM +") REFERENCES " +
            UtilisateurDao.TABLE_UTILISATEUR + "(" + UtilisateurDao.COL_ID + ")," +
            "FOREIGN KEY (" + COL_ID_CONTACT_TO + ") REFERENCES " +
            UtilisateurDao.TABLE_UTILISATEUR + "(" + UtilisateurDao.COL_ID + ")" +
        ");";

    private SQLiteDatabase bdd;

    private MyDBOpenHelper openHelper ;

    public MessageDao(Context context)
    {
        openHelper = new MyDBOpenHelper(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = openHelper.getWritableDatabase();
        openHelper.onUpgrade(bdd, 1, 3);
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertMessage(Message message){
        try
        {
            this.open();

            ContentValues values = new ContentValues();

            //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
            values.put(COL_ID_CONTACT_FROM, message.getIdUtilisateurFrom());
            values.put(COL_ID_CONTACT_TO, message.getIdUtilisateurTo());
            values.put(COL_MESSAGE, message.getMessage());
            values.put(COL_DATE, String.valueOf(message.getDate()));

            //on insère l'objet dans la BDD via le ContentValues
            return bdd.insert(TABLE_MESSAGE, null, values);
        }
        finally {
            this.close();
        }
    }

    public int removeMessageWithId(int id){
        try
        {
            this.open();
            return bdd.delete(TABLE_MESSAGE, COL_ID + " = " + id, null);
        }
        finally {
            this.close();
        }
    }

    public List<Message> getListMessage(String friend, String me)
    {
        try {
            this.open();
            Cursor c = bdd.rawQuery("SELECT m." + COL_MESSAGE + " FROM " + TABLE_MESSAGE + " AS m " +
                    "JOIN " + UtilisateurDao.TABLE_UTILISATEUR +" AS uf " +
                    "ON uf." + UtilisateurDao.COL_ID + " = m." + COL_ID_CONTACT_FROM +
                    " JOIN " + UtilisateurDao.TABLE_UTILISATEUR + " AS ut "+
                    "ON ut." + UtilisateurDao.COL_ID + " = m." + COL_ID_CONTACT_TO +
                    " WHERE uf." + UtilisateurDao.COL_PSEUDO + " = ?" +
                    "OR (uf." + UtilisateurDao.COL_PSEUDO + " = ? AND ut." + UtilisateurDao.COL_PSEUDO + " = ?)" +
                    ";",
                    new String[]{friend, me, friend});
            return cursorToMessageList(c);
        }
        catch (Exception e)
        {
            Log.e("BDD", e.getMessage(), e);
            throw e;
        }
        finally {
            this.close();
        }
    }

    private List<Message> cursorToMessageList(Cursor c)
    {
        List<Message> result = new ArrayList<>(c.getCount());

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            result.add(cursorToMessage(c));

        return result;
    }

    private Message cursorToMessage(Cursor c)
    {
        return new Message(
                c.getInt(NUM_COL_ID),
                c.getString(NUM_COL_MESSAGE),
                c.getInt(NUM_COL_ID_CONTACT_FROM),
                c.getInt(NUM_COL_ID_CONTACT_TO)
        );
    }
}