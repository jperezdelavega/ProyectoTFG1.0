package com.example.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventario.InventarioAdapter;
import com.example.inventario.Producto;
import com.example.menu.MenuActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.SortedList;
import pk.gb.useraccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInventario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInventario extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RequestQueue requestQueue;
    HashMap<String, List<Producto>> listaGlobal ;
    InventarioAdapter inventarioAdapter;
    ProgressBar progressBar;
    public String[] tipoGrupos = {"Verdura","Fruta","Lácteos","Carne","Pescado","Droguería","Cereales","Pasta","Legumbres","Snacks","Conservas","Bollería","Bebidas","Congelados","Aliños","Otros"};
    FloatingActionButton addButton;
    FragmentFormularioItem fragmentFormularioItem;
    public FragmentInventario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInventario.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInventario newInstance(String param1, String param2) {
        FragmentInventario fragment = new FragmentInventario();
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
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);
        addButton = view.findViewById(R.id.botonAnadirInventario);
        addButton.setVisibility(View.GONE);
        ExpandableListView listView = view.findViewById(R.id.ListaGruposExpansible);
        fragmentFormularioItem = new FragmentFormularioItem();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuActivity menuActivity = (MenuActivity) getActivity();
                menuActivity.onClick(v);
            }
        });
        listaGlobal = new HashMap<>();
        inventarioAdapter = new InventarioAdapter(listaGlobal);
        listView.setAdapter(inventarioAdapter);
        crearInventario(view);


        Log.d("menu","entra en el menu");

        return view;
    }

    private void crearInventario(View view) {
        requestQueue = Volley.newRequestQueue(getContext());
        String URL = "http://3.15.228.207/inventario/cargarInventario.php";
        progressBar = view.findViewById(R.id.progressBar);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Producto> lista = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayProd = obj.getJSONArray("productos");
                    for(int i = 0;i<arrayProd.length();i++){
                        Producto p = new Producto();
                        p.setNombre(arrayProd.getJSONObject(i).getString("nombre"));
                        p.setCod_barras(arrayProd.getJSONObject(i).getString("codigo_barra"));
                        p.setGrupo(arrayProd.getJSONObject(i).getString("id_grupo"));
                        p.setUnidades(arrayProd.getJSONObject(i).getString("unidades"));
                        p.setUnidadeLimite(arrayProd.getJSONObject(i).getString("unidades_limite"));
                        lista.add(p);
                    }
                    agregaACadaGrupo(lista);
                    inventarioAdapter.Refresh(listaGlobal);
                    progressBar.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                SharedPreferences preferences = getContext().getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                String user = preferences.getString("user","null");
                map.put("email",user);
                return map;
            }
        };
    requestQueue.add(stringRequest);

    }

    private void agregaACadaGrupo(List<Producto> lista) {
        for(Producto p : lista){
            String key = tipoGrupos[Integer.parseInt(p.getGrupo())-1];
            if (listaGlobal.containsKey(key)){
                List<Producto> listaAModificar = listaGlobal.get(key);
                listaAModificar.add(p);
                listaGlobal.put(key,listaAModificar);
            }else{
                List<Producto> listaAIntroducir = new ArrayList<>();
                listaAIntroducir.add(p);
                listaGlobal.put(key,listaAIntroducir);
            }
        }

    }

    public int buscaIdImagen(String key) {
        int valor =0;
        switch (key){
            case "Verdura":
            valor = R.drawable.ic_verdura;
                break;
            case "Fruta":
            valor = R.drawable.ic_fruta;
                break;
            case "Lácteos":
                valor = R.drawable.ic_lacteos;
                break;
            case "Carne":
                valor = R.drawable.ic_carne;
                break;
            case "Pescado":
                valor = R.drawable.ic_pescado;
                break;
            case "Droguería":
                valor = R.drawable.ic_drogueria;
                break;
            case "Cereales":
                valor = R.drawable.ic_cereales;
                break;
            case "Pasta":
                valor = R.drawable.ic_pasta;
                break;
            case "Legumbres":
                valor = R.drawable.ic_legumbre;
                break;
            case "Snacks":
                valor = R.drawable.ic_snack;
                break;
            case "Conservas":
                valor = R.drawable.ic_conservas;
                break;
            case "Bollería":
                valor = R.drawable.ic_bolleria;
                break;
            case "Bebidas":
                valor = R.drawable.ic_bebidas;
                break;
            case "Congelados":
                valor = R.drawable.ic_congelados;
                break;
            case "Aliños":
                valor = R.drawable.ic_especias;
                break;
            case "Otros":
                valor = R.drawable.ic_otros;
                break;
        }
        return valor;
    }

}
