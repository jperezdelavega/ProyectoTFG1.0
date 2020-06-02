package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import pk.gb.useraccount.LoginActivity;
import pk.gb.useraccount.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fragments.FragmentAgregar;
import com.example.fragments.FragmentEliminar;
import com.example.fragments.FragmentFormularioItem;
import com.example.fragments.FragmentInventario;
import com.example.fragments.FragmentListaCompra;
import com.example.fragments.FragmentMenu;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_logo;
    FragmentMenu fragmentMenu;
    FragmentListaCompra fragmentListaCompra;
    FragmentInventario fragmentInventario;
    FragmentAgregar fragmentAgregar;
    FragmentEliminar fragmentEliminar;
    FragmentFormularioItem fragmentFormularioItem;
    Toolbar toolbar;
    CardView inventario,lista_compra,eliminar,agregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_toolbar);
        img_logo = (ImageView) findViewById(R.id.home_tool_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_reference) ;
        fragmentMenu = new FragmentMenu();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_principal,fragmentMenu).commit();
        fragmentInventario = new FragmentInventario();
        fragmentListaCompra = new FragmentListaCompra();
        fragmentAgregar = new FragmentAgregar();
        fragmentEliminar = new FragmentEliminar();
        fragmentFormularioItem = new FragmentFormularioItem();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tools,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_icon:
                Toast.makeText(getApplicationContext(),"Has pulsado setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_icon:
                borrarUsuario();
                finish();
                break;
        }
        return true;
    }

    private void borrarUsuario() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user","null");
        editor.commit();
    }

public void onClick(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    switch (view.getId()){
        case R.id.menu_invetory:
            transaction.replace(R.id.contenedor_principal,fragmentInventario,"inventario");
            transaction.addToBackStack("inventario");
            break;
        case R.id.lista_compra_menu:
            transaction.replace(R.id.contenedor_principal,fragmentListaCompra,"compra");
            transaction.addToBackStack("compra");
            break;
        case R.id.agregar_menu:
            transaction.replace(R.id.contenedor_principal,fragmentAgregar,"agrega");
            transaction.addToBackStack("agrega");
            break;
        case R.id.eliminar_menu:
            transaction.replace(R.id.contenedor_principal,fragmentEliminar,"eliminar");
            transaction.addToBackStack("eliminar");
            break;
        case R.id.home_tool_main:
            while (!getSupportFragmentManager().popBackStackImmediate());
            transaction.replace(R.id.contenedor_principal,fragmentMenu,"menu");

            break;
        case R.id.botonAnadirInventario:
            transaction.replace(R.id.contenedor_principal, fragmentFormularioItem, "addInventario");
            transaction.addToBackStack("addInventario");
            break;
    }
    transaction.commit();
}

    @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().popBackStackImmediate()){
            finish();
        };

    }
}
