package virginia.edu.uva_safety_map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import android.graphics.Color;

import java.io.InputStream;
import java.util.List;
import android.util.Log;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List data;
    private static final LatLng UVA = new LatLng(38.0336, -78.5080);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        InputStream inputStream = getResources().openRawResource(R.raw.parseddata);
        CSVFile csvFile = new CSVFile(inputStream);
        data = csvFile.read();
        Log.i("file structure", data.get(1).toString());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        Start the map at Charlottesville
        mMap.setMaxZoomPreference(40f);
        mMap.setMinZoomPreference(15f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(38.0336, -78.507980)));

        Marker uva = mMap.addMarker(new MarkerOptions()
                .position(UVA)
                .title("University of Virginia")
                .snippet("go hoos"));

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(UVA)
                .strokeColor(Color.argb(0, 255, 0, 0))
                .fillColor(Color.argb(50, 255, 0, 0))
                .radius(500); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);
     }
}
