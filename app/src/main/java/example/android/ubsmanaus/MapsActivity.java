package example.android.ubsmanaus;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import example.android.ubsmanaus.Model.Ubs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Ubs ubs;
    private boolean firstRender = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //pega ubs passada pela main
        ubs = (Ubs) getIntent().getSerializableExtra("ubs");

        //inicializa mapFragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //coordenadas da ubs
        LatLng ubslatlng = new LatLng(Double.parseDouble(ubs.latitude), Double.parseDouble(ubs.longitude));

        //adiciona marcador e move a camera do mapa p ele
        googleMap.addMarker(new MarkerOptions().position(ubslatlng).title(ubs.nome));

        CameraUpdate current = CameraUpdateFactory.newLatLngZoom(ubslatlng,15.5f);
        if (firstRender) {
            googleMap.animateCamera(current);
            firstRender = false;
        }
        else {
            googleMap.moveCamera(current);
        }
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubslatlng, 15.5f));
    }
}
