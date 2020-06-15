package com.example.inventario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

public class DialogError extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EliminarCodigoBarra eliminarCodigoBarra = new EliminarCodigoBarra();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error en la Despensa");
        builder.setMessage("El producto que ha introducido no se encuentra en la despensa");
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
