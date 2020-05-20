package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import pk.gb.useraccount.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fragments.FragmentMenu;

public class MenuActivity extends AppCompatActivity {
    ImageView img_logo;
    FragmentMenu fragmentMenu;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_toolbar);
        img_logo = (ImageView) findViewById(R.id.home_tool_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_reference) ;
        fragmentMenu = new FragmentMenu();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_principal,fragmentMenu).commit();
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tools,menu);
       return true;
    }

}
