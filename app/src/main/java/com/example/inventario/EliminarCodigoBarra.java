package com.example.inventario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pk.gb.useraccount.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EliminarCodigoBarra extends AppCompatActivity {
    String URLConsulta = "http://3.15.228.207/codigoBarras/valoresDespensaConCodigoEliminar.php";
    String URLEliminar = "http://3.15.228.207/codigoBarras/eliminarProductoCodigoBarra.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        escanearCodigo();
    }

    private void escanearCodigo() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View view = layoutInflater.inflate(R.layout.dialog_eliminar,null);
                final TextView nombreDialog = view.findViewById(R.id.nombreDialog_eliminar);
                final TextView unidadesDespensaDialog = view.findViewById(R.id.unidadesEnDespensaDialog_eliminar);
                final EditText unidadesAEliminar = view.findViewById(R.id.CampoUnidadesDialog_eliminar);
                Button cancelarbttn = view.findViewById(R.id.cancelarDialog_eliminar);
                Button aceptarbttn = view.findViewById(R.id.aceptarDialog_eliminar);
                final Integer[] unidadesEnDespensa = new Integer[1];
                alertDialog.setView(view);
                AlertDialog dialog = alertDialog.create();
                dialog.show();

                StringRequest stringRequestConsultaDatos = new StringRequest(Request.Method.POST, URLConsulta, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("esta").equals("true")){
                                nombreDialog.setText("Nombre: "+ jsonObject.getString("nombre"));
                                unidadesEnDespensa[0] = Integer.parseInt(jsonObject.getString("unidades"));
                                unidadesDespensaDialog.setText("Unidades en la despensa: "+jsonObject.getString("unidades"));
                            }else{
                                Intent error = new Intent(getApplicationContext(),DialogError.class);
                                getApplicationContext().startActivity(error);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"falla",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = view.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("codigo",result.getContents());

                        return params;
                    }
                };

                requestQueue.add(stringRequestConsultaDatos);

                cancelarbttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                aceptarbttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (unidadesEnDespensa[0]>Integer.parseInt(unidadesAEliminar.getText().toString())){
                            StringRequest stringRequestAceptar = new StringRequest(Request.Method.POST, URLEliminar, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    SharedPreferences preferences = view.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                                    String user = preferences.getString("user","null");
                                    params.put("email",user);
                                    params.put("codigo", result.getContents());
                                    params.put("unidades",unidadesAEliminar.getText().toString());
                                    return params;
                                }
                            };
                            requestQueue.add(stringRequestAceptar);
                        }else{
                            Toast.makeText(getApplicationContext(),"No se puede eliminar mas unidades de las que hay en la despensa",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }else{
                Toast.makeText(this,"No se ha podido leer el codigo",Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
