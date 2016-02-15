package com.example.mario.puntogpsqr;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.content.Intent;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.zxing.integration.android.IntentIntegrator;


public class mapamain extends Activity implements OnClickListener{

    GoogleMap mMap;
    static Ruta ruta;
    Intent i;
    String latlon ;
    static LatLng goal,antgoal;
    private Button reinciarBtn;

    private void setUpMapIfNeeded() {
        // Configuramos el objeto GoogleMaps con valores iniciales.
        if (mMap == null) {
            //Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
            if (mMap != null) {
                // El objeto GoogleMap ha sido referenciado correctamente
                //ahora podemos manipular sus propiedades

                //Seteamos el tipo de mapa
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //Activamos la capa o layer MyLocation
                mMap.setMyLocationEnabled(true);

                Location posActual=getMyLocation();
                LatLng posActuallatlon=new LatLng(37.177887 , -3.598709 );

                if (posActual != null) {
                    posActuallatlon = new LatLng(posActual.getLatitude(),posActual.getLongitude());
                }
                //Centrar el mapa en la ubicacion actual y aplicarle un zoom de 18
                CameraUpdate camera;
                camera = CameraUpdateFactory.newLatLngZoom(posActuallatlon,18);
                mMap.animateCamera(camera);
            }
        }
    }

    //Obtiene la posición actual
    private Location getMyLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;

    }

    private void setMarker(LatLng position, String titulo, String info,float tipo) {
        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(titulo)  //Agrega un titulo al marcador
                .snippet(info)   //Agrega información detalle relacionada con el marcador
                .icon(BitmapDescriptorFactory.defaultMarker(tipo))); //Color del marcador
    }

    private void drawPolilyne(PolylineOptions options){
        Polyline polyline = mMap.addPolyline(options);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        reinciarBtn = (Button)findViewById(R.id.reiniciar_button);
        reinciarBtn.setOnClickListener(this);

        setUpMapIfNeeded();



        i=getIntent();
        latlon = i.getStringExtra("latlonstring");

        // Añadir un marker con la posicion goal obtenida en el codigo qr y crear una ruta
        if(latlon!=null) {
            goal = getLatLon(latlon);
            if (antgoal == null)
                antgoal = goal;
            else if (!antgoal.equals(goal)) {
                ruta = new Ruta(goal);
                antgoal = goal;
            }

            if (ruta == null)
                ruta = new Ruta(goal);

            setMarker(goal, "GOAL", "", BitmapDescriptorFactory.HUE_AZURE);
        }

        // Dibujar el camino que se ha seguido hasta llegar al GOAL
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            public void onMyLocationChange(Location pos) {
                if(ruta!=null) {
                    ruta.POLILINEA.color(Color.RED);

                    // Si no hemos alcanzdo el goal
                    if (!ruta.goal) {
                        // Si la posición actual no esta cercano al GOAL añadimos la posicion actual a la ruta
                        if (!ruta.Goal(pos))
                            ruta.POLILINEA.add(new LatLng(pos.getLatitude(), pos.getLongitude()));
                        // Dibujmos la ura en rojo
                        drawPolilyne(ruta.POLILINEA);
                    } else {
                        // Si se ha alcanzdo el goal dibujamos el camino que hemos seguido en verde
                        ruta.POLILINEA.color(Color.BLUE);
                        drawPolilyne(ruta.POLILINEA);
                    }
                }
            }
        });
    }

    // Obtiene el valor numérico de la latitud y longitud del string en el formato indicado en el guión
    private LatLng getLatLon(String s){
        LatLng latlng;
        double lat, lon;
        int i;
        String aux;

        //Calcular la posicion de la aparicion de _LONGITUD_
        i=s.indexOf("_LONGITUD_");
        //Obtener la latitud del string
        aux=s.substring(8, i);
        lat=Double.valueOf(aux).doubleValue();
        //Obtener la longitud del string
        aux=s.substring(i+10 , s.length());
        lon= Double.valueOf(aux).doubleValue();

        latlng = new LatLng(lat,lon);

        return latlng;
    }

    @Override
    public void onClick(View v){
        //Se responde al evento click
        if(v.getId()==R.id.reiniciar_button){
           if(goal!=null) {
                mMap.clear();
                setMarker(goal, "GOAL", "", BitmapDescriptorFactory.HUE_AZURE);
                ruta = new Ruta(goal);
           }
        }
    }
}
