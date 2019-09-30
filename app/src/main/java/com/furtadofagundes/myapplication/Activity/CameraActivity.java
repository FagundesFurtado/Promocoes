package com.furtadofagundes.myapplication.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.furtadofagundes.myapplication.Main.MainActivity;
import com.furtadofagundes.myapplication.Adapter.Preview;
import com.furtadofagundes.myapplication.Singleton.Usuario;
import com.furtadofagundes.myapplication.R;

public class CameraActivity extends Activity {
    private static final String TAG = "ConfirmaFoto";
    private Preview preview;
    private FloatingActionButton button;
    private Camera camera;
    private Activity act;
    private Context ctx;
    private String nomeFoto;


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);


        button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);

            }
        });


        preview = new Preview(this, (SurfaceView) findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);

        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            }
        });

        button.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                camera = Camera.open(0);

                camera.startPreview();
                camera.setDisplayOrientation(90);
                preview.setCamera(camera);
            } catch (RuntimeException ex) {

            }
        }
    }

    public float getBackCameraResolutionInMp() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0; i < noOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera camera = Camera.open(i);
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float) pixelCountTemp) / (1024000.0f);
                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }


    @Override
    protected void onPause() {
        if (camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
                			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);

            //resetCam();
            Log.i(TAG, "onPictureTaken - jpeg");

        }
    };


    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;


            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Promocoes/Fotos");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());


                String foto = Usuario.getInstance().getIdUser() + fileName;


                File outFile = new File(dir, foto);
                nomeFoto = foto;


                outStream = new FileOutputStream(outFile);


                outStream.write(data[0]);
                outStream.flush();
                outStream.close();


                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                Intent i = new Intent(getApplicationContext(), ConfirmacaoFoto.class);
                i.putExtra("NomeArquivo", nomeFoto);
                startActivity(i);
            }
            return null;
        }

    }



}