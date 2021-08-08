package mfm.learn.fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class AbsenJSON extends AppCompatActivity {
    TextView mTextView;
    getJson mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_j_s_o_n);

        mTextView = (TextView) findViewById(R.id.textView);
        mJson = new getJson();
        mJson.execute();


        Intent mIntent = new Intent (AbsenJSON.this,MainActivity.class);
        startActivity(mIntent);
    }

    private class getJson extends AsyncTask <String, String ,String> {

        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... string) {

            try {

                GetLink mGetLink = new GetLink(AbsenJSON.this);

                url = new URL(mGetLink.getLink());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.setDoOutput(true);
            }
            catch (IOException e1){
                e1.printStackTrace();
                return e1.toString();
            }

            try {
                int responeCode = httpURLConnection.getResponseCode();

                //chec jika konesi terjadi
                if (responeCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder hasil = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null){
                        hasil.append(line);
                    }

                    return  hasil.toString();
                }
                else {
                    return ("Tidak berhasil");
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
                return e2.toString();
            }
            finally {
                httpURLConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            ParsJson mJson = new ParsJson(s);

////
//            mTextView.append("NIP : "+mJson.getNIP());
//            mTextView.append("Waktu Masuk : "+mJson.getWaktuMasuk());
            Intent intent = new Intent(AbsenJSON.this,MainActivity.class);

            if(mJson.getStatus() == null){
                Toast.makeText(AbsenJSON.this,s,Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(AbsenJSON.this,mJson.getStatus(),Toast.LENGTH_LONG).show();
            }

            startActivity(intent);
        }
    }
}


