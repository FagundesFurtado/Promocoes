package com.furtadofagundes.myapplication.Singleton;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.furtadofagundes.myapplication.Interface.RespostaFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class FotosBanco {
    private static final FotosBanco ourInstance = new FotosBanco();

    public static FotosBanco getInstance() {
        return ourInstance;
    }

    private final String TAG = "FotosBanco";

    private ArrayList<Drawable> fotos = new ArrayList<>();
    private HashMap<String, Integer> hashMap = new HashMap<>();
    private RespostaFirebase respostaUsuario;

    private FotosBanco() {
    }


    public Drawable getImagem(String imagem) {
        if (hashMap.containsKey(imagem)) {
            Log.i(TAG, "Imagem existe " + imagem);
            int posicao = hashMap.get(imagem);

            return fotos.get(posicao);
        }

        int pos =carregaImagemDoDisco(imagem);
        if(pos != -1){
            return fotos.get(pos);

        }
        return null;
    }

    public void setRespostaUsuario(RespostaFirebase respostaUsuario) {
        this.respostaUsuario = respostaUsuario;
    }

    private int carregaImagemDoDisco(String nomeFoto) {

        Log.i(TAG, "Carregando do disco " + nomeFoto);

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Produtos");
        File localFile = new File(rootPath, nomeFoto);

        if (!localFile.exists()) {
            downloadImagens(nomeFoto);
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(localFile);
                Drawable imagem = Drawable.createFromStream(fis, null);
                int posicao = fotos.size();

                fotos.add(imagem);
                hashMap.put(nomeFoto, posicao);

                return posicao;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    return -1;

    }


    private void downloadImagens(String nomeFoto) {

        Log.i(TAG, "Nao existe no disco");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://promocoes-9e512.appspot.com/Produtos");
        StorageReference islandRef = storageRef.child(nomeFoto);

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Produtos");

        rootPath.mkdirs();


        final File localFile = new File(rootPath, nomeFoto);
        Log.i(TAG, "Baixando arquivo");
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, ";local tem file created  created " + localFile.toString());
                Log.i(TAG, "Finalizou download");
                respostaUsuario.listaAtualizada();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i(TAG, ";local tem file not created  created " + exception.toString());
            }
        });


    }


}
