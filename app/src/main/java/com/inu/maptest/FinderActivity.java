package com.inu.maptest;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inu.maptest.databinding.ActivityFinderBinding;

import java.util.ArrayList;

public class FinderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityFinderBinding binding;

    private Spinner spinnerName = null;
    private int old = -1;
    private LatLng CITY_HALL = new LatLng(37.5662952, 126.977945099999994);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFinderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerName = (Spinner)findViewById(R.id.spinnerName);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fixed");

        myRef.getParent().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Toast.makeText(getApplicationContext(), "onDataChange", Toast.LENGTH_SHORT).show();

                ArrayList<String> list = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, list);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    if(key.equals("fixed")) {
                        continue; // 통과
                    }

                    list.add(key);

                }

                spinnerName.setAdapter(adapter);

                if(old != -1) { // 초기는 통과
                    spinnerName.setSelection(old);
                }

                spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String s = (String) adapterView.getItemAtPosition(position);
                        old = position; // 마지막에 선택했던 포지션
                        Toast.makeText(getApplicationContext(), "setOnItemSelectedListener: "+s, Toast.LENGTH_SHORT).show();

                        // 넘어온 키값으로 위도, 경도 얻어서 지도 출력
                        DatabaseReference ref = database.getReference(s);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 지도에 세팅

                                DO datas = snapshot.getValue(DO.class);
                                LatLng point = new LatLng(Double.parseDouble(datas.getLatitude()), Double.parseDouble(datas.getLongitude()));
                                mMap.addMarker(new MarkerOptions().position(point).title("Marker in User"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(17.5f));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}