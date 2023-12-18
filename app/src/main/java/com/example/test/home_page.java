package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class home_page extends AppCompatActivity {

    Button mat_btn, weather_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mat_btn=findViewById(R.id.mat_btn);
        weather_btn=findViewById(R.id.weather_btn);
        mat_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent= new Intent(getApplicationContext(), board.class);
                startActivity(intent);
            }
        });
        weather_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent WeatherIntent= new Intent(getApplicationContext(), weather_page.class);
                startActivity(WeatherIntent);
            }
        });
    }
}