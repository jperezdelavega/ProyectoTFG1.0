package com.example.inventario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.dialog.InsetDialogOnTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
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
    String URLEdit = "http://3.15.228.207/inventario/editarProducto.php";
    RequestQueue requestQueue;
    private String valorTipo;

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
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
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
        //Boton de editar el producto
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(parent.getContext());
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog,null);
                final TextView barCode = view.findViewById(R.id.CodigoDialog);
                final TextView unidadesDespensa = view.findViewById(R.id.unidadesEnDespensaDialog);
                final TextView textoUnidadesLimite = view.findViewById(R.id.unidadesDialog);
                final EditText nombreProd = view.findViewById(R.id.nombreDialog);
                final EditText unidadesProd = view.findViewById(R.id.CampoUnidadesDialog);
                Button btnCancelar = view.findViewById(R.id.cancelarDialog);
                Button btnAceptar = view.findViewById(R.id.AceptarDialog);
                final FragmentInventario fragmentInventario = new FragmentInventario();

                final Spinner spinner = view.findViewById(R.id.spinnerDialog);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, fragmentInventario.tipoGrupos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        valorTipo = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        valorTipo = "otros";
                    }
                });
                final String unidadesLimite = (p.getUnidadeLimite().equals("")) ? "0" : p.getUnidadeLimite();
                unidadesDespensa.setText("Unidades límite acltuales: "+ unidadesLimite);
                barCode.setText("Codigo de barras: "+ p.getCod_barras());
                textoUnidadesLimite.setText("Unidades límite: ");
                unidadesProd.setText(p.getUnidadeLimite());
                nombreProd.setText(p.getNombre());
                spinner.setSelection(Integer.parseInt(p.getGrupo())-1);

                alertDialog.setView(view);
                final AlertDialog dialog = alertDialog.create();

                //Establecemos el boton de cancelar del dialog
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //Boton de aceptar del dialog
                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nombreProd.getText().toString().matches("") || unidadesProd.getText().toString().matches("")){
                            AlertDialog.Builder error = new AlertDialog.Builder(parent.getContext());
                            error.setTitle("Error");
                            error.setMessage("Todos los campos tienen que estar completos");
                            error.create().show();
                        }else{
                            StringRequest stringRequestEdit = new StringRequest(Request.Method.POST, URLEdit, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    SharedPreferences preferences = parent.getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                                    String user = preferences.getString("user","null");
                                    params.put("email",user);
                                    params.put("codigo", p.getCod_barras());
                                    params.put("nombre", nombreProd.getText().toString());
                                    List array = Arrays.asList(fragmentInventario.tipoGrupos);
                                    params.put("grupo",Integer.toString(array.indexOf(valorTipo) + 1 ));
                                    params.put("unidades_limite",unidadesProd.getText().toString());
                                    return params;
                                }
                            };
                            List array = Arrays.asList(fragmentInventario.tipoGrupos);
                            p.setUnidadeLimite(unidadesProd.getText().toString());
                            p.setNombre(nombreProd.getText().toString());
                            int pos = Integer.parseInt(p.getGrupo()) -1;
                            tree.get(fragmentInventario.tipoGrupos[pos]).remove(childPosition);
                            if (tree.get(fragmentInventario.tipoGrupos[pos]).isEmpty()) tree.keySet().remove(fragmentInventario.tipoGrupos[pos]);

                            if(tree.keySet().contains(valorTipo)){
                                tree.get(valorTipo).add(p);
                            }else{
                                List<Producto> lista = new ArrayList<>();
                                lista.add(p);
                                tree.put(valorTipo,lista);
                            }
                            p.setGrupo(Integer.toString(array.indexOf(valorTipo) + 1 ));
                            Refresh(tree);
                            requestQueue.add(stringRequestEdit);

                        }
                    }
                });
                dialog.show();

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
                FragmentInventario fragmentInventario = new FragmentInventario();
                int pos = Integer.parseInt(p.getGrupo()) -1;
                tree.get(fragmentInventario.tipoGrupos[pos]).remove(childPosition);
                if (tree.get(fragmentInventario.tipoGrupos[pos]).isEmpty()) tree.keySet().remove(fragmentInventario.tipoGrupos[pos]);
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
