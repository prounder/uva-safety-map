package virginia.edu.uva_safety_map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import android.graphics.Color;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng UVA = new LatLng(38.0336, -78.507980);
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseDatabase.getInstance();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UVA));

        mMap.setMyLocationEnabled(true);
        displayHistory(30);

//        Marker uva = mMap.addMarker(new MarkerOptions()
//                .position(UVA)
//                .title("University of Virginia")
//                .snippet("go hoos"));

        CircleOptions circleOptions = new CircleOptions()
                .center(UVA)
                .strokeColor(Color.argb(0, 255, 0, 0))
                .fillColor(Color.argb(50, 255, 0, 0))
                .radius(500); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);


    }

    private void displayHistory(int d){
        int day = 19;
        int mon = 3;
        for(int i = 0; i < d; i++) {
            DatabaseReference myRef = db.getReference("2018-" + String.format("%02d", mon) + "-" + day);
            day--;
            if(day == 0){
                day = 28;
                mon--;
            }

            // TAKE LOCATION, PLACE PIN WITH POINT DESCRIPTION AND CIRCLE
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> td = new HashMap<String, String>();
                    for (DataSnapshot loc : dataSnapshot.getChildren()) {
                        td.put(loc.getKey(), loc.getValue(String.class));
                    }

//                ArrayList<Job_Class> values = new ArrayList<>(td.values());
//                List<String> keys = new ArrayList<String>(td.keySet());

                    for (String s : td.keySet()) {
                        Log.d("firebase", s + " : " + td.get(s));
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DATABASE", "Failed to read value.", error.toException());
                }
            });
        }
    }
}
