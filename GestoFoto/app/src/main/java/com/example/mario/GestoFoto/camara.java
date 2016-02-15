package com.example.mario.GestoFoto;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

import android.provider.MediaStore.Images;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;



public class camara extends Activity implements SurfaceHolder.Callback{


    Camera myCamera;
    byte[] tempdata;
    boolean myPreviewRunning = false;
    private SurfaceHolder mySurfaceHolder;
    private SurfaceView mySurfaceView;
    TextView tview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camara);

        mySurfaceView = (SurfaceView) findViewById(R.id.surface);
        mySurfaceHolder = mySurfaceView.getHolder();
        mySurfaceHolder.addCallback(this);

        tview = (TextView) findViewById(R.id.textView);
        tview.setText("Waiting...");

        //Iniciar el método mMyRunnable tras una espera de 3 segundos
        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable, 3000);

    }

    private Runnable mMyRunnable = new Runnable()
    {

        public void run() {
            // Tomar una foto
            myCamera.takePicture(myShutterCallback, myPictureCallback, myJpeg);

        }
    };

    ShutterCallback myShutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    PictureCallback myPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera myCamera) {
            //TODO Auto-generated method stub
        }
    };

    PictureCallback myJpeg = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera myCamera) {
            //TODO Auto-generated method stub
            if(data != null){
                tempdata = data;
                tview.setText("IMAGEN CAPTURADA!!");
                done();
            }
        }
    };

    //Guardar la imagen
    void done(){
        Bitmap bm = BitmapFactory.decodeByteArray(tempdata, 0, tempdata.length);
        String url = Images.Media.insertImage(getContentResolver(), bm, null, null);
        bm.recycle();
        Bundle bundle = new Bundle();
        if(url != null){
            bundle.putString("url",url);
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(RESULT_OK, mIntent);
        }else{
            Toast.makeText(this, "La imagen no ha podido ser guardada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myCamera = Camera.open();
        myCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try{
            if(myPreviewRunning){
                myCamera.stopPreview();
                myPreviewRunning = false;
            }
            myCamera.setPreviewDisplay(mySurfaceHolder);
            myCamera.startPreview();
            myPreviewRunning = true;
        }catch(Exception e){}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myCamera.stopPreview();
        myPreviewRunning = false;
        myCamera.release();
        myCamera = null;
    }
}
