package com.furtadofagundes.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furtadofagundes.myapplication.Singleton.FotosBanco;
import com.furtadofagundes.myapplication.Model.Produto;
import com.furtadofagundes.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Produto> {

    private final String TAG = "ListaAdapter";
    private DecimalFormat decimalFormat = new DecimalFormat("0,00");

    public ListAdapter(Context context, int resource, List<Produto> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.button, null);
        }
        Produto item = getItem(position);
        if (item != null) {
            ImageView imageView = view.findViewById(R.id.foto);
            TextView nome = view.findViewById(R.id.nome);
            TextView preco = view.findViewById(R.id.preco);
            TextView local = view.findViewById(R.id.local);
            TextView votos = view.findViewById(R.id.likes);

            if (nome != null) {
                nome.setText(item.getNome());
            }

            if (preco != null) {
                preco.setText(String.valueOf(item.getPrecoFormatado()));
            }
            if (local != null) {
                local.setText(item.getLocal());
            }
            if (imageView != null) {
//                Log.i(TAG, "Not null Image");
//                File rootPath = new File(Environment.getExternalStorageDirectory(), "Produtos");
//                File localFile = new File(rootPath, item.getCaminhoFoto());
//                Log.i(TAG, localFile.getAbsolutePath());
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(localFile);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Drawable buttonBg = Drawable.createFromStream(fis, null);


                imageView.setImageDrawable(FotosBanco.getInstance().getImagem(item.getCaminhoFoto()));
                imageView.setRotation(90);
            }

            if (votos != null) {
                int resultado = item.getPositivos() - item.getNegativos();
                votos.setText(String.valueOf(resultado));

            }


        }


        return view;
    }


}
