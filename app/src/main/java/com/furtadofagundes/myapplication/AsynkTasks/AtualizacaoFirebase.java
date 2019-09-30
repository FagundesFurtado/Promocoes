package com.furtadofagundes.myapplication.AsynkTasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;


import com.furtadofagundes.myapplication.Interface.RespostaAtualizacaoFirebase;
import com.furtadofagundes.myapplication.Model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AtualizacaoFirebase extends AsyncTask implements Comparator<Produto> {
    private final String TAG = "AtualizacaoFirebase";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Produto> produtos = new ArrayList<>();
    private RespostaAtualizacaoFirebase listaAtualizada;
    private Comparator comparator;

    public AtualizacaoFirebase(RespostaAtualizacaoFirebase listaAtualizada) {
        this.listaAtualizada = listaAtualizada;
        comparator = this;
        this.execute();
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i(TAG, "Atualizacao");
        DatabaseReference myRef = database.getReference("Produtos");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot d : ds.getChildren()) {
                        Produto novo = d.getValue(Produto.class);
                        produtos.add(novo);
                        Log.i(TAG, novo.toString());
                        downloadImagens(novo);
                        Log.i("Desistindo", "Chegou aqui");
                    }
                }

                Collections.sort(produtos, comparator);

                listaAtualizada.produtosAtualizados(produtos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    private void downloadImagens(Produto produto) {

        Log.i("Desistindo", "fazendo download");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://promocoes-9e512.appspot.com/Produtos");
        StorageReference islandRef = storageRef.child(produto.getCaminhoFotoDownload());

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Produtos");
        if (!rootPath.exists()) {
            Log.i("Desistindo", "Arquivo nao existe");
            rootPath.mkdirs();
        }


        final File localFile = new File(rootPath, produto.getCaminhoFoto());

        if (!localFile.exists()) {
            Log.i("Desistindo", "Baixando arquivo");
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Desistindo", ";local tem file created  created " + localFile.toString());
                    Log.i("Desistindo", "Finalizou download");
                    listaAtualizada.produtosAtualizados(produtos);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("Desistindo", ";local tem file not created  created " + exception.toString());
                }
            });
        } else {
            Log.i("Desistindo", "Arquivo ja existe");
        }
    }


    @Override
    public int compare(Produto p1, Produto p2) {
        int total1 = p1.getPositivos() - p1.getNegativos();
        int total2 = p2.getPositivos() - p2.getNegativos();
        if (total1 > total2) {
            return -1;
        }
        return 1;
    }
}
