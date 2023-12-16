package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigationrail.NavigationRailView;

import java.util.Objects;

public class Crud_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_page);

        NavigationRailView navigationRailview= findViewById(R.id.navigationRail);
        NavController navController = Navigation.findNavController(Crud_page.this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationRailview, navController);

        Objects.requireNonNull(navigationRailview.getHeaderView()).setOnClickListener(new View.OnClickListener(){
            @Override
            public void OnClick(View v){
                Toast.makeText(Crud_page.this,"Replace this with your own action", Toast.LENGTH_SHORT).show();
            }
        });
    }
}