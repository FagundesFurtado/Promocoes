package com.furtadofagundes.myapplication.Singleton;

import com.furtadofagundes.myapplication.Model.Voto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Votos {
    private static final Votos ourInstance = new Votos();

    public static Votos getInstance() {
        return ourInstance;
    }

    private HashMap<String, Voto> votos = new HashMap<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private Votos() {
        database.getReference().child(Usuario.getInstance().getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                votos.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Voto voto = d.getValue(Voto.class);
                    votos.put(voto.getKey(), voto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean jaVotou(String key) {
        return (votos.containsKey(key));
    }

    public Voto getVoto(String key) {
        return votos.get(key);
    }

    public void adicionaVoto(Voto voto) {
        DatabaseReference reference = database.getReference().child("Voto");
        reference.child(Usuario.getInstance().getIdUser()).setValue(voto);
        votos.put(voto.getKey(), voto);
    }

}
