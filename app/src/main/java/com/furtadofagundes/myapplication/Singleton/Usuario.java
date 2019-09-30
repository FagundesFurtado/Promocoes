package com.furtadofagundes.myapplication.Singleton;

import android.net.Uri;
import android.util.Log;

import com.furtadofagundes.myapplication.Interface.RespostaUsuario;
import com.furtadofagundes.myapplication.AsynkTasks.AtualizaUsuario;
import com.google.firebase.auth.FirebaseUser;

public class Usuario {
    private static final Usuario ourInstance = new Usuario();

    public static Usuario getInstance() {
        return ourInstance;
    }

    private final String TAG = "UsuarioLogin";
    private String nomeUsuario = "Cristiano";
    private String email;
    private String idUser;
    private String caminhoFoto;
    private Uri fotoUsuario;
    private RespostaUsuario usuario;


    private Usuario() {
    }


    public void setUsuario(FirebaseUser user) {
        Log.i(TAG, "Setando usuario");

        this.nomeUsuario = user.getDisplayName();
        this.email = user.getEmail();
        this.fotoUsuario = user.getPhotoUrl();
        this.idUser = user.getUid();
        this.usuario = usuario;
        this.caminhoFoto = idUser + ".jpg";

        Log.i(TAG, "Informacao do usuario");
        Log.i(TAG, nomeUsuario);
        Log.i(TAG, email);
        Log.i(TAG, idUser);

        Log.i(TAG, "Acabou");
        AtualizaUsuario atualizaUsuario = new AtualizaUsuario(caminhoFoto, fotoUsuario);
        Votos.getInstance();

    }

    public String getIdUser() {
        return idUser;
    }


    public String getNomeUsuario() {
        return nomeUsuario;
    }


    public String getEmail() {
        return email;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }
}
