package mfm.learn.fingerprint;

import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Created by farras on 6/8/2020
 */public class ParsJson {
     JSONObject jsonObject;
     String NIP;
     String waktuMasuk;
     String status;

     public ParsJson(String jsonString){
         try {
             this.jsonObject = new JSONObject(jsonString);

             this.NIP = jsonObject.getString("NIP");
             this.waktuMasuk = jsonObject.getString("absen_masuk");
             this.status = jsonObject.getString("status");
         }
         catch (JSONException e){
             e.printStackTrace();
         }
     }

    public String getStatus() {
        return status;
    }

    public String getNIP() {
        return NIP;
    }

    public String getWaktuMasuk() {
        return waktuMasuk;
    }
}
