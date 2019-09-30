package com.furtadofagundes.myapplication.Singleton;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

public class FotoAtual {
    private static final FotoAtual ourInstance = new FotoAtual();

    private final int ID = 1;

    public static FotoAtual getInstance() {
        return ourInstance;
    }
    
    private NotificationManager notificacao;
    private NotificationCompat.Builder builder;
    private String nomeFoto;
    private Location location;

    private FotoAtual() {
    }


    public void setInformacoes(String nomeFoto, Location location) {
        this.nomeFoto = nomeFoto;
        this.location = location;

    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public Location getLocation() {
        return location;
    }


    public void inicializaNotificacao(Context context, int icon){



        notificacao = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Cadastrando produto")
                .setContentText("Aguarde")
                .setSmallIcon(icon).setProgress(100,0,true);
        notificacao.notify(ID, builder.build());



    }


    public void finalizaNotificacao(){
        notificacao.cancelAll();


    }




}
