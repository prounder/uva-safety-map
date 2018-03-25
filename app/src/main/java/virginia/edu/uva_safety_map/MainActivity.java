package virginia.edu.uva_safety_map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng UVA = new LatLng(38.0336, -78.507980);
    private FirebaseDatabase db;
    private ArrayList<String> crimes = new ArrayList<String>();

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
        mMap.setMinZoomPreference(13f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UVA));

        mMap.setMyLocationEnabled(true);
        displayHistory(30);

//        Marker uva = mMap.addMarker(new MarkerOptions()
//                .position(UVA)
//                .title("University of Virginia")
//                .snippet("go hoos"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.050244, -78.500885))
                .title("ASSIST CITIZEN"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.031185, -78.498823))
                .title("ASSIST AGENCY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.033446, -78.51493))
                .title("UNDERAGE POSSESSION"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.031919, -78.49866))
                .title("HIT AND RUN"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.032040, -78.50043))
                .title("TRAFFIC ACCIDENT"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.031919, -78.49866))
                .title("DOMESTIC DISTURBANCE"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.031185, -78.498823))
                .title("LARCENY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.034771, -78.516652))
                .title("SEXUAL ASSAULT"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.051631, -78.499785))
                .title("SIMPLE ASSAULT"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.034832, -78.515063))
                .title("DRUNK IN PUBLIC"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.039163, -78.505397))
                .title("SUICIDE"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.031185, -78.498823))
                .title("THREATENING TEXT MESSAGES"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.029105, -78.521101))
                .title("BURGLARY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.049336, -78.510111))
                .title("BURGLARY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.033246, -78.521323))
                .title("ANNOYING EMAILS"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.047126, -78.51105))
                .title("BURGLARY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.034961, -78.51057))
                .title("PROPERTY DAMAGE"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.038286, -78.503002))
                .title("NARCOTICS VIOLATION"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.050146, -78.510278))
                .title("BURGLARY"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.032880, -78.51349))
                .title("SEXUAL OFFENSE"));

//        CircleOptions circleOptions = new CircleOptions()
//                .center(UVA)
//                .strokeColor(Color.argb(0, 255, 0, 0))
//                .fillColor(Color.argb(50, 255, 0, 0))
//                .radius(500); // In meters
//
//        // Get back the mutable Circle
//        Circle circle = mMap.addCircle(circleOptions);


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
//                        Log.d("firebase", dataSnapshot.getChildrenCount() + " #hours");
                        for(DataSnapshot hour : loc.getChildren()) {
//                            Log.d("firebase", loc.getChildrenCount() + " #entries");
                            for(DataSnapshot list : hour.getChildren()) {
//                                Log.d("firebase", hour.getChildrenCount() + " #cases");
                                td.put(list.getKey(), list.getValue(String.class));
                            }
                        }
                    }

//                ArrayList<Job_Class> values = new ArrayList<>(td.values());
//                List<String> keys = new ArrayList<String>(td.keySet());
                    crimes.add(td.get("2"));
                    createMarker(td.get("1"));
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

    private void createMarker(String address) {
        if(address == null)
            return;
        address = address.replace(' ', '+');
        Log.d("Response is: ", address);
        String GEOCODE_API = "AIzaSyC55We6_XnAppTxl50XAKBcVedWM0yhAdQ";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address="+address+",+Charlottesville,+VA&key=" + GEOCODE_API;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        Log.d("Response is: ", response);
                        double[] toReturn = new double[2];
                        int index1 = response.indexOf("lat\" : ")+6;
                        int index2 = response.indexOf("lng\" : ")+6;
                        toReturn[0] = Double.parseDouble(response.substring(index1, index1 + 4));
                        toReturn[1] = Double.parseDouble(response.substring(index2, index2 + 4));
                        addToLLS(toReturn);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.d("Response is: ", "Didn't work");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void addToLLS(double[] d){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(d[0], d[1]))
                .title(crimes.remove(0)));
    }
}
