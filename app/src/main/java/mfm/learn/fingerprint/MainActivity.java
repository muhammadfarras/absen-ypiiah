package mfm.learn.fingerprint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricPrompt;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.biometrics.BiometricManager;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button mButton;
    private Button buttonJaringan;
    private Button buttonScanBarcode;
    private TextView mtextNiy;
    private TextView mDateView;
    private TextView mTimeIn;
    private TextView mTimeOut;
    private HttpURLConnection httpURLConnection;
    private URL url;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        buttonScanBarcode = (Button) findViewById(R.id.button4);
        mtextNiy = (TextView) findViewById(R.id.textView5);
        mDateView = (TextView) findViewById(R.id.textView2);
        mTimeIn =  (TextView) findViewById(R.id.textView8);
        mTimeOut = (TextView) findViewById(R.id.textView9);



//        Toast.makeText(MainActivity.this,getLink.getLink(),Toast.LENGTH_LONG).show();

        // Asyntask untuk melihat update terbaru dari kehadiran
        GetLink getLink = new GetLink(MainActivity.this);
        AsynStatus m = new AsynStatus();

        if ((!getLink.getLink().equals("default"))){
            m.execute(getLink.getLink().replace("index.php","status.php"));
        }
        //





        goFingerPrint();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    public void goFingerPrint (){
        executor = ContextCompat.getMainExecutor(MainActivity.this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Authentication Error",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent mIntent = new Intent(MainActivity.this,AbsenJSON.class);
                startActivity(mIntent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Absen SDIT Anshal")
                .setDescription("Absen menggunakan aplikasi android")
                .setNegativeButtonText("Absen manual dikantor")
                .build();


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent (MainActivity.this,ScanBarcode.class);
                startActivity(mIntent);
            }
        };
        buttonScanBarcode.setOnClickListener(listener);
    }


    private class AsynStatus extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL (strings[0]);
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
            }
            catch (IOException e){
                Toast.makeText(MainActivity.this,"Tidak konek ke server",Toast.LENGTH_LONG).show();
            }

            try {
                int responeCode = httpURLConnection.getResponseCode();

                if (responeCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder hasil = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null){
                        hasil.append(line);
                    }
                    return hasil.toString();
                }
                else {
                    return ("Tidak berhasil");
                }
            }
            catch (IOException e){
                e.printStackTrace();
                return e.toString();
            }
            finally {
                httpURLConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject hasilRekap = new JSONObject(s.replace("index.php","status.php"));
                mtextNiy.setText("NIY : "+hasilRekap.getString("NIP"));
                mDateView.setText("Tanggal : "+hasilRekap.getString("tanggal"));
                mTimeIn.setText(hasilRekap.getString("absen_masuk"));
                mTimeOut.setText(hasilRekap.getString("absen_keluar"));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
