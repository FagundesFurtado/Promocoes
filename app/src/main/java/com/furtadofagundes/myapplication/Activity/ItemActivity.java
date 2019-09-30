package com.furtadofagundes.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.furtadofagundes.myapplication.Singleton.Dados;
import com.furtadofagundes.myapplication.Singleton.FotosBanco;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.Model.Voto;
import com.furtadofagundes.myapplication.Singleton.Votos;
import com.furtadofagundes.myapplication.R;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "ItemActivity";
    private Produto produto;

    private ImageButton positivo;
    private ImageButton negativo;
    private String keyVoto;
    private Button mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int i = (int) getIntent().getExtras().get("Objeto");
        produto = Dados.getInstance().getItem(i);

        ImageView img = findViewById(R.id.imagem);
        TextView local = findViewById(R.id.local);
        final TextView preco = findViewById(R.id.preco);
        TextView ultimoNegativo = findViewById(R.id.ultimoNegativo);
        TextView ultimoPositivo = findViewById(R.id.ultimoPositivo);
        TextView totalPositivos = findViewById(R.id.totalPositivos);
        TextView totalNegativos = findViewById(R.id.totalNegativos);

        mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String caminho = "google.navigation:q=" + produto.getLat() + "," + produto.getLog();


                Uri gmmIntentUri = Uri.parse(caminho);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");


                startActivity(mapIntent);


            }
        });


        local.setText(produto.getLocal());
        preco.setText(produto.getPrecoFormatado());
        setImagem(img, produto.getCaminhoFoto());
        ultimoNegativo.setText("Último voto:" + produto.getUltimoNegativo());
        ultimoPositivo.setText("Último voto:" + produto.getUltimoPositivo());
        totalNegativos.setText("Total: " + produto.getNegativos());
        totalPositivos.setText("Total: " + produto.getPositivos());


        positivo = findViewById(R.id.positivo);
        negativo = findViewById(R.id.negativo);

        keyVoto = produto.getIdUsuario() + produto.getHorario();

        if (Votos.getInstance().jaVotou(keyVoto)) {
            Voto voto = Votos.getInstance().getVoto(keyVoto);
            if (voto.isPositivo()) {
                negativo.setVisibility(View.GONE);
            } else {
                positivo.setVisibility(View.GONE);
            }


        } else {

            positivo.setOnClickListener(this);
            negativo.setOnClickListener(this);

        }


    }

    private void setImagem(ImageView imagem, String caminho) {


        imagem.setImageDrawable(FotosBanco.getInstance().getImagem(caminho));
        imagem.setRotation(90);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == positivo) {
            Voto voto = new Voto(true, true, keyVoto);
            Votos.getInstance().adicionaVoto(voto);
            negativo.setVisibility(View.GONE);
            positivo.setOnClickListener(null);
            negativo.setOnClickListener(null);
            Dados.getInstance().atualizaVotos(produto, voto);
        }
        if (v == negativo) {
            Voto voto = new Voto(true, false, keyVoto);
            Votos.getInstance().adicionaVoto(voto);
            positivo.setVisibility(View.GONE);
            positivo.setOnClickListener(null);
            negativo.setOnClickListener(null);
            Dados.getInstance().atualizaVotos(produto, voto);
        }
    }
}
