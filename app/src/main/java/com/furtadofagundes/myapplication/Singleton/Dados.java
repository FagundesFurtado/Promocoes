package com.furtadofagundes.myapplication.Singleton;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.furtadofagundes.myapplication.Interface.RespostaAtualizacaoFirebase;
import com.furtadofagundes.myapplication.Interface.RespostaFirebase;
import com.furtadofagundes.myapplication.AsynkTasks.AtualizacaoFirebase;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.Model.Voto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class Dados implements RespostaAtualizacaoFirebase {
    private static final Dados ourInstance = new Dados();

    private RespostaFirebase respostaFirebase;
    private final String TAG = "Dados";
    private AtualizacaoFirebase atualizacaoFirebase;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();


    public static Dados getInstance() {
        return ourInstance;
    }

    public void setRespostaFirebase(RespostaFirebase respostaFirebase) {
        this.respostaFirebase = respostaFirebase;

    }

    private ArrayList<Produto> produtos = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    private Dados() {
        Log.i(TAG, "Iniciando listener");
        atualizacaoFirebase = new AtualizacaoFirebase(this);

    }

    public void inserirProduto(Produto produto) {
        DatabaseReference myRef = database.getReference("Produtos");


        myRef.getRef().child(Usuario.getInstance().getIdUser()).child(String.valueOf(produto.getHorario())).setValue(produto);

    }


    public Produto getItem(int position) {
        return produtos.get(position);

    }

    public void atualizaVotos(Produto produto, Voto voto){

        DatabaseReference produtoVoto = database.getReference().child("Produtos").child(produto.getIdUsuario()).child(String.valueOf(produto.getHorario()));
        Date d = new Date();
        String horario = d.getHours()+":"+d.getMinutes();


        if(voto.isPositivo()){
            produtoVoto.child("positivos").setValue(produto.getPositivos()+1);
            Date date = new Date();


            produtoVoto.child("ultimoPositivo").setValue(horario);

        }else{
            produtoVoto.child("negativos").setValue(produto.getPositivos()+1);
            Date date = new Date();


            produtoVoto.child("ultimoNegativo").setValue(horario);


        }




    }


    public ArrayList<Produto> getProdutos() {
        return produtos;
    }


    public void uploadFoto(String nome, final Produto produto) {


        File rootPath = new File(Environment.getExternalStorageDirectory(), "Promocoes/Fotos");

        File localFile = new File(rootPath, nome);


        Uri file = Uri.fromFile(localFile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference caminhoPasta = storage.getReferenceFromUrl("gs://promocoes-9e512.appspot.com/Produtos");
        StorageReference arquivo = caminhoPasta.child(nome);
        UploadTask uploadTask = arquivo.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("UploadFoto", "Deu Ruim");
                Log.i("UploadFoto", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("UploadFoto", "Sucesso");
                FotoAtual.getInstance().finalizaNotificacao();
                inserirProduto(produto);

            }
        });

    }

    public void removerProduto(Produto produto) {


        Log.i("Remover", "Produto " + produto.getNome());
        Log.i("Remover", "Produto " + produto.getHorario());


        DatabaseReference myRef = database.getReference("Produtos");

        DatabaseReference produtoReferencia = myRef.child(Usuario.getInstance().getIdUser()).child(String.valueOf(produto.getHorario()));
        Log.i("Remover", produtoReferencia.toString());
        produtoReferencia.setValue(null);


//        myRef.child(Usuario.getInstance().getIdUser()).child(String.valueOf(produto.getHorario())).removeValue();
    }


    @Override
    public void produtosAtualizados(ArrayList<Produto> produtos) {
        Log.i(TAG, "Dados recebeu notificacao da AsynkTask");
        this.produtos.clear();
        this.produtos.addAll(produtos);
        if (respostaFirebase != null) {
            Log.i(TAG, "Notificou a thread principal");
            respostaFirebase.listaAtualizada();
        }
    }
}
