package com.furtadofagundes.myapplication.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.furtadofagundes.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConfirmacaoFoto extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "ConfirmaFoto";
    private ImageView imageView;
    private FloatingActionButton confirma;
    private FloatingActionButton delete;
    private String nomeFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_foto);


        imageView = findViewById(R.id.imagem);
        confirma = findViewById(R.id.confirma);
        delete = findViewById(R.id.delete);
        Log.i(TAG, "Confirma foto");


        nomeFoto = getIntent().getExtras().getString("NomeArquivo").toString();


        setImagem();

        confirma.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void setImagem() {
        File rootPath = new File(Environment.getExternalStorageDirectory(), "Promocoes/Fotos");

        File localFile = new File(rootPath, nomeFoto);
        Log.i(TAG, localFile.getAbsolutePath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Drawable buttonBg = Drawable.createFromStream(fis, null);
        imageView.setImageDrawable(buttonBg);

        imageView.setRotation(90);

    }

    private void deleteFile() {
        File rootPath = new File(Environment.getExternalStorageDirectory(), "Promocoes/Fotos");
        File localFile = new File(rootPath, nomeFoto);

        boolean result = localFile.delete();
        Log.i(TAG, "Delete file " + result);
    }

    @Override
    public void onClick(View v) {
        if (v == confirma) {
            Intent i = new Intent(this, ConfirmaEnvio.class);
            i.putExtra("NomeArquivo", nomeFoto);
            startActivity(i);
        }

        if (v == delete) {
            deleteFile();
            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);

        }
    }
}
