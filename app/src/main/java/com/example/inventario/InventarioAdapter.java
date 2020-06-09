package com.example.inventario;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fragments.FragmentInventario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import pk.gb.useraccount.R;

public class InventarioAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<Producto>> tree ;
    private String[] cabeceraGrupo;
    String URL = "http://3.15.228.207/inventario/anadirOeliminarUnidades.php";
    String URLDelete = "http://3.15.228.207/inventario/eliminarProductoManual.php";
    RequestQueue requestQueue;
//Inicializa el arbol donde se guarda key = nombre del grupo, values = productos del grupo
    public InventarioAdapter(HashMap<String, List<Producto>> tree) {
        this.tree = tree;
        cabeceraGrupo = tree.keySet().toArray(new String[0]);
    }
// Cuenta el numero de titulos de cabecera que hay
    @Override
    public int getGroupCount() {
        return cabeceraGrupo.length;
    }
//Cuenta el numero de objetos que hay por cabecera
    @Override
    public int getChildrenCount(int groupPosition) {
        return tree.get(cabeceraGrupo[groupPosition]).size();
    }
//Obtiene el nombre de la cabecera del grupo
    @Override
    public Object getGroup(int groupPosition) {
        return cabeceraGrupo[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tree.get(cabeceraGrupo[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
//Establece la vista de la cabecera de los grupos
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grupos,parent,false);
        TextView textView = convertView.findViewById(R.id.cabecera_grupo);
        FragmentInventario fragmentInventario = new FragmentInventario();
        ImageView imagen = convertView.findViewById(R.id.imgGrupos);
        imagen.setImageResource(fragmentInventario.buscaIdImagen(String.valueOf(getGroup(groupPosition))));
        textView.setText(String.valueOf(getGroup(groupPosition)));
        return convertView;
    }
//establece la vista de los productos
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        final Producto p = (Producto) getChild(groupPosition,childPosition);
        TextView textViewNombre = convertView.findViewById(R.id.titulo_item);
        TextView textViewUnidades= convertView.findViewById(R.id.unidades_item);
        ImageButton buttonEdit = convertView.findViewById(R.id.edit);
        ImageButton buttonDelete = convertView.findViewById(R.id.deleteItem);
        ImageButton buttonDeleteUnit = convertView.findViewById(R.id.button_item_delete);
        ImageButton buttonAddUnit = convertView.findViewById(R.id.button_item_add);
        requestQueue = Volley.newRequestQueue(convertView.getContext());
        final View finalConvertView = convertView;
        textViewNombre.setText(p.getNombre());
        textViewUnidades.setText(p.getUnidades());

//Boton de agregar unidades configurado
        buttonAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(finalConvertView.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = finalConvertView.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("nombre",p.getNombre());
                        params.put("accion","agrega");
                        return params;
                    }
                };
                int unidadAct = Integer.parseInt(p.getUnidades()) + 1;
                p.setUnidades(Integer.toString(unidadAct));
                Refresh(tree);
                requestQueue.add(stringRequest);
            }
        });
//Boton eliminar unidades configurado
        buttonDeleteUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(finalConvertView.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = finalConvertView.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("nombre",p.getNombre());
                        params.put("accion","elimina");
                        return params;
                    }
                };
                int unidadAct = Integer.parseInt(p.getUnidades()) - 1;
                p.setUnidades(Integer.toString(unidadAct));
                Refresh(tree);
                requestQueue.add(stringRequest);
            }
        });
        //Boton de establecer numero de unidades limite configurado
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Boton de borrar producto configurado
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDelete, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(finalConvertView.getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        SharedPreferences preferences = finalConvertView.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                        String user = preferences.getString("user","null");
                        params.put("email",user);
                        params.put("nombre",p.getNombre());
                        return params;
                    }
                };
                Refresh(tree);
                requestQueue.add(stringRequest);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void Refresh(HashMap<String, List<Producto>> tree){
        this.tree = tree;
        cabeceraGrupo = tree.keySet().toArray(new String[0]);
        notifyDataSetChanged();
    }
}
