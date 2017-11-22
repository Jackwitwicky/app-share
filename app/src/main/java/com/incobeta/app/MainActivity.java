package com.incobeta.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private RecyclerView appRecyclerView;
    private AppAdapter appAdapter;

    String firstAppPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connectIntent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(connectIntent);

            }
        });

        ArrayList<PInfo> appList = getPackages();

        appRecyclerView = (RecyclerView) findViewById(R.id.appRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        appRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        appRecyclerView.addItemDecoration(dividerItemDecoration);

        appAdapter = new AppAdapter(appList);
        appRecyclerView.setAdapter(appAdapter);
    }

    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        PackageInfo neededPackage = null;
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
//            if ((!getSysPackages) && (p.versionName == null)) {
//                continue ;
//            }

            if (  (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                PInfo newInfo = new PInfo();
                newInfo.setAppname(p.applicationInfo.loadLabel(getPackageManager()).toString());
                newInfo.setPname(p.packageName);
                newInfo.setVersionName(p.versionName);
                newInfo.setVersionCode(p.versionCode);

                String sourceDir = p.applicationInfo.publicSourceDir;
                newInfo.setSourcDir(sourceDir);

                newInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));
                res.add(newInfo);

                Log.i("Source dir: ", p.applicationInfo.dataDir);



                firstAppPath = p.applicationInfo.dataDir;
                neededPackage = p;
            }



        }

        return res;
    }

    private class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolder> {

        private ArrayList<PInfo> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView appNameView;
            ImageView appIconView;
            TextView appVersionView;
            ImageView shareApkView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.appNameView = (TextView) itemView.findViewById(R.id.appName);
                this.appVersionView = (TextView) itemView.findViewById(R.id.appVersion);
                this.appIconView = (ImageView) itemView.findViewById(R.id.appIcon);
                this.shareApkView = (ImageView) itemView.findViewById(R.id.shareApk);

                shareApkView.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                //empty on click
                PInfo selectedInfo = dataSet.get(getAdapterPosition());

                File apkFile = new File(selectedInfo.getSourcDir());
                if (apkFile.exists()) {
                    //open intent
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        }


        public AppAdapter(ArrayList<PInfo> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_item, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            TextView appNameView = holder.appNameView;
            TextView appVersionView = holder.appVersionView;
            ImageView appIconView = holder.appIconView;


            appNameView.setText(dataSet.get(listPosition).getAppname());
            appVersionView.setText(dataSet.get(listPosition).getVersionName());
            appIconView.setImageDrawable(dataSet.get(listPosition).getIcon());

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

    }
}
