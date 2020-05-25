package com.example.inventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import pk.gb.useraccount.R;

public class InventarioAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<Producto>> tree ;
    private String[] cabeceraGrupo;
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
        textView.setText(String.valueOf(getGroup(groupPosition)));
        return convertView;
    }
//establece la vista de los productos
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        Producto p = (Producto) getChild(groupPosition,childPosition);
        TextView textViewNombre = convertView.findViewById(R.id.titulo_item);
        TextView textViewUnidades= convertView.findViewById(R.id.unidades_item);
        textViewNombre.setText(p.getNombre());
        textViewUnidades.setText(p.getUnidades());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
