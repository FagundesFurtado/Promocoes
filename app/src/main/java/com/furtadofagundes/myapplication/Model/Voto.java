package com.furtadofagundes.myapplication.Model;

public class Voto {

    private boolean votou;
    private boolean positivo;
    private String key;

    public Voto() {
    }

    public Voto(boolean votou, boolean positivo, String key) {
        this.votou = votou;
        this.positivo = positivo;
        this.key = key;
    }

    public boolean isVotou() {
        return votou;
    }

    public void setVotou(boolean votou) {
        this.votou = votou;
    }

    public boolean isPositivo() {
        return positivo;
    }

    public void setPositivo(boolean positivo) {
        this.positivo = positivo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
