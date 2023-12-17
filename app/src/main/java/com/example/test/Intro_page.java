package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Intro_page extends AppCompatActivity {

    ImageButton mat_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        mat_btn=findViewById(R.id.mat_btn);
        mat_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent= new Intent(getApplicationContext(), Crud_page.class);
                startActivity(intent);
            }
        });
    }
}