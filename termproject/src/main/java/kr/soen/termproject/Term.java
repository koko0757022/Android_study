package kr.soen.termproject;

import android.*;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Term extends FragmentActivity {

    LocationManager mLocMan;
    Location location;
    GoogleMap mMap;
    String mProvider;
    Geocoder mCoder;
    ArrayList<LatLng> poly ;
    Button btn;
    double slat1;
    double slat2;
    static final LatLng SEOUL = new LatLng(37.56, 126.97);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.best);

        Log.v("TAG","어플생성");
        btn = (Button)findViewById(R.id.check1);
        poly = new ArrayList<LatLng>();

        FragmentManager fm = getFragmentManager();
        MapFragment frag = (MapFragment)fm.findFragmentById(R.id.map);

        frag.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
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
                    mLocMan.requestLocationUpdates(mProvider, 60 * 1000, 20, mListener);
                    //location = mLocMan.getLastKnownLocation(mProvider);
                    Log.d("TAG", "GPs_on키쟈~~~~~~~~");
                }else {

                        btn.setText("Gps_On");
                        PolylineOptions polyline = new PolylineOptions().addAll(poly).color(Color.BLUE).width(15);
                        mMap.addPolyline(polyline);
                        mLocMan.removeUpdates(mListener);

                }
                break;

            case R.id.check2:
                Intent intent = new Intent(this,StopWatch.class);
                startActivity(intent);
             break;

            case R.id.check3:
                break;

            case R.id.check4:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("TAG","어플종료");
    }
    /*
    public String geoConvert(double slat1, double slat2){

        List<Address> addr = null;
        mCoder = new Geocoder(this);
        try {
            addr = mCoder.getFromLocation(slat1, slat2, 1);
            //mResult4.setText("주소 :  " + addr.get(0).getAddressLine(0));

        }catch(IOException e){

        }
        return addr.get(0).getAddressLine(0);
    }
    */


    void setMapPosition(double lat, double lng, int zlevel){

        LatLng pt = new LatLng(lat,lng);
        Log.v("TAG","111111111111");
        CameraPosition cp = new CameraPosition.Builder().target(pt).zoom(zlevel).build();
        Log.v("TAG","2222222222222");
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        Log.v("TAG","3333333333333"+slat1);
        MarkerOptions marker = new MarkerOptions().position(pt);
        Log.v("TAG","44444444444444"+slat1);
        mMap.addMarker(marker);
        Log.v("TAG","55555555555"+slat1);
        poly.add(pt);
        Log.v("TAG","666666666");

    }

    LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            Log.d("TAG","업데이트가되려나???");
            slat1 = location.getLatitude();
            slat2 = location.getLongitude();

            Log.d("TAG","slat1 랑 slat2 까지는가네");

            setMapPosition(slat1,slat2,18);
            Log.v("TAG","위도"+slat1);

        }
        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };
}
