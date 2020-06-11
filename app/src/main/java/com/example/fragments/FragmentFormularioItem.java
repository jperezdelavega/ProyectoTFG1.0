package com.example.fragments;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import pk.gb.useraccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFormularioItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFormularioItem extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentFormularioItem() {
        // Required empty public constructor
    }

    EditText campoNombre,campoUnidades,campoUnidadesLimites;
    Spinner spinner;
    Button bttnadd;
    String valorTipo;
    String URL = "http://3.15.228.207/inventario/anadirProductoManual.php";
    FragmentInventario fragmentInventario;
    RequestQueue requestQueue ;

    // TODO: Rename and change types and number of parameters
    public static FragmentFormularioItem newInstance(String param1, String param2) {
        FragmentFormularioItem fragment = new FragmentFormularioItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Declaración de las variables
        View view = inflater.inflate(R.layout.fragment_formulario_item, container, false);
        campoNombre = view.findViewById(R.id.campo_nombreProducto);
        campoUnidades = view.findViewById(R.id.campo_unidades);
        campoUnidadesLimites = view.findViewById(R.id.campo_UnidadesLimite);
        spinner = view.findViewById(R.id.campo_tipoProducto);
        bttnadd = view.findViewById(R.id.botonAnadirForm);
        fragmentInventario = new FragmentInventario();
        requestQueue = Volley.newRequestQueue(getContext());

        //Creamos un adaptador para el desplegable de los tipos de productos
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, fragmentInventario.tipoGrupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        bttnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Aqui estaría bien mejorar la navegacion y volver al fragment del inventario, pero no se como hacerlo
                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("nombre",campoNombre.getText().toString());
                        params.put("unidades",campoUnidades.getText().toString());
                        params.put("unidadesLimite",campoUnidadesLimites.getText().toString());
                        params.put("grupo",obtenerIntGrupo(valorTipo));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }

        });

        return view;
    }

    public String obtenerIntGrupo(String valorTipo) {
        int resultado = 0;
        fragmentInventario = new FragmentInventario();
        for (int i=0;i<fragmentInventario.tipoGrupos.length;i++){
            if(valorTipo.equals(fragmentInventario.tipoGrupos[i])) resultado = i+1;
        }
        return Integer.toString(resultado);
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
