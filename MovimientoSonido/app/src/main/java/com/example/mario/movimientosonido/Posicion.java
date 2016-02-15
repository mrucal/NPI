package com.example.mario.movimientosonido;

public class Posicion {

    public float x,y,z;

    public Posicion(){
        x=y=z=0;
    }

    public Posicion(float a, float b, float c){
        x=a;y=b;z=c;
    }

    private boolean between_pos(float a, float i, float j){
        return i-j<=a && a<=i+j;
    }

    public boolean between(Posicion a,float ux, float uy, float uz){
        return between_pos(x,a.x,ux) && between_pos(y,a.y,uy) && between_pos(z,a.z,uz);
    }
}
