package com.incobeta.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by witwicky on 01/10/17.
 */

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    //instance variables
    private static final String TAG = "WifiBroadcastReceiver";

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private ConnectActivity connectActivity;

    //default constructor
    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       ConnectActivity connectActivity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.connectActivity = connectActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //wifi p2p is enabled
            }
            else {
                //wifi p2p is disabled
            }
        }

//        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
//
//            if (manager == null) {
//                return;
//            }
//
//            NetworkInfo networkInfo = (NetworkInfo) intent
//                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
//
//            if (networkInfo.isConnected()) {
//
//                // we are connected with the other device, request connection
//                // info to find group owner IP
//                Log.d(TAG,
//                        "Connected to p2p network. Requesting network details");
//                manager.requestConnectionInfo(channel,
//                        (ConnectionInfoListener) activity);
//            } else {
//                // It's a disconnect
//            }
//        }
////        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
////
////            // request available peers from the wifi p2p manager. This is an
////            // asynchronous call and the calling activity is notified with a
////            // callback on PeerListListener.onPeersAvailable()
////            if (manager != null) {
////                manager.requestPeers(channel, (WifiP2pManager.PeerListListener) activity);
////            }
////            Log.d(TAG, "P2P peers changed");
////        }
//        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
//                .equals(action)) {
//
//            WifiP2pDevice device = (WifiP2pDevice) intent
//                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//            Log.d(TAG, "Device status -" + device.status);
//            manager.requestConnectionInfo(channel,
//                    (ConnectionInfoListener) activity);
//
//        }
    }
}
