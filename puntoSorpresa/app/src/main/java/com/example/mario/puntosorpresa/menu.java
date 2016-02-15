package com.example.mario.puntosorpresa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends Activity implements View.OnClickListener {

    Button button_gal,button_pres;

    boolean  boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_gal=(Button)findViewById(R.id.button_gal);
        button_pres=(Button)findViewById(R.id.button_pres);

        button_gal.setOnClickListener(this);
        button_pres.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Boton galeria
        if(v.getId()==R.id.button_gal){
            boton=false;
        }
        // Boton presentacion
        if(v.getId()==R.id.button_pres){
            boton=true;
        }
        Intent i=new Intent(this,sorpresa.class);
        i.putExtra("boton",boton);
        startActivity(i);

    }
}
