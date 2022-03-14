package com.inu.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnFinder = null;
    Button btnUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinder = (Button) findViewById(R.id.btnFinder);
        btnUser = (Button)findViewById(R.id.btnUser);

        btnFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FinderActivity.class);
                startActivity(i);
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(i);
            }
        });
    }
}

// 추적자, 사용자 버튼 있는 메인액티비티
// 지도를 보여줄 FinderActivity
// 시작, 중지 버튼을 보여줄 UserActivity