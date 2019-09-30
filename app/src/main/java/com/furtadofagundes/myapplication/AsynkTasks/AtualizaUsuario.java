package com.furtadofagundes.myapplication.AsynkTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AtualizaUsuario extends AsyncTask {


    private final String TAG = "AtualizaUsuario";
    private String caminhoFoto;
    private Uri fotoUsuario;


    public AtualizaUsuario(String caminhoFoto, Uri fotoUsuario) {
        this.caminhoFoto = caminhoFoto;
        this.fotoUsuario = fotoUsuario;
        this.execute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Promocoes");
        File pasta = new File(rootPath, "Usuario");
        File arquivo = new File(pasta, caminhoFoto);


        if (arquivo.exists()) {
            Log.i(TAG, "Nao existe a foto " +rootPath.toString());
            return null;
        }

        Log.i(TAG, "Comecando download foto usuario");

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(fotoUsuario.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }


            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();


            File pastaRaiz = new File(Environment.getExternalStorageDirectory(), "Promocoes");
            File pastaUsuario = new File(pastaRaiz, "Usuario");
            File fotoUsuario = new File(pastaUsuario, caminhoFoto);

            output = new FileOutputStream(fotoUsuario);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                output.write(data, 0, count);
            }
            Log.i(TAG, "Download deu certo");
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        Log.i(TAG, "Finalizou download");



        return null;
    }

}
