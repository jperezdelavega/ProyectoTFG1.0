package com.example.inventario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pk.gb.useraccount.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AgregarCodigoBarra extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_codigo_barra);
        escanearCodigo();
    }

    private void escanearCodigo() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Escaner");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() != null){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.dialog,null);
                TextView barCode = view.findViewById(R.id.CodigoDialog);
                Button btnCancelar = view.findViewById(R.id.cancelarDialog);
                barCode.setText("Codigo de barras: "+result.getContents());
                alertDialog.setView(view);
                AlertDialog dialog = alertDialog.create();
                dialog.show();

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }else{
                Toast.makeText(this,"No se ha podido leer el codigo",Toast.LENGTH_LONG);
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }

}
