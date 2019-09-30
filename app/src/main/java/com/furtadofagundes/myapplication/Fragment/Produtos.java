package com.furtadofagundes.myapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.furtadofagundes.myapplication.Interface.RespostaFirebase;
import com.furtadofagundes.myapplication.Activity.ItemActivity;
import com.furtadofagundes.myapplication.Singleton.Dados;
import com.furtadofagundes.myapplication.Singleton.FotosBanco;
import com.furtadofagundes.myapplication.R;

public class Produtos extends Fragment implements AdapterView.OnItemClickListener, RespostaFirebase {

    private static final String TAG = "Produtos";
    private ListView lista;
    private SwipeRefreshLayout refreshLayout;
    private ListAdapter listAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produtos, container, false);
        lista = view.findViewById(R.id.lista);


        Log.i("TesteDesempenho", "Produto On Create");
        listAdapter = new com.furtadofagundes.myapplication.Adapter.ListAdapter(getContext(), R.id.lista, Dados.getInstance().getProdutos());

        lista.setAdapter(listAdapter);
        lista.setOnItemClickListener(this);
        Dados.getInstance().setRespostaFirebase(this);
        refreshLayout = view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshLayout.setRefreshing(false);
                ((ArrayAdapter)listAdapter).notifyDataSetChanged();
            }
        });

        FotosBanco.getInstance().setRespostaUsuario(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TesteDesempenho", "Produto On Resume");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("ProdutosFragment", "Cliquei no item " + position);


        Intent i = new Intent(getContext(), ItemActivity.class);
        i.putExtra("Objeto", position);
        startActivity(i);


    }

    @Override
    public void listaAtualizada() {
        Log.i(TAG, "Lista Atualizada");




        listAdapter = new com.furtadofagundes.myapplication.Adapter.ListAdapter(getContext(), R.id.lista, Dados.getInstance().getProdutos());

        ((ArrayAdapter)(listAdapter)).notifyDataSetChanged();

    }





}
