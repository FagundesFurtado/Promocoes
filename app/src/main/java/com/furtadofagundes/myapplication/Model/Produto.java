package com.furtadofagundes.myapplication.Model;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Produto {


    private String nome;
    private double preco;
    private String local;
    private String caminhoFotoDownload;
    private String caminhoFoto;
    private int positivos;
    private int negativos;
    private long horario;
    private Double lat;
    private Double log;
    private String ultimoPositivo;
    private String ultimoNegativo;
    private String idUsuario;
    private NumberFormat formatter = new DecimalFormat("#0.00");

    public Produto() {
    }

    public Produto(String nome, double preco, String local, Double lat, Double log, String caminhoFoto, String caminhoFotoDownload, long horario, String usuarioId) {
        this.nome = nome;
        this.preco = preco;
        this.local = local;
        this.positivos = 0;
        this.negativos = 0;
        this.lat = lat;
        this.log = log;
        this.caminhoFoto = caminhoFoto;
        this.caminhoFotoDownload = caminhoFotoDownload;
        this.horario = horario;
        this.ultimoPositivo = " - ";
        this.ultimoNegativo = " - ";
        this.idUsuario = usuarioId;

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCaminhoFotoDownload() {
        return caminhoFotoDownload;
    }

    public void setCaminhoFotoDownload(String caminhoFotoDownload) {
        this.caminhoFotoDownload = caminhoFotoDownload;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public int getPositivos() {
        return positivos;
    }

    public void setPositivos(int positivos) {
        this.positivos = positivos;
    }

    public int getNegativos() {
        return negativos;
    }

    public void setNegativos(int negativos) {
        this.negativos = negativos;
    }



    public String getUltimoPositivo() {
        return ultimoPositivo;
    }

    public void setUltimoPositivo(String ultimoPositivo) {
        this.ultimoPositivo = ultimoPositivo;
    }

    public String getUltimoNegativo() {
        return ultimoNegativo;
    }

    public void setUltimoNegativo(String ultimoNegativo) {
        this.ultimoNegativo = ultimoNegativo;
    }

    public String getPrecoFormatado() {


        String preco = formatter.format(this.preco);
        preco = "R$" + preco.replace(".", ",");

        return preco;

    }

    public long getHorario() {
        return horario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "nome='" + nome + '\'' +
                ", preco=" + preco +
                ", local='" + local + '\'' +
                ", caminhoFotoDownload='" + caminhoFotoDownload + '\'' +
                ", caminhoFoto='" + caminhoFoto + '\'' +
                ", positivos=" + positivos +
                ", negativos=" + negativos +
                ", lat='" + lat + '\'' +
                ", log='" + log + '\'' +
                ", ultimoPositivo='" + ultimoPositivo + '\'' +
                ", ultimoNegativo='" + ultimoNegativo + '\'' +
                ", formatter=" + formatter +
                '}';
    }
}
