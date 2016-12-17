package kr.soen.termproject;

import android.*;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Term extends FragmentActivity {

    DataBase mHelper;
    SQLiteDatabase db;
    LocationManager mLocMan;
    Location location;
    GoogleMap mMap;
    String mProvider;
    Geocoder mCoder;
    ArrayList<LatLng> poly ;
    Button btn;
    double slat1;
    double slat2;
    final static LatLng SEOUL = new LatLng(37.56, 126.97);
    final static int ACT_EDIT2 = 2;
    final static int ACT_EDIT3 = 3;

    String watch_out;
    String record_out;
    String loc;

    String Field1;
    String Field2;
    String Field3;
    String total;

    long myBaseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.best);

        mHelper = new DataBase(this);
        btn = (Button)findViewById(R.id.check1);
        poly = new ArrayList<LatLng>();

        FragmentManager fm = getFragmentManager();
        MapFragment frag = (MapFragment)fm.findFragmentById(R.id.map);

        frag.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
               // Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        });
        mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mProvider = mLocMan.getBestProvider(new Criteria(), true);
    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.check1:

                if(btn.getText().toString() == "Gps_On") {
                    btn.setText("Gps_Off");

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                    mLocMan.requestLocationUpdates(mProvider,  60*1000, 10, mListener);
                    Log.d("TAG", "GPs_on키쟈~~~~~~~~");

                    myBaseTime = SystemClock.elapsedRealtime();
                    myTimer.sendEmptyMessage(0);  //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                }else {

                        btn.setText("Gps_On");

                        mLocMan.removeUpdates(mListener);
                    watch_out = getTimeOut();
                    mMap.clear();                }
                break;

            case R.id.check3:
                Intent intent2 = new Intent(this,Record.class);
                startActivityForResult(intent2,ACT_EDIT3);
                break;

            case R.id.check4:
                select();
                Log.d("TAG","select실행");
                Intent intent3 = new Intent(this,Statistics.class);
                intent3.putExtra("final",total);
                startActivity(intent3);
                Log.d("TAG","통계가 제대로 작동");
                break;
            case R.id.store:
                Log.d("TAG","저장버튼클릭");
                insert(loc, watch_out, record_out);
                Log.d("TAG","insert 저장.");
                break;
            case R.id.clear:
                db = mHelper.getWritableDatabase();
                db.execSQL("DELETE FROM avg");
                Log.d("TAG","clear 완료");
                mHelper.close();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACT_EDIT2:
                if(resultCode == RESULT_OK){
                    //database에 저장합니다
                   watch_out = data.getStringExtra("WatchOut");
                    Log.d("TAG","watch_out 대입");
                }
                break;
            case ACT_EDIT3:
                if(resultCode == RESULT_OK){
                    //database에 저장합니다
                    record_out = data.getStringExtra("RecordOut");
                    Log.d("TAG","record_out 대입");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("TAG","어플종료");
    }

    public void geoConvert(double slat1, double slat2){

        List<Address> addr = null;
        mCoder = new Geocoder(this);
        try {
            addr = mCoder.getFromLocation(slat1, slat2, 1);
            loc = addr.get(0).getAddressLine(0);
            //mResult4.setText("주소 :  " + addr.get(0).getAddressLine(0));

        }catch(IOException e){
        }
    }


    void setMapPosition(double lat, double lng, int zlevel){

        LatLng pt = new LatLng(lat,lng);
        CameraPosition cp = new CameraPosition.Builder().target(pt).zoom(zlevel).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        MarkerOptions marker = new MarkerOptions().position(pt);
        mMap.addMarker(marker);
        poly.add(pt);

    }

    LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            Log.d("TAG","업데이트가되려나???");
            slat1 = location.getLatitude();
            slat2 = location.getLongitude();
            setMapPosition(slat1,slat2,18);
            geoConvert(slat1,slat2);
            Log.d("TAG","주소변환완료");
            PolylineOptions polyline = new PolylineOptions().addAll(poly).color(Color.BLUE).width(15);
            mMap.addPolyline(polyline);

        }
        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    public void insert(String loc, String time, String rec){
        db = mHelper.getWritableDatabase();
        db.execSQL("INSERT INTO avg VALUES(null, '" + loc + "', '" + time + "', '" + rec + "');");
        Log.d("TAG","execSQL 제대로 실행완료.");
        mHelper.close();

    }
    public void select(){
        db = mHelper.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT location,time,record FROM avg",null);
        String result="";
        Log.d("TAG","1단계");

        while(cursor.moveToNext()){
            Field1 = cursor.getString(0);
            Field2 = cursor.getString(1);
            Field3 = cursor.getString(2);

            result += ("location = " + Field1 + "\n"+"time = "+Field2+"\n"+"record = "+Field3+"\n"+"----------"+"\n" );
            total = result;
        }
        Log.d("TAG","2단계");
        if(result.length() == 0){
            total = "내용없음";
        }
        mHelper.close();
    }

    Handler myTimer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);
        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        return easy_outTime;
    }
}
