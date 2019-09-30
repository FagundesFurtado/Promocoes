package com.furtadofagundes.myapplication.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.furtadofagundes.myapplication.Main.MainActivity;
import com.furtadofagundes.myapplication.Singleton.Dados;
import com.furtadofagundes.myapplication.Singleton.FotoAtual;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.Singleton.Usuario;
import com.furtadofagundes.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class ConfirmaEnvio extends AppCompatActivity implements View.OnClickListener {


    private final String TAG = "ConfirmaEnvio";
    private ImageView imageView;
    private EditText produto;
    private EditText preco;
    private EditText local;
    private Button confirma;
    private String nomeFoto;
    private Button voltar;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_envio);
        Intent i = getIntent();

        nomeFoto = i.getExtras().getString("NomeArquivo");


        imageView = findViewById(R.id.foto);

        setImagem();
        produto = findViewById(R.id.produto);
        preco = findViewById(R.id.preco);
        local = findViewById(R.id.local);
        confirma = findViewById(R.id.confirma);
        confirma.setOnClickListener(this);

        voltar = findViewById(R.id.cancelar);
        voltar.setOnClickListener(this);
        location = FotoAtual.getInstance().getLocation();

    }

    @Override
    public void onClick(View v) {
        if (v == confirma) {
            if (!preco.getText().toString().equals("") && !produto.getText().toString().equals("") && !local.getText().toString().equals("")) {
                Toast.makeText(this, "Produto cadastrado", Toast.LENGTH_SHORT).show();

                String nomeArquivo = nomeFoto;
                Double lat = 0.0;
                Double log = 0.0;
                if (location != null) {
                    lat = location.getLatitude();
                    log = location.getLongitude();
                    Log.i("PedidoConfirmado", location.getLatitude() + " " + location.getLongitude());

                }

                Date date = new Date();
                Produto novoProduto = new Produto(produto.getText().toString(), Double.parseDouble(preco.getText().toString()), local.getText().toString(), lat, log, nomeArquivo, nomeArquivo, date.getTime(), Usuario.getInstance().getIdUser());

                Dados.getInstance().uploadFoto(nomeFoto, novoProduto);
                FotoAtual.getInstance().inicializaNotificacao(this, R.drawable.ic_home_black_24dp);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);

//                Dados.getInstance().uploadImage(nomeFoto,this);
                //Realiza Pedido

            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == voltar) {

            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

}
