package com.example.td1_eb03;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class BTConnectActivity extends AppCompatActivity {

    private final static int RN42_COD = 0x1F00;

    private enum Action {START, STOP}

    private Toolbar mToolBar;
    private Button mScann;

    private ListView mPairedList;
    private ListView mDiscoveredList;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean mBroadcastRegistered = false;

    private BluetoothAdapter mBlueToothAdapter;
    private ArrayAdapter<String> mPairedAdapter;
    private ArrayAdapter<String> mDiscoveredAdapter;

    private Set<String> foundList = new HashSet<String>();

    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_connect2);

        // installation de la toolbar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mProgressBar = findViewById(R.id.progressBar);

        // installation du listener de bouton scann
        mScann = findViewById(R.id.scan_button);


        mScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBtScan();
            }
        });

            /* TODO 7 : Mettre en place les listeners du bouton
               et des la listView
            */

        // installation des listeners d'item ListView
        mPairedList = findViewById(R.id.appaired_list);
        mDiscoveredList = findViewById(R.id.discovered_list);


        // les adaptateurs associent une présentation à une liste
        mPairedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDiscoveredAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mPairedList.setAdapter(mPairedAdapter);
        mDiscoveredList.setAdapter(mDiscoveredAdapter);

        // création de la liste des périphériques liés
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueToothAdapter != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBlueToothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice pairedDevice : pairedDevices) {
                    mPairedAdapter.add(pairedDevice.getName() + "\n" + pairedDevice.getAddress());
                }
            } else {
                mPairedAdapter.add("Pas de périphérique appairé");
            }

        }
        mBroadcastReceiver = new BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        if (mDiscoveredAdapter.getCount() == 0){
                            mDiscoveredAdapter.add("aucun périphérique trouvé");
                        }
                    mScann.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if ((device.getBondState() != BluetoothDevice.BOND_BONDED) && (device.getBluetoothClass().getDeviceClass() == RN42_COD)) {

                            if (!foundList.contains(device.getAddress())) {
                                foundList.add(device.getAddress());
                                mDiscoveredAdapter.add(device.getName() + "\n" + device.getAddress());
                            }
                        }
                        break;
                }

            }
        };
    }


    @SuppressLint("MissingPermission")
    private void btScan(Action startStop) {
        if (startStop == Action.START) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

             registerReceiver(mBroadcastReceiver, filter);
            mBroadcastRegistered = true;

            mBlueToothAdapter.startDiscovery();

            mProgressBar.setVisibility(View.VISIBLE);
            mScann.setText("Stopper");
        } else if (startStop == Action.STOP) {

            mBlueToothAdapter.cancelDiscovery();

            if (mBroadcastRegistered) {
                unregisterReceiver(mBroadcastReceiver);
                mBroadcastRegistered = false;
            }
            mProgressBar.setVisibility(View.GONE);
            mScann.setText("Scanner");
        }
    }


    private void toggleBtScan() {
        if (mScann.getText().toString().equalsIgnoreCase("Scanner")) {
            btScan(Action.START);
        } else {
            btScan(Action.STOP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    /* TODO 6 : Il s'agit ici de traiter la sélection d'un device sur lequel on veut se connecter.
        Après avoir indiqué que la class BTConnectActivity implémentait l'interface
        AdapterView.OnItemClickListener, redéfinir la méthode OnItemClick. Ne pas oublier que cette
        méthode est également appelée pour tous les listView de l'activité.
        Explications :
            1 - Stopper la recherche BT
            2 - Récupérer le nom du device cliqué par un getText sur le view passé en argument
            3 - Traiter le cas où le clic correspond à "Aucun élément appairé" ou "Aucun élément trouvé".
            Dans ce cas, le retour à l'activité principale doit s'accompagner d'un setResult(RESULT_CANCELED);
            et d'un finish()
            4 - Passer l'adresse du device à l'activité principale à l'aide du champ extra d'un intent en
            utilisant la méthode setResult(RESULT_OK, intent).
     */

   /* TODO 8 : Dans onPause s'assurer que la découverte BT est stoppée et que l'application est
               désabonnée des messages de broadcast.
    */

    @Override
    protected void onPause() {
        super.onPause();
    }

}