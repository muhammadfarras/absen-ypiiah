package mfm.learn.fingerprint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/*
 * Created by farras on 6/7/2020
 */public class GetLink {
    private Context context;

    public GetLink(Context context_param) {
        this.context = context_param;
    }

    public String getLink() {
        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(context);


        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("LINKABSEN", new String[]{"Link"}, "ID = 1", null, null, null, null);

            if (cursor.moveToFirst()) {
                String linkResult = cursor.getString(0);
                return linkResult;
            }
        } catch (
                SQLException e) {
            return "Data base tidak tersedia";
        }

        return "";
    }

    public String updateLink(String resultBarcode) {
        try {
            android.database.sqlite.SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(context);
            ContentValues contentValues = new ContentValues();
            contentValues.put("LINK",resultBarcode);
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            db.update("LINKABSEN",contentValues,"ID = 1",null);
            return "Sukses";
        }
        catch (SQLException e){
            return e.toString();
        }
    }

}
