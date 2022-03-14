package com.inu.maptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserActivity extends AppCompatActivity implements LocationListener {
// 시작버튼 눌렀을 때
    // 위치값 실시간으로 얻어오기
    // 실시간으로 파이어베이스에 저장

    // 중지버튼 눌렀을 때
    //위치값 받아오기 종료
    // 파이어베이스에 저장하지 않음

    Button btnStart = null;
    Button btnStop = null;
    LocationManager manager = null;
    EditText edtName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        edtName = (EditText) findViewById(R.id.etdName);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                String name = edtName.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "onClick: " + name, Toast.LENGTH_SHORT).show();
                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, UserActivity.this);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(name);

                    Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    myRef.child("latitude").setValue(""+lat);
                    myRef.child("longitude").setValue(""+lon);

                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (manager != null) {
                    manager.removeUpdates(UserActivity.this);
                    manager = null;
                }
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String name = edtName.getText().toString();
        DatabaseReference myRef = database.getReference(name);

        Toast.makeText(getApplicationContext(), "onLocationChanged: " + name, Toast.LENGTH_SHORT).show();
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        myRef.child("latitude").setValue(""+lat);
        myRef.child("longitude").setValue(""+lon);

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}