package com.incobeta.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class ConnectActivity extends AppCompatActivity {

    //global variables
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;

    IntentFilter intentFilter;

    RelativeLayout connectInstructionsLayout;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Connect");

        //views
        connectInstructionsLayout = (RelativeLayout) findViewById(R.id.connectInstructionsLayout);
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);

        //define the network functionality
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }


    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.beginButton:
                //check if wifi is enabled
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (wifiManager.isWifiEnabled()) {

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    if (wifiInfo != null && wifiInfo.getNetworkId() != -1) {
                        Toast.makeText(this, "Wifi is good", Toast.LENGTH_SHORT).show();
                        connectInstructionsLayout.setVisibility(View.INVISIBLE);

                        animationView.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(this, "Not connected to any wifi. Ensure Step 2 is complete", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "Wifi is disabled. Ensure Step 1 is complete", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
