package example.android.ubsmanaus;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import example.android.ubsmanaus.Adapter.Adapter;
import example.android.ubsmanaus.Adapter.MapsAdapter;
import example.android.ubsmanaus.Model.Ubs;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Ubs ubs;
    private boolean firstRender = true;
    private MapsAdapter adapter;
    private ArrayList ubsList = new ArrayList<Ubs>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //pega ubs passada pela main
        ubs = (Ubs) getIntent().getSerializableExtra("ubs");
        ubsList.add(ubs);

        //inicializa mapFragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        adapter = new MapsAdapter(this, ubsList);

        final TextView name = (TextView) findViewById(R.id.nomeUBS);
        name.setText(ubs.nome);

        final TextView bairro = (TextView) findViewById(R.id.nomeBairro);
        bairro.setText(ubs.bairro);


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
