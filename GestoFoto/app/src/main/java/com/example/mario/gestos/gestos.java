package com.example.mario.gestos;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class gestos extends Activity implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary libreria;
    private TextView salida;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestos);

        libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);

        if (!libreria.load()) {
            finish();
        }

        GestureOverlayView gesturesView = (GestureOverlayView)findViewById(R.id.gestures);
        gesturesView.addOnGesturePerformedListener(this);
        salida = (TextView) findViewById(R.id.salida);
    }



    public void onGesturePerformed(GestureOverlayView ov,Gesture gesture) {

        ArrayList<Prediction> predictions =libreria.recognize(gesture);
        salida.setText("Gesto - Precisión\n");/*
        for (Prediction prediction : predictions){
            salida.append(prediction.name+" " +
                    prediction.score+"\n");
        }*/
        salida.append(predictions.get(0).name + " " + predictions.get(0).score+"\n");
        if(predictions.get(0).name.equals("GestoFoto") && predictions.get(0).score > 4.7){
            Intent i = new Intent(this,camara.class);
            startActivity(i);
        }
    }



    }
