package com.example.td1_eb03;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
public class MainActivity extends AppCompatActivity {

    TextView mtv;
    Slider msl;
    int MenuRes = R.menu.menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtv = findViewById(R.id.tv);
        msl = findViewById(R.id.slider3);

        msl.setSliderChangeListener((value) -> mtv.setText(String.format("%2f", value)));

    }

    public void requestAgain(){
        btPermissionLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN});
    }
    ActivityResultLauncher<String[]> btPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result ->{
        if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)){
            showDialog("Permission denied", "Autorisation requise", "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            requestAgain();
                        }
                    },
                    "Non, quitter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    }, false);
        }
    });

    private void verifyBtPermission(){
        if (BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(this, "This device required bluetooth module", Toast.LENGTH_LONG).show();
            return;
        }
        if (!hasBTPermissions()){
            Log.i("Permissions", "not granted");
            btPermissionLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT});
        }
    }

    private boolean hasBTPermissions(){
        String[] permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                        return false;
                }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(MenuRes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        switch(menuItem){
            case R.id.connect :
                verifyBtPermission();
                Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog showDialog(String title,
                                   String message,
                                   String positivelabel,
                                   DialogInterface.OnClickListener positiveOnClick,
                                   String negativelabel,
                                   DialogInterface.OnClickListener negativeOnClick,
                                   boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancelable);
        builder.setPositiveButton(positivelabel,positiveOnClick);
        builder.setNegativeButton(negativelabel,negativeOnClick);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
}

