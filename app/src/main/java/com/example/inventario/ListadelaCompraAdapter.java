package com.example.inventario;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import pk.gb.useraccount.R;

public class ListadelaCompraAdapter extends BaseExpandableListAdapter {
    private HashMap<String, List<Producto>> tree ;
    private String[] cabeceraGrupo;
    RequestQueue requestQueue;
    String URLListaCompraAgregaElimina = "http://3.15.228.207/listaCompra/agregaOeliminaListaCompra.php";
    String URLBorrar = "http://3.15.228.207/listaCompra/eliminarProductoListaCompra.php";

    public ListadelaCompraAdapter(HashMap<String, List<Producto>> tree){
        this.tree=tree;
        cabeceraGrupo = tree.keySet().toArray(new String[0]);

    }
    @Override
    public int getGroupCount() {
        return cabeceraGrupo.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return tree.get(cabeceraGrupo[groupPosition]).size();
    }

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
        return groupPosition;    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

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

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compra,parent,false);
        final Producto p = (Producto) getChild(groupPosition,childPosition);
        TextView textViewNombre = convertView.findViewById(R.id.titulo_itemCompra);
        TextView textViewUnidades= convertView.findViewById(R.id.unidades_itemCompra);
        ImageButton buttonDelete = convertView.findViewById(R.id.deleteItemCompra);
        ImageButton buttonDeleteUnit = convertView.findViewById(R.id.button_item_deleteCompra);
        ImageButton buttonAddUnit = convertView.findViewById(R.id.button_item_addCompra);
        requestQueue = Volley.newRequestQueue(convertView.getContext());
        final View finalConvertView = convertView;
        textViewNombre.setText(p.getNombre());
        textViewUnidades.setText(p.getUnidades());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequestEliminarProd = new StringRequest(Request.Method.POST, URLBorrar, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                FragmentInventario fragmentInventario = new FragmentInventario();
                int pos = Integer.parseInt(p.getGrupo()) -1;
                tree.get(fragmentInventario.tipoGrupos[pos]).remove(childPosition);
                if (tree.get(fragmentInventario.tipoGrupos[pos]).isEmpty()) tree.keySet().remove(fragmentInventario.tipoGrupos[pos]);
                Refresh(tree);
                requestQueue.add(stringRequestEliminarProd);
            }
        });

        buttonAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(p.getUnidades()) + 1 <=99){
                    StringRequest stringRequestAgregar = new StringRequest(Request.Method.POST, URLListaCompraAgregaElimina, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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
                    requestQueue.add(stringRequestAgregar);
                }
            }
        });

        buttonDeleteUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequestEliminar = new StringRequest(Request.Method.POST, URLListaCompraAgregaElimina, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                requestQueue.add(stringRequestEliminar);

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void Refresh(HashMap<String, List<Producto>> tree){
        this.tree = tree;
        cabeceraGrupo = tree.keySet().toArray(new String[0]);
        notifyDataSetChanged();
    }
}
