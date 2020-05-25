package com.example.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.inventario.InventarioAdapter;
import com.example.inventario.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import pk.gb.useraccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInventario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInventario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        ExpandableListView listView = view.findViewById(R.id.ListaGruposExpansible);
        HashMap<String, List<Producto>> lista = new HashMap<>();

        ArrayList<Producto> listaProductosGrupo1 = new ArrayList<>();
        Producto p1 = new Producto("Patatas","12","easda");
        Producto p2 = new Producto("Macarrones","5","easda");
        Producto p3 = new Producto("Tomate","4","easda");
        Producto p4 = new Producto("Spagueti","3","easda");
        listaProductosGrupo1.add(p1);
        listaProductosGrupo1.add(p2);
        ArrayList<Producto> listaProductosGrupo2 = new ArrayList<>();
        listaProductosGrupo2.add(p3);
        listaProductosGrupo2.add(p4);
        lista.put("Vegetales",listaProductosGrupo1);
        lista.put("Otros",listaProductosGrupo2);

        InventarioAdapter inventarioAdapter = new InventarioAdapter(lista);
        listView.setAdapter(inventarioAdapter);
        return view;
    }
}
