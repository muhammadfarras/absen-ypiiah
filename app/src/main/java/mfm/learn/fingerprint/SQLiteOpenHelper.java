package mfm.learn.fingerprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/*
 * Created by farras on 6/7/2020
 */public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

     private static final String DB_NAME = "Link";
     private static final int DB_VERSION = 1;


     SQLiteOpenHelper (Context context){
         super(context, DB_NAME,null,DB_VERSION);

     }

    @Override
    public void onCreate(SQLiteDatabase db) {

         // membuat DB
        db.execSQL("CREATE TABLE LINKABSEN (" +
                "ID INTEGER, " +
                "LINK TEXT);");

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",1);
        contentValues.put("LINK","default");
        db.insert("LINKABSEN",null,contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    private static void addDb (SQLiteDatabase db, int id, String Link ){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("ID",id);
//        contentValues.put("LINK",Link);
//
//        db.insert("LINKABSEN",null,contentValues);
//    }
}
