package com.example.mario.puntogpsqr;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;



public class qrmain extends  Activity implements OnClickListener {

    private Button scanBtn;
    private Button mapaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        //Se Instancia el botón de Scan
        scanBtn = (Button)findViewById(R.id.scan_button);
        mapaBtn = (Button)findViewById(R.id.mapa_button);
        //Se agrega la clase qrmain.java como Listener del evento click del botón de Scan
        scanBtn.setOnClickListener(this);
        mapaBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        //Se responde al evento click
        if(v.getId()==R.id.scan_button){
            //Se instancia un objeto de la clase IntentIntegrator
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //Se procede con el proceso de scaneo
            scanIntegrator.initiateScan();
        }
        if(v.getId()==R.id.mapa_button){
            Intent i = new Intent(this,mapamain.class);
            startActivity(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado pro lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            String scanContent = scanningResult.getContents();
            Intent i = new Intent(this,mapamain.class);
            i.putExtra("latlonstring",scanContent);
            startActivity(i);

        }else{
            //Quiere decir que NO se obtuvo resultado
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se ha recibido datos del scaneo!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}
