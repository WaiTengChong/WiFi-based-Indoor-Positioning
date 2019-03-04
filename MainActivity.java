package com.example.user.d1test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan, buttonSwitch, buttonPlus,buttonDown,buttonAnalysis;
    int locCount=1;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private TextView tv;
    SQLiteDatabase db = null;
    Cursor cursor;



    String CREATE_TABLE = "CREATE TABLE  if not exists table1" + "(_id INTEGER PRIMARY KEY autoincrement, ssid TEXT, rss INTEGER, loc INTEGER)";
    String CREATE_TABLE2 = "CREATE TABLE  if not exists table2" + "(ssid TEXT, rss INTEGER, loc INTEGER)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = findViewById(R.id.scanBtn);
        buttonSwitch = findViewById(R.id.changeView);
        buttonPlus = findViewById(R.id.plus);
        buttonDown = findViewById(R.id.down);
        buttonAnalysis = findViewById(R.id.analysis);
        tv = findViewById(R.id.location);

        tv.setText(String.valueOf(locCount));
        db = openOrCreateDatabase("db.db", MODE_PRIVATE, null);

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
//        clearDatabase();

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clearDatabase();

                Toast.makeText(MainActivity.this, "Start Scanning WiFi for location - " + tv.getText().toString() + " ...", Toast.LENGTH_LONG).show();
                    for(int i = 0; i<30;i++) {
                        scanWifi();
                        Log.d("here","++"+ i);
                    }




                Toast.makeText(MainActivity.this, "Done Scanning WiFi for location - " + tv.getText().toString() + " ...", Toast.LENGTH_LONG).show();

            }
//                cursor = getAll();
//                UpdateAdapter(cursor);


        });

        buttonAnalysis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Start get average - ", Toast.LENGTH_LONG).show();
                String CREATE_TABLE2 = "CREATE TABLE  if not exists table3" + "(_id INTEGER PRIMARY KEY autoincrement, x INTEGER, y INTEGER, loc INTEGER)";
                db.execSQL(CREATE_TABLE2);
                db.execSQL("INSERT INTO table3 (x,y,loc) values (" + "'" + 99+ "'" + "," + 470 + ","+1 +")");
                db.execSQL("INSERT INTO table3 (x,y,loc) values (" + "'" + 99+ "'" + "," + 370 + ","+2 +")");
//                db.execSQL("INSERT INTO table3 (x,y,loc) values (" + "'" + 413+ "'" + "," + 520 + ","+3 +")");
                getaverage();
                Toast.makeText(MainActivity.this, "Done ", Toast.LENGTH_LONG).show();


            }
        });

        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, screen2.class);
                startActivity(intent);
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locCount++;
                tv.setText(String.valueOf(locCount));


            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(locCount - 1 < 1) {
                    locCount=1;
                    tv.setText(String.valueOf(locCount));
                }else{
                    locCount--;
                    tv.setText(String.valueOf(locCount));
                }

            }
        });


        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }



    }

    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM " + "table2";
        db.execSQL(clearDBQuery);
    }
    public void getaverage(){
        Set<String> setloc = new HashSet<String>();
        clearDatabase();


        Cursor cursor1 = db.rawQuery("SELECT loc FROM table1", null);

        if (cursor1.moveToFirst()){
            do{
                String data = cursor1.getString(cursor1.getColumnIndex("loc"));
                setloc.add(data);
                Log.d("average","loc has = "+ data);
            }while(cursor1.moveToNext());
            cursor1.close();
        }

        for (String s : setloc) {
            Set<String> setSSID = new HashSet<String>();
            Cursor cursor2 = db.rawQuery("SELECT ssid FROM table1 WHERE loc= '" + s + "'", null);
            if (cursor2.moveToFirst()){
                do{
                    String data = cursor2.getString(cursor2.getColumnIndex("ssid"));
                    setSSID.add(data);
                }while(cursor2.moveToNext());
            }
            cursor2.close();

            for (String i : setSSID) {
                ArrayList<Integer> arrayListRss = new ArrayList<>();
                Cursor cursor3 = db.rawQuery("SELECT rss FROM table1 WHERE ssid= '" + i + "' AND loc = "+s, null);
                if (cursor3.moveToFirst()){
                    do{
                        int data = cursor3.getInt(cursor3.getColumnIndex("rss"));
                        arrayListRss.add(data);
                    }while(cursor3.moveToNext());
                }
                int average = 0;
                for(int f = 0; f<arrayListRss.size();f++){
                    average = average + arrayListRss.get(f);

            }
            average = average/arrayListRss.size();
                Log.d("average","1 is ="+ average);
                int foo = Integer.parseInt(s);

                db.execSQL("INSERT INTO table2 (ssid,rss,loc) values (" + "'" + i + "'" + "," + average + ","+foo+")");
                average = 0;
                Log.d("average","2 is ="+ average);
                cursor3.close();

            }


        }
        setloc.clear();
    }

    public Cursor getAll() {
        Cursor cursor = db.rawQuery("SELECT _id, _id||'.'||ssid pssid, rss,loc FROM table1", null);

        return cursor;
    }

    private void scanWifi() {
        arrayList.clear();

        BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                results = wifiManager.getScanResults();

                unregisterReceiver(this);
                //ContentValues ct = new ContentValues();

                for (ScanResult scanResult : results) {
                    db.execSQL("INSERT INTO table1 (ssid,rss,loc) values (" + "'" + scanResult.BSSID + "'" + "," + wifiManager.calculateSignalLevel(scanResult.level, 100) + ","+tv.getText().toString()+")");
                    //ct.put("id" , scanResult.SSID);
                    // adapter.notifyDataSetChanged();
                    //https://www.youtube.com/watch?v=U4Xal1YrWjg
                }
                cursor = getAll();
                UpdateAdapter(cursor);

            }
        };
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();



    }

    public void UpdateAdapter(Cursor cursor) {
        if (cursor != null && cursor.getCount() >= 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"pssid", "rss"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);
        }
    }







}
