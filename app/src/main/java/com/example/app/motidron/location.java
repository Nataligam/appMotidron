package com.example.app.motidron;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

public class location {

    private LocationManager locationManager;
    private Activity c;
    private TextView longitud, latitud;

    public location(Activity c, TextView longitud, TextView latitud){
        this.c=c;
        this.longitud=longitud;
        this.latitud=latitud;
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        this.gpsActivate();
        this.getPermission();
    }

    public void gpsActivate(){
        boolean gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Verifica si el gps esta desactivado
        if (!gpsActivo) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setMessage("El sistema GPS esta desactivado, tener el GPS activado es necesario " +
                    "para el funcionaiento " +
                    "de las notificaciones por GPS \n ¿Desea activarlo?  ")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                            @SuppressWarnings("unused") final int id) {
                            c.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            //activa el switch de notificaciones
                            enableLocationSettings();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog,
                                            @SuppressWarnings("unused") final int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(c, "esta encendido el GPS", Toast.LENGTH_LONG).show();
        }
    }


    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        c.startActivity(settingsIntent);
    }


    private void getPermission(){
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(c,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(c,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(c,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(c,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitud.setText(String.valueOf(location.getLatitude()));
        longitud.setText(String.valueOf(location.getLongitude()));
    }

}
