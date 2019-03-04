package com.example.user.d1test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.graphics.Paint.Style;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class screen2 extends AppCompatActivity implements SensorEventListener{
    private RelativeLayout rl_Main;
    private WifiManager wifiManager;
    private Button mBtGoBack,mBtLocate;
    private ArrayList<String> arrayList = new ArrayList<>();
    private List<ScanResult> results;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    SQLiteDatabase db = null;
    int dataX,dataY;
    int max = 0;
    private float[] mGravity;
    HashMap<String, Integer> hmap = new HashMap<String, Integer>();
    HashMap<String, Integer> hmapdb = new HashMap<String, Integer>();
    ArrayList<Integer> rssarray = new ArrayList<>();
    HashMap<Integer,Integer> rssarrayNumberAndSqrt = new HashMap<Integer, Integer>();
    HashMap<Integer,Integer> rssarrayNumberAndSize = new HashMap<Integer, Integer>();
    Sensor Countsensor;
    SensorManager sensorManager;
    Sensor sr;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        rl_Main = (RelativeLayout) findViewById(R.id.rl_main);
        rl_Main.addView(new MyView(this));
        mBtGoBack = findViewById(R.id.GoBack);
        mBtLocate = findViewById(R.id.locate);
        db = openOrCreateDatabase("db.db", MODE_PRIVATE, null);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Countsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;




        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }



        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBtGoBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("here", "clicked");

                        finish();
                    }


                });

            }


        });

        mBtLocate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {




                if (Countsensor != null) {
                    sensorManager.registerListener(screen2.this, Countsensor, SensorManager.SENSOR_DELAY_NORMAL);
                    Log.d("run", "step: " + Countsensor);
                } else {
                    Toast.makeText(screen2.this, "Sensor not found!", Toast.LENGTH_SHORT).show();
                }
            }




        });
    }



public void onSensorChanged(SensorEvent event){
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        mGravity = event.values.clone();
        // Shake detection
        float x = mGravity[0];
        float y = mGravity[1];
        float z = mGravity[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent =  (float) Math.sqrt(x*x + y*y + z*z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        // Make this higher or lower according to how much
        // motion you want to detect
        if(mAccel > 3){
            // do something
            Log.d("move","moving!!");
        }
    }
     //   Log.d("count","X = "+ (int) event.values[0] + "  Y = "+ (int)event.values[1] + "  Z = "+ (int)event.values[2]);

}
public void onAccuracyChanged(Sensor sensor, int accuracy){


}


    class MyView extends View {


        Paint paint = new Paint();
        Point point = new Point();

        public MyView(Context context) {
            super(context);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5);
            paint.setStyle(Style.FILL);

        }
        private float dpFromPixels(float px)
        {
            return px / this.getContext().getResources().getDisplayMetrics().density;
        }

        private float pixelsFromDp(float dp)
        {
            return dp * this.getContext().getResources().getDisplayMetrics().density;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.emfloorplan);

            int bitmapWidth = b.getWidth();
            int bitmapHeight = b.getHeight();
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();

//            Log.d("here", "canvasWidth = " + canvasWidth);
//            Log.d("here", "canvasHeight= " + canvasHeight);
//            Log.d("here", "bitmapWidth = " + bitmapWidth);
//            Log.d("here", "bitmapHeight= " + bitmapHeight);
            // resize bitmap to fit in canvas
            if (bitmapWidth > canvasWidth) {

                double r  = (double) canvasWidth / (double) bitmapWidth;
//                Log.d("here", "r = " + r);

                b = Bitmap.createScaledBitmap(b,
                        (int) (bitmapWidth * r) , (int) (bitmapHeight), true);
//                Log.d("here", "bitmapWidth * r = " + bitmapWidth * r);
//                Log.d("here", "bitmapHeight= " + bitmapHeight);
            }else if(bitmapHeight > canvasHeight){
                double r2  = (double) canvasHeight/ (double) bitmapHeight;
                b = Bitmap.createScaledBitmap(b,
                        (int) (bitmapWidth) , (int) (bitmapHeight * r2), true);
//                Log.d("here", "bitmapWidth = " + bitmapWidth);
//                Log.d("here", "bitmapHeigh  * r2 = " + bitmapHeight * r2);
            }


            canvas.drawBitmap(b, 0, 100, paint);

            canvas.drawCircle(point.x, point.y, 10, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    location();




                    point.x = dataX;
                    point.y =dataY;


//                    point.x = (int) event.getX();
//                    point.y =(int) event.getY();
                    Log.d("here", "point x = " + point.x);
                    Log.d("here", "point y = " + point.y);
                    Log.d("touch","X ="+event.getX());
                    Log.d("touch","Y ="+event.getY());
            }
            invalidate();
            return true;

        }

        public void location(){
            hmap.clear();
            rssarrayNumberAndSqrt.clear();
            rssarrayNumberAndSize.clear();
            hmapdb.clear();
            rssarray.clear();
            max = 0;
            Set<Integer> setloc = new HashSet<Integer>();


            Cursor cursor = db.rawQuery("SELECT loc FROM table2", null);
            if (cursor.moveToFirst()){
                do{
                    setloc.add(cursor.getInt(cursor.getColumnIndex("loc")));


                }while(cursor.moveToNext());
            }

            cursor.close();


            for(Integer i : setloc) {
                if (i > max){
                    max = i;
                }
            }

            BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    results = wifiManager.getScanResults();
                    String data="NoData";
                    unregisterReceiver(this);
                    //ContentValues ct = new ContentValues();

                    for (ScanResult scanResult : results) {

                        hmap.put(scanResult.BSSID,wifiManager.calculateSignalLevel(scanResult.level, 100));

                    }

                    Log.d("here","hmap size is = " +hmap.size());


//                                Set set = hmap.entrySet();
//                                Iterator iterator = set.iterator();
//                                while(iterator.hasNext()) {
//                                    Map.Entry mentry = (Map.Entry)iterator.next();
////                                    System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
////                                    System.out.println(mentry.getValue());
//                                }



                    for(int g = 1 ; g < max +1; g ++) {
                        Log.d("here","g is = (1-3)" +g);




                        Cursor cursor2 = db.rawQuery("SELECT ssid,rss FROM table2 WHERE loc =" +g, null);
                        if (cursor2.moveToFirst()) {
                            do {
                                hmapdb.put(cursor2.getString(cursor2.getColumnIndex("ssid")),cursor2.getInt(cursor2.getColumnIndex("rss")));
                            } while (cursor2.moveToNext());
                        }

                        cursor2.close();

                        for ( String key : hmap.keySet() ) {
                            for ( String key2 : hmapdb.keySet() ) {
                                if (key.equals(key2)){
                                    int f = (int) Math.pow((hmap.get(key2)-hmapdb.get(key2)),2);
                                    rssarray.add(f);
                                };
                            };

                        }

                        hmapdb.clear();
                        Toast.makeText(screen2.this, "The size of same is ..." + rssarray.size(), Toast.LENGTH_SHORT).show();




                        int sum=0;
                        for(int k = 0; k < rssarray.size();k++){
                            sum+=rssarray.get(k);
                        }


                        //rssarrayfinal is to a list of sqrt of each loc and used to find the less one. But there is a problem with unbalanced data.

                        rssarrayNumberAndSqrt.put(g,(int) Math.round(Math.sqrt(sum)));
                        rssarrayNumberAndSize.put(g,rssarray.size());
                        rssarray.clear();
                    }

                    int minloc=0;
                    int maxSize=0;
                    int minSqrt= rssarrayNumberAndSqrt.get(1);

                    for ( int key : rssarrayNumberAndSize.keySet() ) {

                        if (rssarrayNumberAndSize.get(key) == maxSize ){

                            if (rssarrayNumberAndSqrt.get(key) <= minSqrt ){

                                minSqrt = rssarrayNumberAndSqrt.get(key);
                                minloc= key;

                            }
                            else  if (rssarrayNumberAndSqrt.get(minloc) <= minSqrt ){

                                minSqrt = rssarrayNumberAndSqrt.get(minloc);
                            }

                        }else if (rssarrayNumberAndSize.get(key) > maxSize ){
                            maxSize = rssarrayNumberAndSize.get(key);
                            minloc= key;
                        }
                    }

                    rssarrayNumberAndSqrt.clear();
                    rssarrayNumberAndSize.clear();

                    // if they are equal then check sqrt else which big is big.
                    Log.d("here", "minloc = " + minloc);



                    Cursor cursor3 = db.rawQuery("SELECT x,y FROM table3 WHERE loc =" +minloc, null);
                    if (cursor3.moveToFirst()) {
                        do {
                            Log.d("here", "dataX x = " + dataX);
                            Log.d("here", "dataY y = " + dataY);
                            dataX = cursor3.getInt(cursor3.getColumnIndex("x"));
                            dataY = cursor3.getInt(cursor3.getColumnIndex("y"));
                        } while (cursor3.moveToNext());
                    }
                    cursor3.close();

                    Log.d("here", "dataX x = " + dataX);
                    Log.d("here", "dataY y = " + dataY);

                }
            };




            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();



        }



    }
    class Point {
        int x, y;

        public int setX (int  xx){
            x = xx;

            return x;
        }
        public int setY (int  yy){
            y = yy;

            return y;
        }

    }

}
