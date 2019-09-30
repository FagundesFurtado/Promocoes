package com.furtadofagundes.myapplication.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.furtadofagundes.myapplication.Interface.RespostaPesquisa;
import com.furtadofagundes.myapplication.AsynkTasks.PesquisaProduto;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.R;

import java.util.ArrayList;

public class Pesquisa extends Fragment implements View.OnKeyListener, SearchView.OnQueryTextListener, RespostaPesquisa {

    private ListView lista;
    private ListAdapter listaPesquisa;
    private ArrayList<Produto> resultado;
    private SearchView searchView;
    private Toast toast;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pesquisa, container, false);


        searchView = view.findViewById(R.id.pesquisa);

        NestedScrollView nestedScrollView = view.findViewById(R.id.lista2);
        resultado = new ArrayList<>();
        listaPesquisa = new com.furtadofagundes.myapplication.Adapter.ListAdapter(getContext(), R.id.lista, resultado);

        lista = view.findViewById(R.id.lista);
        ViewCompat.setNestedScrollingEnabled(lista, true);

        lista.setAdapter(listaPesquisa);

        searchView.setOnKeyListener(this);
        searchView.setOnQueryTextListener(this);


        return view;
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {


        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (!query.equals("")) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
            PesquisaProduto pesquisaProduto = new PesquisaProduto(this, query);
        }
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {


        return false;
    }

    @Override
    public void listaProdutos(final ArrayList<Produto> produtos) {
        resultado.clear();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultado.addAll(produtos);
                ((ArrayAdapter) listaPesquisa).notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });


    }


}
