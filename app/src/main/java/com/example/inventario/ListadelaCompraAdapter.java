package com.example.inventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fragments.FragmentInventario;

import java.util.HashMap;
import java.util.List;

import pk.gb.useraccount.R;

public class ListadelaCompraAdapter extends BaseExpandableListAdapter {
    private HashMap<String, List<Producto>> tree ;
    private String[] cabeceraGrupo;
    RequestQueue requestQueue;
    String URLListaCompraAgregaElimina = "";
    String URLBorrar = "";

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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
