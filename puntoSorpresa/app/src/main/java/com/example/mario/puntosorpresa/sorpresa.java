package com.example.mario.puntosorpresa;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class sorpresa extends Activity implements SensorEventListener {

    TextView text_nombres, movimiento;
    SensorManager sm;
    Sensor sensor;

    private long last_update = 0;

    private int n_pasadas = 0;

    ImageView imagen;

    // Vector con los id de las imagenes
    int[] imagenId={
            R.drawable.imagen1,
            R.drawable.imagen2,
            R.drawable.imagen3,
            R.drawable.imagen4
    };

    String nombres[]={"imagen1","imagen2","imagen3","imagen4" };

    Intent intent;

    boolean boton,n_pausas=false;

    int i=0;

    final Handler handler = new Handler();

    // Cambia la imagen actual e incrementa el indice al vector de ids cada 3 segundos
    Runnable runnable = new Runnable(){
        public void run(){
            imagen.setImageResource(imagenId[i]);
            text_nombres.setText(nombres[i]);
            i=(i+1)%imagenId.length;
            handler.postDelayed(runnable,3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorpresa);

        // Inicializar el sensor de proximidad
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensor=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

        movimiento=(TextView)findViewById(R.id.movimiento);
        text_nombres= (TextView) findViewById(R.id.text_nombres);
        text_nombres.setText(nombres[0]);

        // Cargar la primera imagen
        imagen = (ImageView) findViewById(R.id.imageView);
        imagen.setImageResource(imagenId[0]);

        // Obtener el booleano que indica si vamos a iniciar la galeria o la presentacion
        intent=getIntent();
        boton = intent.getBooleanExtra("boton",false);

        // Si lanzamos la presentacion iniciar el runnable para cambiar la imagen cada 3 segundos
        if(boton)
            handler.post(runnable);

        last_update = System.currentTimeMillis();

    }

    @Override protected void onStop() {
        super.onStop();
        if(boton)
            handler.removeCallbacks(runnable);
    }

    @Override protected void onRestart() {
        super.onRestart();
        if(boton)
            handler.post(runnable);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){

        float v=event.values[0];

        if(!boton)
            Galeria(v);
        else
            Presentacion(v);
    }

    public void Galeria(float v){

        long current_time = System.currentTimeMillis();
        long time_difference = current_time - last_update;

        // Si se ha detectado un objeto
        if(v==0){

            n_pasadas++;

            // Si se ha detectado el objeto 2 veces en menos de 500 milisegundos
            if(n_pasadas == 2) {
                if (time_difference <= 500) {
                    // Se reinicia el numero de detecciones
                    n_pasadas = 0;
                    movimiento.setText("Movimiento: Doble Pasada (atrás) " );
                    // Se decrementa el indice de las imagenes
                    i=(i+imagenId.length-1)%imagenId.length;
                } else {
                    // Si la segunda deteccion ha tenido lugar en mas de 500 milisegundos de la primera, no se contabiliza como el gesto
                    // pero si cuenta, por lo que se inicia a 1 y no a 0 (se podria contar como la primera deteccion de un gesto de doble pasada
                    n_pasadas = 1;
                }
            }
        }else{
            // Si se ha detectado un objeto una vez y ha permanecido durante 60 milisegundos
            if(n_pasadas==1){
                if(time_difference>60){
                    movimiento.setText("Movimiento: Pasada corta (siguiente)" );
                    // Se incrementa el indice de las imagenes
                    i=(i+1)%imagenId.length;
                    // Se reinicia el numero de detecciones
                    n_pasadas=0;
                }
            }

        }

        // Se actualiza el tiempo
        last_update=current_time;
        // Se actualiza la imagen con el nuevo indice
        imagen.setImageResource(imagenId[i]);
        text_nombres.setText(nombres[i]);

    }

    public void Presentacion(float v){

        long current_time = System.currentTimeMillis();
        long time_difference = current_time - last_update;

        // Si se ha detectado un objeto y ha permanecido durante 1400 milisegundos
        if(time_difference>1400 && v==10){
            // Si la presentcion está en marcha, se pausa
            if(!n_pausas) {
                movimiento.setText("Movimiento: Pasada larga (pausa) ");
                handler.removeCallbacks(runnable);
                // Se actualiza el booleano que indica si la presentacion esta pausada o no
                n_pausas=!n_pausas;
            }else{
                movimiento.setText("Movimiento: Pasada larga (play)");
                handler.post(runnable);
                n_pausas=!n_pausas;
            }

        }

        // Se acutaliza el tiempo
        last_update=current_time;
    }
}
