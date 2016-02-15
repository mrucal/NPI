package com.example.mario.brujulavoz;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class reconocimiento extends Activity {

    private Button iniciar;
    private static int REQUEST_CODE = 123;

    private final float NORTE = 0;
    private final float ESTE = 90;
    private final float SUR = 180;
    private final float OESTE = 270;

    float punto = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocimiento);

        iniciar = (Button)findViewById(R.id.iniciar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Definición del intent para realizar en análisis del mensaje
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // Indicamos el modelo de lenguaje para el intent
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                // Definimos el mensaje que aparecerá
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga un punto cardenal y el error ...");
                // Lanzamos la actividad esperando resultados
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            // Obtenemos las cadenas que se han escuchado. La primera será la que se ajusta con mayor probabilidad a lo eschuchado.
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // Separamos las palabras de la cadena escuchada.
            String [ ] palabras = matches.get(0).toString().split(" ");

            List aux=Arrays.asList(palabras);

            // Si la cadena tiene dos palabras, la primera es un punto cardinal y la segunda un numero
            if(aux.size()==2 && getParametrosBrujula(palabras[0]) && esNumero(palabras[1])){

                // Creamos y lanzamos una nueva actividad a la que le pasamos el punto cardinal y el error.
                Intent i = new Intent(this,brujula.class);
                i.putExtra("punto_cardinal",punto);
                i.putExtra("error",Integer.parseInt(palabras[1]));
                startActivity(i);

            }else {

                // Si ha habido algun problema, mostramos un mensaje de error con la cadena incorrecta para poder rectificar.
                Toast toast = Toast.makeText(this, "Error al escuchar: " +matches.get(0), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }

        }
    }

    // Comprobamos que el parametro p es un punto cardinal (se prueba con diferentes formatos) y se le da un valor a la variable asociada al punto cardinal
    private boolean getParametrosBrujula(String p){

        switch (p) {
            case "Norte":
            case "norte":
            case "NORTE":
                punto=NORTE;
                break;
            case "Este":
            case "este":
            case "ESTE":
                punto=ESTE;
                break;
            case "Sur":
            case "sur":
            case "SUR":
                punto=SUR;
                break;
            case "Oeste":
            case "oeste":
            case "OESTE":
                punto=OESTE;
                break;
            default:
                return false;
        }

        return true;
    }

    //Comprobamos que el string es un número y que es positivo y menor que 100 (para que el error no sea demasiado grande)
    private static boolean esNumero(String s){
        int n;
        try{
            Long.parseLong(s);
            n=Integer.parseInt(s);
        }catch(Exception e){
            return false;
        }
        if( n>=0 && n<=100 )
            return true;
        else return false;
    }
}
