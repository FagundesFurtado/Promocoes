package com.furtadofagundes.myapplication.Main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;


import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.furtadofagundes.myapplication.Activity.CameraActivity;
import com.furtadofagundes.myapplication.Interface.RespostaUsuario;
import com.furtadofagundes.myapplication.Activity.Login;
import com.furtadofagundes.myapplication.Singleton.FotoAtual;
import com.furtadofagundes.myapplication.Singleton.Usuario;
import com.furtadofagundes.myapplication.R;
import com.furtadofagundes.myapplication.Fragment.MeusProdutos;
import com.furtadofagundes.myapplication.Fragment.Notificacoes;
import com.furtadofagundes.myapplication.Fragment.Pesquisa;
import com.furtadofagundes.myapplication.Fragment.Principal;
import com.furtadofagundes.myapplication.Fragment.Produtos;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RespostaUsuario, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private int MY_PERMISSION_GPS = 0;
    private int MY_PERMISSION_WRITE = 0;
    private int MY_PERMISSION_CAMERA = 0;
    private static final int RC_SIGN_IN = 123;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private PendingResult<LocationSettingsResult> pendingResult;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationSettingsRequest;
    private final String GPS = "GPSLOG";
    private final String TAG = "Permissao";

    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private NavigationView navigationView;
    private LinearLayout fundoFragment;


    private FusedLocationProviderClient mLocation;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);


    }


    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);




        setSupportActionBar(toolbar);


        navigationView = findViewById(R.id.nav_view);

        fundoFragment = findViewById(R.id.fundoFragment);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getApplicationContext(), Login.class));

                }
            }
        };


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        toolbar.setTitle("Promoções");

        verificaPermissoes();

        mLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    FotoAtual.getInstance().setLocation(location);
                }
            }
        });


//        Date date = new Date();
//        Dados.getInstance().inserirProduto(new Produto("Nome", 10, "San Michel", "URL DOWNLOAD", "Celular Local", 10, 10 , date.getTime()));


        fragmentManager(new Principal());

        this.atualizaConfiguracoesUsuario();
    }


    private void verificaPermissoes() {

        String permissoes[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ArrayList<String> pedidos = new ArrayList<>();
        for (String s : permissoes) {
            int permissao = ContextCompat.checkSelfPermission(this, s);
            if (permissao != PackageManager.PERMISSION_GRANTED) {
                pedidos.add(s);
            }
        }
        if (!pedidos.isEmpty()) {

            Log.i(TAG, "Nao tem permissao");
            pedirPermissao(pedidos.toArray(new String[pedidos.size()]), MY_PERMISSION_WRITE);
        } else {
            mEnableGps();
        }

    }


    private void pedirPermissao(String[] permissao, int saida) {

        Log.i(TAG, "Pedir Permissao ");

        ActivityCompat.requestPermissions(this, permissao, saida);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opcoes, menu);
        return true;
    }


    private void ligouGPS() {

        MenuItem gps = toolbar.getMenu().findItem(R.id.gps);


        gps.setIcon(R.drawable.baseline_gps_fixed_white_48dp);


    }

    private void desligouGPS() {

        MenuItem gps = toolbar.getMenu().findItem(R.id.gps);

        gps.setIcon(R.drawable.baseline_gps_off_white_24dp);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.camera) {
            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.gps) {
            mEnableGps();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mEnableGps();
    }

    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    private void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    private void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        ligouGPS();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {

                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);


                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fundoFragment);
            if(f instanceof Produtos || f instanceof Pesquisa ||  f instanceof Notificacoes){
                super.onBackPressed();
            }
            else {
               fragmentManager(new Principal());
            }


        } else {
            super.onBackPressed();
        }
    }

    private void fragmentManager(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fundoFragment, fragment);
        transaction.commit();


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            fragmentManager(new Principal());
            toolbar.setTitle("Promoções");
        } else if (id == R.id.nav_produtos) {
            toolbar.setTitle("Meus Produtos");
            fragmentManager(new MeusProdutos());

        } else if (id == R.id.nav_desejos) {
            toolbar.setTitle("Meus Desejos");

        } else if (id == R.id.nav_lojas) {
            toolbar.setTitle("Lojas");

        } else if (id == R.id.nav_configuracoes) {
            toolbar.setTitle("Configurações");

        } else if (id == R.id.nav_report) {

            toolbar.setTitle("Relatar Problema");
        } else if (id == R.id.nav_exit) {
            mAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.i("LoginAcesso", "Deu certo");
                Log.i("LoginAcesso", "Anonimo");
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("UploadFireBase", "Deu errado o login");
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(GPS, "Ligou o GPS");
                        ligouGPS();
                        break;
                    case Activity.RESULT_CANCELED:
                        desligouGPS();
                        Toast.makeText(this, "Para uma melhor experiência, habilite o GPS", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void atualizaConfiguracoesUsuario() {

        Log.i("UsuarioLogin", "Atualizando informacoes");

        View v = navigationView.getHeaderView(0);

        final ImageView fotoUsuario = v.findViewById(R.id.fotoUsuario);
        final TextView nomeUsuario = v.findViewById(R.id.usuarioNome);
        final TextView emailUsuario = v.findViewById(R.id.usuarioEmail);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (nomeUsuario != null) {
                    nomeUsuario.setText(Usuario.getInstance().getNomeUsuario());
                }

                if (emailUsuario != null) {
                    emailUsuario.setText(Usuario.getInstance().getEmail());
                }
                if (fotoUsuario != null) {
                    setImagem(Usuario.getInstance().getCaminhoFoto(), fotoUsuario);
                }
            }
        });
    }

    private void setImagem(String nomeFoto, ImageView imageView) {
        File rootPath = new File(Environment.getExternalStorageDirectory(), "Promocoes");
        File pasta = new File(rootPath, "Usuario");
        File localFile = new File(pasta, nomeFoto);

        if (!localFile.exists()) {
            localFile.mkdirs();
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Drawable buttonBg = Drawable.createFromStream(fis, null);
        imageView.setImageDrawable(buttonBg);


    }

}




