package com.example.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fragments.FragmentFormularioItem;
import com.example.fragments.FragmentInventario;
import com.example.fragments.FragmentListaCompra;
import com.example.fragments.FragmentMenu;
import com.example.inventario.AgregarCodigoBarra;
import com.example.inventario.EliminarCodigoBarra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import pk.gb.useraccount.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_logo;
    FragmentMenu fragmentMenu;
    FragmentListaCompra fragmentListaCompra;
    FragmentInventario fragmentInventario;

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
            //transaction.replace(R.id.contenedor_principal,fragmentAgregar,"agrega");
            //transaction.addToBackStack("agrega");
            Intent intent = new Intent(this, AgregarCodigoBarra.class);
            this.startActivity(intent);
            break;
        case R.id.eliminar_menu:
            //transaction.replace(R.id.contenedor_principal,fragmentEliminar,"eliminar");
            //transaction.addToBackStack("eliminar");
            Intent intentEliminar = new Intent(this, EliminarCodigoBarra.class);
            this.startActivity(intentEliminar);
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
