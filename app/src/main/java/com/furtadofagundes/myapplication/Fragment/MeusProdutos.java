package com.furtadofagundes.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.furtadofagundes.myapplication.Activity.ItemActivity;
import com.furtadofagundes.myapplication.Singleton.Dados;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.Singleton.Usuario;
import com.furtadofagundes.myapplication.R;

import java.util.ArrayList;


public class MeusProdutos extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private final String TAG = "MeusProdutosLog";

    private OnFragmentInteractionListener mListener;
    private ListAdapter listAdapter;
    private ArrayList<Produto> meusProdutos;
    private AlertDialog alerta;

    public MeusProdutos() {
        // Required empty public constructor
    }


    public static MeusProdutos newInstance(String param1, String param2) {
        MeusProdutos fragment = new MeusProdutos();
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
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_meus_produtos, container, false);

        meusProdutos = new ArrayList<>();

        ArrayList<Produto> todos = Dados.getInstance().getProdutos();

        String id = Usuario.getInstance().getIdUser();

        if (id == null) {
            Log.i(TAG, "ID nulo");
        }

        for (Produto p : todos) {
            if (p.getIdUsuario().equals(id)) {
                meusProdutos.add(p);
            }
        }
        listView = v.findViewById(R.id.listaMeusProdutos);

        listAdapter = new com.furtadofagundes.myapplication.Adapter.ListAdapter(getContext(), R.id.listaMeusProdutos, meusProdutos);
        listView.setAdapter(listAdapter);
        listView.setOnItemLongClickListener(this);


        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(), ItemActivity.class);
        i.putExtra("Objeto", position);
        startActivity(i);

    }




    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        alerta = new AlertDialog.Builder(getContext()).setTitle("Confirma").setMessage("Voce deseja excluir o produto " + meusProdutos.get(position).getNome() + "?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Dados.getInstance().removerProduto(meusProdutos.remove(position));
                        ((ArrayAdapter) listAdapter).notifyDataSetChanged();
                        alerta.dismiss();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alerta.dismiss();
                    }
                }).show();

        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
