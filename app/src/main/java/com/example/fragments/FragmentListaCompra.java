package com.example.fragments;

import android.app.AlertDialog;
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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventario.ListadelaCompraAdapter;
import com.example.inventario.Producto;
import com.example.menu.MenuActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import pk.gb.useraccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaCompra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaCompra extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FloatingActionMenu addButton;
    HashMap<String, List<Producto>> listaGlobal ;
    ListadelaCompraAdapter listadelaCompraAdapter;
    private String mParam1;
    private String mParam2;
    private RequestQueue requestQueue;
    String URLCargaListaCompra = "http://3.15.228.207/listaCompra/cargarListaCompra.php";
    String URLAgrega = "http://3.15.228.207/listaCompra/anadirListaCompra.php";
    String URLCompleta = "http://3.15.228.207/listaCompra/completarListaCompra.php";
    String URLBorrar = "http://3.15.228.207/listaCompra/borrarListaCompra.php";
    final FragmentInventario fragmentInventario = new FragmentInventario();
    String valorTipo;
    AlertDialog alertDialog;
    public FragmentListaCompra() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListaCompra.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListaCompra newInstance(String param1, String param2) {
        FragmentListaCompra fragment = new FragmentListaCompra();

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_compra, container, false);
        addButton = view.findViewById(R.id.botonActualizarLista);
        addButton.setVisibility(View.GONE);

        FloatingActionButton bttnDialog = view.findViewById(R.id.agregarAListaForm);
        FloatingActionButton btnnRellenar = view.findViewById(R.id.autocompletarListaCommpra);
        FloatingActionButton btnBorrar = view.findViewById(R.id.borrarListaCompra);
        ExpandableListView listView = view.findViewById(R.id.ListaCompraExpandible);

        listaGlobal = new HashMap<>();
        listadelaCompraAdapter = new ListadelaCompraAdapter(listaGlobal);
        listView.setAdapter(listadelaCompraAdapter);
        crearListadelaCompra(view);

        bttnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View vdialog = inflater.inflate(R.layout.dialog_formlista,null);
                builder.setView(vdialog);
                alertDialog = builder.create();
                alertDialog.show();
                agregarElementoALista(vdialog);
            }
        });

        btnnRellenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompletarLista();
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarListaCompra();
            }
        });
        return view;
    }

    private void borrarListaCompra() {

        StringRequest stringRequestBorrar = new StringRequest(Request.Method.POST, URLBorrar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaGlobal.clear();
                listadelaCompraAdapter.Refresh(listaGlobal);
                addButton.close(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        requestQueue.add(stringRequestBorrar);
    }

    private void autoCompletarLista() {
        StringRequest stringRequestCompletar = new StringRequest(Request.Method.POST, URLCompleta, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Producto> lista = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("vacia").equals("false")){
                        JSONArray arrayProd = obj.getJSONArray("productos");
                        for(int i = 0;i<arrayProd.length();i++){
                            Producto p = new Producto();
                            p.setNombre(arrayProd.getJSONObject(i).getString("nombre"));
                            p.setGrupo(arrayProd.getJSONObject(i).getString("grupo"));
                            p.setUnidades(arrayProd.getJSONObject(i).getString("unidades"));
                            lista.add(p);
                        }
                        agregaACadaGrupo(lista);
                        listadelaCompraAdapter.Refresh(listaGlobal);

                    }
                    addButton.close(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        requestQueue.add(stringRequestCompletar);
    }

    private void agregarElementoALista(View vdialog) {
        final EditText nombreProd = vdialog.findViewById(R.id.nombreDialogLista);
        final EditText uds = vdialog.findViewById(R.id.CampoUnidadesDialogLista);
        Button btnnAceptar = vdialog.findViewById(R.id.AceptarDialogLista);
        Button bttnCancelar = vdialog.findViewById(R.id.cancelarDialogLista);
        Spinner spinner = vdialog.findViewById(R.id.spinnerDialogLista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, fragmentInventario.tipoGrupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valorTipo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                valorTipo = "Otros";
            }
        });
        bttnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mandamos al server el producto que queremos agregar en la lista de la compra
                StringRequest stringRequestAgregar = new StringRequest(Request.Method.POST, URLAgrega, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT);
                        Producto p = new Producto();
                        p.setNombre(nombreProd.getText().toString());
                        p.setUnidades(uds.getText().toString());
                        if(listaGlobal.keySet().contains(valorTipo)){
                            listaGlobal.get(valorTipo).add(p);
                            listadelaCompraAdapter.Refresh(listaGlobal);
                        }else {
                            ArrayList<Producto> lista = new ArrayList<>();
                            lista.add(p);
                            listaGlobal.put(valorTipo,lista);
                            listadelaCompraAdapter.Refresh(listaGlobal);
                        }
                        alertDialog.dismiss();
                        addButton.close(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("nombre",nombreProd.getText().toString());
                        params.put("unidades", uds.getText().toString());
                        List arrayTipo = Arrays.asList(fragmentInventario.tipoGrupos);
                        params.put("grupo",Integer.toString(arrayTipo.indexOf(valorTipo)+1));
                        return params;
                    }
                };
                requestQueue.add(stringRequestAgregar);
            }
        });

    }

    private void crearListadelaCompra(View view) {
        requestQueue = Volley.newRequestQueue(getContext());
        final ProgressBar progressBar = view.findViewById(R.id.progressBarCompra);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLCargaListaCompra, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Producto> lista = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arrayProd = obj.getJSONArray("productos");
                    for(int i = 0;i<arrayProd.length();i++){
                        Producto p = new Producto();
                        p.setNombre(arrayProd.getJSONObject(i).getString("nombre"));
                        p.setGrupo(arrayProd.getJSONObject(i).getString("grupo"));
                        p.setUnidades(arrayProd.getJSONObject(i).getString("unidades"));
                        lista.add(p);
                    }
                    agregaACadaGrupo(lista);
                    listadelaCompraAdapter.Refresh(listaGlobal);
                    progressBar.setVisibility(View.GONE);
                    addButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                String user = preferences.getString("user","null");
                params.put("email",user);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void agregaACadaGrupo(List<Producto> lista) {
        for(Producto p : lista){
            String key = fragmentInventario.tipoGrupos[Integer.parseInt(p.getGrupo())-1];
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
}
