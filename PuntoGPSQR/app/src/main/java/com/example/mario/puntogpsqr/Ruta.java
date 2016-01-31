package com.example.mario.puntogpsqr;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import android.location.Location;


public class Ruta {   //Nueva clase Posiciones

    private double minLat  = 0;
    private double minLon  = 0;
    private double maxLat  = 0;
    private double maxLon  = 0;
    private final double margen = 0.00005 ;
    //Constante de Posición del marcador
    public LatLng posGoal ;

    public boolean goal = false;

    //Constante de Opciones de Polilínea.
    public PolylineOptions POLILINEA = new PolylineOptions();

    public Ruta(LatLng pGoal){
        posGoal=pGoal;
        minLat = posGoal.latitude - margen;
        maxLat = posGoal.latitude + margen;
        minLon = posGoal.longitude - margen;
        maxLon = posGoal.longitude + margen;
        goal=false;
    }

    public boolean Goal(Location l){
        double lat = l.getLatitude();
        double lon = l.getLongitude();
        if(!goal)
            if(lat>=minLat && lat<=maxLat && lon>=minLon && lon<=maxLon){
                goal=true;
                POLILINEA.add(posGoal);
            }
        return goal;
    }


}