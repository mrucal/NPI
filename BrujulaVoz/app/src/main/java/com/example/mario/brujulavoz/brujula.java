package com.example.mario.brujulavoz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class brujula extends Activity implements SensorEventListener{

    private ImageView image;

    private RelativeLayout layout;

    // Variable utilizada para rotar la imagen de la brújula
    private float currentDegree = 0f;

    private SensorManager mSensorManager;

    TextView tvHeading,mensaje;

    private final int NORTE = 0;
    private final int ESTE = 90;
    private final int SUR = 180;
    private final int OESTE = 270;

    private float punto = SUR;

    private int error=10;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_brujula);

        // Obtenemos el intent del reconocimiento con los valores que envia (punto cardinal y error)
        i=getIntent();
        punto= i.getFloatExtra("punto_cardinal",0);
        error=i.getIntExtra("error",5);

        // imagen de la brujula
        image = (ImageView) findViewById(R.id.imageViewCompass);

        layout = (RelativeLayout) findViewById(R.id.layout);

        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mensaje = (TextView) findViewById(R.id.mensaje);

        // Inicililizar el sensor
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Parar el listener
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        mensaje.setVisibility(View.INVISIBLE);

        // Obtener el ángulo
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Grados: " + Float.toString(degree));

        // Si el angulo está entre el punto cardinal que buscamos y el margen de error, el color de fondo de la aplicación
        // cambia a verde, si el ángulo está próximo al margen de error el color de fondo cambia a naranja (indicando que nos
        // acercamos al punto
        if(punto==NORTE){

            if((degree >= 360-error && degree<=360) || (degree>=0 && degree<= NORTE + error)) {
                layout.setBackgroundColor(Color.GREEN);
                mensaje.setVisibility(View.VISIBLE);
            }
            else if ((degree >= 360 - 2*error && degree <= 360 - error) || (degree >= 0 + error && degree <= 0 + 2*error))
                layout.setBackgroundColor(Color.rgb(255, 183, 26));
            else
                layout.setBackgroundColor(Color.rgb(60, 140, 255));

        }else {

            if (degree >= punto - error && degree <= punto + error){
                layout.setBackgroundColor(Color.GREEN);
                mensaje.setVisibility(View.VISIBLE);
            }
            else if ((degree >= punto - 2*error && degree <= punto - error) || (degree >= punto + error && degree <= punto + 2*error))
                layout.setBackgroundColor(Color.rgb(255, 183, 26));
            else
                layout.setBackgroundColor(Color.rgb(60, 140, 255));

        }

        // Crear y lanzar una animación para rotar la imagen que hará de brújula.
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);


        ra.setDuration(210);


        ra.setFillAfter(true);


        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
