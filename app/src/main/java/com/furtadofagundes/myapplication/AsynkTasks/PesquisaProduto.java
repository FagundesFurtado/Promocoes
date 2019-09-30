package com.furtadofagundes.myapplication.AsynkTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.furtadofagundes.myapplication.Interface.RespostaPesquisa;
import com.furtadofagundes.myapplication.Singleton.Dados;
import com.furtadofagundes.myapplication.Model.Produto;

import java.util.ArrayList;

public class PesquisaProduto extends AsyncTask {

    private final String TAG = "PesquisaProduto";
    private RespostaPesquisa respostaPesquisa;
    private String pesquisa;

    public PesquisaProduto(RespostaPesquisa respostaPesquisa, String pesquisa) {
        this.respostaPesquisa = respostaPesquisa;
        this.pesquisa = pesquisa;
        this.execute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i(TAG, "Iniciando Pesquisa");
        ArrayList<Produto> resposta = new ArrayList<>();

        for(Produto p : Dados.getInstance().getProdutos()){
            Log.i("TesteDesempenho", "Produto pesquisa " +p.getNome());
            if(p.getNome().toLowerCase().contains(pesquisa.toLowerCase())){
                resposta.add(p);
            }

        }


        respostaPesquisa.listaProdutos(resposta);



        return null;
    }
}
