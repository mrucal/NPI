package com.example.mario.movimientosonido;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class movimiento_sonido extends Activity implements SensorEventListener {

    private long last_update = 0;

    private Posicion curPos=new Posicion();

    private ArrayList<Posicion> gesto = new ArrayList();

    private MediaPlayer sonido;

    private int estado=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento_sonido);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sonido = MediaPlayer.create(this, R.raw.latigazo);

        // Gesto que queremos reconocer
        gesto.add(new Posicion(0, 9.8f, 0));
        gesto.add(new Posicion(8,5,0));

        last_update = System.currentTimeMillis();

        // Inicializar el sensor acelerometro y lanzarlo
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            // Tiempo actual
            long current_time = System.currentTimeMillis();

            //margen de error de la posición en cada eje
            float umbral_x=3,umbral_y=3,umbral_z=2;

            // Obtener los valores de cada eje y almacenarlos en una nueva variable de tipo Posicion
            curPos=new Posicion(event.values[0],event.values[1],event.values[2]);

            long time_difference = current_time - last_update;

            // Volver a la primera posición si ha pasado mas de un segundo desde localizar la ultima posicion
            if(time_difference>1000) {
                estado=0;
                last_update=current_time;
            }

            // Si la posicion actual esta en el margen de error de la posicion actual del gesto
            if(curPos.between(gesto.get(estado),umbral_x,umbral_y,umbral_z)) {

                // Si es la ultima posicion del gesto, reproducimos el sonido y mostramos la imagen
                if(estado==gesto.size()-1) {
                    ((ImageView) findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
                    sonido.start();
                }
                // Avanzamos a la siguiente posicion y actualizamos el tiempo
                estado=(estado+1)%gesto.size();
                last_update=current_time;
            }


            ((TextView) findViewById(R.id.txtAccX)).setText("X: " + curPos.x);
            ((TextView) findViewById(R.id.txtAccY)).setText("Y: " + curPos.y);
            ((TextView) findViewById(R.id.txtAccZ)).setText("Z: " + curPos.z);
            // Si ha pasado mas de un segundo dejamos de mostrar la imagen
            if(time_difference>1000) ((ImageView) findViewById(R.id.imageView)).setVisibility(View.INVISIBLE);

        }

    }
}