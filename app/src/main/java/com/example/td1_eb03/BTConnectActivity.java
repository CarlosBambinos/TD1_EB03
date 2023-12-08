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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBlueToothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice pairedDevice : pairedDevices) {
                    mPairedAdapter.add(pairedDevice.getName() + "\n" + pairedDevice.getAddress());
                }
            } else {
                mPairedAdapter.add("pas de périphérique appairé");
            }

        }
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

            /* TODO 1 : instanciation d'un broadcastreceiver (nommé mBroadcastReceiver) + redéfinition
                de la méthode onReceive.
                Explication :
                La méthode onReceive a pour but d'intercepter les 2 signaux BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                et BluetoothDevice.ACTION_FOUND.
                Pour BluetoothAdapter.ACTION_DISCOVERY_FINISHED, il faut :
                 1 - il faut compter les éléments de mDiscoveredAdapter et traiter le cas où rien n'a été trouvé
                 2 - Désactiver la progressBar
                Pour BluetoothAdapter.ACTION_DISCOVERY_FINISHED, il faut :
                 1 - Ajouter le device à la liste mDiscoveredAdapter, uniquement s'il n'est pas déjà dans la liste
                     des devices associés.
             */

    }




    /* TODO 4 : Après avoir indiqué que la class BTConnectActivity implémentait l'interface
        View.OnClickListener, redéfinir la méthode onClick.
        Explication :
        Cette méthode est unique pour tous les boutons de l'activité et L'activité devenant ainsi le listener de tous
        les boutons, la méthode onClick est unique : elle doit donc discriminer elle même quel bouton a été appuyé
        La méthode view.getId() permet de connaitre le bouton à l'origine de l'appel de onClick.
          - Si l'interface BT est désactivée :
                1a - il faut l'activer ici (par une intention implicite BluetoothAdapter.ACTION_REQUEST_ENABLE)
          - Sinon :
                1a - on appelle ensuite la méthode toggleBtScan() pour activer ou stopper la recherche.
     */




    /* TODO 3 : implémenter une méthode de bascule entre lancement du scann et arrêt.
       Explication :
       Cette méthode appellera btScan. Si le texte d'un bouton vaut Scanner, appeler btScan(START)
       sinon appeler btScan(STOP)
    */
    private void toggleBtScan() {
    }

    /* TODO 2 :
        Créez une méthode btScan destinée à lancer une recherche BT ou à la stopper.
        Explication :
            1 - Définir une enumeration constante Action pouvant prendre la valeur START ou STOP.
                Elle sera utilisée comme argument de la fonction.
            2 - Si l'argument vaut START :
                 2a - Créer un filtre d'intention pour indiquer au BroadcastManager que notre application
                      souhaite être prévenue lorsque la recherche BT est terminée (BluetoothAdapter.ACTION_DISCOVERY_FINISHED).
                      Associer associer à ce filtre l'action à exécuter en cas de réception du message (registerReceiver).
                      L'action est celle programmée dans la méthode onReceive de notre broadcastReceiver
                      donc il suffit de fournir la référence vers notre broadcastReceiver.
                 2b - Idem pour le message BluetoothDevice.ACTION_FOUND
                 2c - Lancer la découverte (méthode startDiscovery()),
                 2d - Activer la progressBar et passer le texte du bouton à "Scanner"
              - Si l'argument vaut STOP :
                 2a - Stopper la recherche (méthode cancelDiscovery())
                 2b - Désactiver la progressBar et passer le texte du bouton à "Stopper"
                 2b - Dissocier le filtre pour chaque action

     */

    @SuppressLint("MissingPermission")
    private void btScan(Action startstop){
        if(startstop==Action.START){
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mBroadcastReceiver, filter);
            mBroadcastRegistered = true;
            mBlueToothAdapter.startDiscovery();

        }else{
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastRegistered = false;
            mBlueToothAdapter.cancelDiscovery();
        }
    }

    /* TODO 5 :
        L'intention d'activation du BT peut renvoyer un RESULT_OK ou un refus d'activation
        qu'il faut traiter ici.
        Explication :
        - Si l'utilisateur a accepté :
            1a - on lance ou on stoppe le scanne par toggleBtScan().
        - Sinon,
            1a - on affiche un toast et c'est tout.

     */

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