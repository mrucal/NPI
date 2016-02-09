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

/**
 * Created by Mario on 09/02/2016.
 */
public class brujula extends Activity implements SensorEventListener{

    private ImageView image;

    private RelativeLayout layout;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
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

        i=getIntent();
        punto= i.getFloatExtra("punto_cardinal",0);
        error=i.getIntExtra("error",5);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        layout = (RelativeLayout) findViewById(R.id.layout);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mensaje = (TextView) findViewById(R.id.mensaje);

        // initialize your android device sensor capabilities
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

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mensaje.setVisibility(View.INVISIBLE);
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Grados: " + Float.toString(degree));

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
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
