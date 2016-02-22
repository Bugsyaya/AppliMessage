package com.example.e124138h.imago;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class UtilisateurDao {


    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "utilisateurs.db";

    public static final String TABLE_UTILISATEUR = "T_UTILISATEUR";
    public static final String COL_ID = "ID";
    public static final int NUM_COL_ID = 0;
    public static final String COL_PSEUDO = "PSEUDO";
    public static final int NUM_COL_PSEUDO = 1;

    public static final String CREATE_CONTACT_BDD = "CREATE TABLE " + TABLE_UTILISATEUR + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PSEUDO + " VARCHAR(255) NOT NULL);";

    private SQLiteDatabase bdd;

    private MyDBOpenHelper openHelper ;

    public UtilisateurDao(Context context){
        //On créer la BDD et sa table
        openHelper = new MyDBOpenHelper(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = openHelper.getWritableDatabase();
        openHelper.onUpgrade(bdd, 2, 3);
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertUtilisateur(Utilisateur utilisateur) {
        try
        {
            this.open();
            ContentValues values = new ContentValues();
            values.put(COL_PSEUDO, utilisateur.getPseudo());
            return bdd.insert(TABLE_UTILISATEUR, null, values);
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

    public int removeUtilisateurWithPseudo(String pseudo){
        try
        {
            this.open();
            return bdd.delete(TABLE_UTILISATEUR, COL_PSEUDO + " = " + pseudo, null);
        }
        finally {
            this.close();
        }
    }

    public Utilisateur getUtilisateurWithPseudo(String pseudo){
        try
        {
            this.open();
            //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
            Cursor c = bdd.query(TABLE_UTILISATEUR, null, COL_PSEUDO + " LIKE ?", new String[]{pseudo}, null, null, null);
            return cursorToUtilisateur(c);
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

    private Utilisateur cursorToUtilisateur(Cursor c)
    {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo(c.getString(NUM_COL_PSEUDO));
        utilisateur.setId(c.getInt(NUM_COL_ID));

        c.close();
        return utilisateur;
    }

    public void createOrRetreive()
    {
        Utilisateur friend = getUtilisateurWithPseudo(Connexion.getInstance().getFriendName());
        Utilisateur me = getUtilisateurWithPseudo(Connexion.getInstance().getMyName());

        if(friend == null)
        {
            long id = insertUtilisateur(new Utilisateur(Connexion.getInstance().getFriendName()));
            friend = getUtilisateurWithPseudo(Connexion.getInstance().getFriendName());
        }

        if(me == null)
        {
            long id = insertUtilisateur(new Utilisateur(Connexion.getInstance().getMyName()));
            me = getUtilisateurWithPseudo(Connexion.getInstance().getMyName());
        }

        //Connexion.getInstance().setMyId(me.getId());
        //Connexion.getInstance().setFriendId(friend.getId());
    }
}