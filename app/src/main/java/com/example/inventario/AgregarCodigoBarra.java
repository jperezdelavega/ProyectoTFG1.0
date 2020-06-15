package com.example.inventario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pk.gb.useraccount.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fragments.FragmentFormularioItem;
import com.example.fragments.FragmentInventario;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarCodigoBarra extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String URLcomprobarCodigo = "http://3.15.228.207/codigoBarras/valoresDespensaConCodigoAnadir.php";
    String URLIntroducir = "http://3.15.228.207/codigoBarras/anadirProductoCodigoBarra.php";
    String valorTipo;
    String esta = "false";
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
            if(result.getContents() != null){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                final View view = layoutInflater.inflate(R.layout.dialog,null);
                TextView barCode = view.findViewById(R.id.CodigoDialog);
                final EditText nombreProd = view.findViewById(R.id.nombreDialog);
                final TextView unidadesDespensa = view.findViewById(R.id.unidadesEnDespensaDialog);
                final EditText unidadesProd = view.findViewById(R.id.CampoUnidadesDialog);
                Button btnCancelar = view.findViewById(R.id.cancelarDialog);
                Button btnAceptar = view.findViewById(R.id.AceptarDialog);
                FragmentInventario fragmentInventario = new FragmentInventario();
                final FragmentFormularioItem fragmentFormularioItem = new FragmentFormularioItem();
                final Spinner spinner = view.findViewById(R.id.spinnerDialog);


                //Creamos un adaptador para el desplegable de los tipos de productos
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, fragmentInventario.tipoGrupos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
                unidadesProd.setText("1");

                barCode.setText("Código de barras: "+result.getContents());
                alertDialog.setView(view);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                //Nos traemos el nombre del producto
                StringRequest stringRequestBuscarNombre = new StringRequest(Request.Method.POST, URLcomprobarCodigo, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getString("nombre").equals("null"))
                            nombreProd.setText(jsonObject.getString("nombre"));
                            if (!jsonObject.getString("unidades").equals("null"))
                            unidadesDespensa.setText("Unidades en despensa: "+jsonObject.getString("unidades"));
                            if (!jsonObject.getString("grupo").equals("null")){
                                int pos = Integer.parseInt(jsonObject.getString("grupo")) -1;
                                esta = "true";
                                spinner.setSelection(pos);
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
                requestQueue.add(stringRequestBuscarNombre);

                //Establecemos los botones aceptar y cancelar
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequestAceptar = new StringRequest(Request.Method.POST, URLIntroducir, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(),"Producto añadido",Toast.LENGTH_LONG);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG);
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("codigo",result.getContents());
                                params.put("nombre",nombreProd.getText().toString());
                                params.put("unidades",unidadesProd.getText().toString());
                                params.put("grupo", fragmentFormularioItem.obtenerIntGrupo(valorTipo));
                                params.put("esta",esta);
                                SharedPreferences preferences = view.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                                String user = preferences.getString("user","null");
                                params.put("email",user);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequestAceptar);
                    }
                });

            }else{
                Toast.makeText(this,"No se ha podido leer el codigo",Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        valorTipo = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        valorTipo = "Otros";
    }
}
