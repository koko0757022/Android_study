package kr.soen.my_location_logger2;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.*;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

public class MapsActivity extends Activity {
    LocationManager mLocMan;
    GoogleMap mMap;
    TextView mResult;
    String mProvider;
    Geocoder mCoder;
    TextView mResult4;
    Location location;
    String sloc;
    ArrayList<LatLng> poly ;
    static final LatLng SEOUL = new LatLng(37.56, 126.97);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mResult = (TextView)findViewById(R.id.result);
        mResult4 = (TextView)findViewById(R.id.result4);

        poly = new ArrayList<LatLng>();


        FragmentManager fm = getFragmentManager();
        MapFragment frag = (MapFragment)fm.findFragmentById(R.id.map);
        frag.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                //Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        });

        mProvider = mLocMan.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        mLocMan.requestLocationUpdates(mProvider, 10000, 0, mListener);
        location = mLocMan.getLastKnownLocation(mProvider);

        //slat1 = location.getLatitude();
        //slat2 = location.getLongitude();


        findViewById(R.id.check1).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                double slat1;
                double slat2;

                location = mLocMan.getLastKnownLocation(mProvider);
                slat1 = location.getLatitude();
                slat2 = location.getLongitude();

                mResult4.setText("주소 : "+geoConvert(slat1,slat2));
                setMapPosition(slat1,slat2,18);

            }
        });
        findViewById(R.id.check2).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                PolylineOptions polyline = new PolylineOptions().addAll(poly).color(Color.BLUE).width(15);
                mMap.addPolyline(polyline);

            }
        });
    }

    LocationListener mListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            if (location == null) {
                sloc = "최근 위치 : 알수 없음";
            } else {
                sloc = String.format("위도:%f\n경도:%f\n고도:%f",
                        location.getLatitude(), location.getLongitude(), location.getAltitude());

            }

            mResult.setText(sloc);
        }
        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

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
    void setMapPosition(double lat, double lng, float zlevel){

        LatLng pt = new LatLng(lat,lng);
        CameraPosition cp = new CameraPosition.Builder().target(pt).zoom(zlevel).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        MarkerOptions marker = new MarkerOptions().position(pt);
        mMap.addMarker(marker);


        poly.add(pt);
    }
}


