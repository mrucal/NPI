package com.example.mario.GestoFoto;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;


public class gestos extends Activity implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary libreria;
    private TextView salida;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestos);

        // Cargar la librería de gestos del fichero de gestos de la carpeta raw
        libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);

        if (!libreria.load()) {
            finish();
        }

        // Inicializar el View de gestos
        GestureOverlayView gesturesView = (GestureOverlayView)findViewById(R.id.gestures);
        gesturesView.addOnGesturePerformedListener(this);
        salida = (TextView) findViewById(R.id.salida);
    }



    public void onGesturePerformed(GestureOverlayView ov,Gesture gesture) {

        // Obtenemos el array de los posibles gestos obtenidos
        ArrayList<Prediction> predictions =libreria.recognize(gesture);

        // Mostramos la predicción con mayor probabilidad
        salida.setText("Gesto - Precisión\n");
        salida.append(predictions.get(0).name + " " + predictions.get(0).score+"\n");

        // Si hemos hecho el gesto pedido con una predicción mayor a 4.7 iniciamos una nueva actividad.
        if(predictions.get(0).name.equals("GestoFoto") && predictions.get(0).score > 4.7){
            Intent i = new Intent(this,camara.class);
            startActivity(i);
        }
    }



    }
