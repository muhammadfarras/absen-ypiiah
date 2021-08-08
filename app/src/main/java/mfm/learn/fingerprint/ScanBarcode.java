package mfm.learn.fingerprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBarcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = "ScanBarcode";
    private static final int SCAN_BARCODE_PERMISSION = 1;
    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        mScannerView = new ZXingScannerView(ScanBarcode.this);
        setContentView(mScannerView);

        if (ContextCompat.checkSelfPermission(ScanBarcode.this,Manifest.permission.CAMERA)
                != (PackageManager.PERMISSION_GRANTED)){
            Toast.makeText(ScanBarcode.this,"Butuh izin untuk akses kamera antum",Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(ScanBarcode.this,new String[]{Manifest.permission.CAMERA},SCAN_BARCODE_PERMISSION);
        }
        else {
            Toast.makeText(ScanBarcode.this,"Scan barcode dari HRD Imam Ahmad Bin Hanbal",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SCAN_BARCODE_PERMISSION:
                Intent mIntent = new Intent(ScanBarcode.this,ScanBarcode.class);
                startActivity(mIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        GetLink myUrl = new GetLink(ScanBarcode.this);
        myUrl.updateLink(rawResult.getText());
        Toast.makeText(ScanBarcode.this,"Alhamdulillah scan berhasil",Toast.LENGTH_LONG).show();
        startActivity(new Intent(ScanBarcode.this,MainActivity.class));
        finish();
    }
}
