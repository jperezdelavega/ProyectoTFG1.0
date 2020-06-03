package com.example.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

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

    EditText campoNombre,campoCodigo,campoUnidades,campoUnidadesLimites;
    Spinner spinner;
    Button bttnadd;
    String valorTipo;

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
        FragmentInventario fragmentInventario = new FragmentInventario();

        //Creamos un adaptador para el desplegable de los tipos de productos
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, fragmentInventario.tipoGrupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        return view;
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
