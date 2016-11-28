package kr.soen.termproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Term extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.best);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn = (Button)findViewById(R.id.check1);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
               if(btn.getText().toString() == "Gps_On"){
                   btn.setText("Gps_Off");
               }else{
                   btn.setText("Gps_On");
               }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Seoul = new LatLng(37.56, 126.97);
        mMap.addMarker(new MarkerOptions().position(Seoul).title("Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Seoul));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }
}
