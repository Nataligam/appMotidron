package com.example.app.motidron;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Base64.Encoder;

import com.example.app.motidron.Model.Conexion;
import com.example.app.motidron.Model.DAO.ReconocimientoDAO;
import com.example.app.motidron.Model.DTO.ReconocimientoDTO;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebDetection;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.Blob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class GCVActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String CLOUD_VISION_API_KEY ="AIzaSyBXFzvkaAJPTLFdoeO1tLBinDD3FTsZ5us";
    byte[] byteArray;
    private Vision vision;
    private ImageView image;
    private TextView info,latitud, longitud;
    private Button save;
    private String message = "";
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcv);

        image = (ImageView) findViewById(R.id.image);
        info= (TextView) findViewById(R.id.info);
        latitud= (TextView) findViewById(R.id.latitud);
        longitud= (TextView) findViewById(R.id.longitud);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        //recibir un bitmap por extras
        byteArray = getIntent().getByteArrayExtra("imagen");
        bmp = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
        //set bitmap a un ImageView
        image.setImageBitmap(bmp);


        this.realizarConexion();
        this.realizarReconocimiento();

        location loc = new location(this, longitud, latitud);

}



public void realizarConexion(){
    Vision.Builder visionBuilder = new Vision.Builder(
            new NetHttpTransport(),
            new AndroidJsonFactory(),
            null);

    visionBuilder.setVisionRequestInitializer(
            new VisionRequestInitializer(CLOUD_VISION_API_KEY));

    vision = visionBuilder.build();
}



public void realizarReconocimiento(){
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            //La API espera una imagen codificada conmo una cadena Base64 que seencuentra dentro de un objeto Image
            //Primero hay que convertir la imagen en un  byteArray
            //(la imagene viene como un biteArray del extra de la activity anterior)

            Image inputImage = new Image();
            inputImage.encodeContent(byteArray);

            //Se debe crear un objeto feature, es cual especifica el tipo de reconocimiento que se va a realizar
            Feature desiredFeature = new Feature();
            desiredFeature.setType("WEB_DETECTION");

            //Con la imagen y el objeto feature se crea una instancia de AnnotateImageRequest
            AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(inputImage);
            request.setFeatures(Arrays.asList(desiredFeature));

            //un objeto de AnnotateImageRequest debe pertenecer a un objeto de BatchAnnotateImagesRequest,
            //pues la API esta hecha para procesar multiples imagenes a la vez.

            //Inicializando un BatchAnnotateImageRequest:
            BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
            batchRequest.setRequests(Arrays.asList(request));

            //Se obtiene un objeto BatchAnnotateImageRequest como respuesta de la API
            //por ser una peticion del tipo "WEB_DETECTION" se obtiene como respuesta un objeto WebDetection

            //Se inicializa un BatchAnnotateImageResponse:
            BatchAnnotateImagesResponse batchResponse = null;
            try {
                batchResponse = vision.images().annotate(batchRequest).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //se obtienen las respuestas delreconocimiento
            //en caso del reconocimiento web se obtiene una sola respuesta (posición 0)
            WebDetection webDetections = batchResponse.getResponses().get(0).getWebDetection();
            //se agrega la información obtenida de la API en una variable para el mensaje
            if(webDetections!=null){
                for(int i = 0;i<webDetections.getWebEntities().size();i++){
                    message+=webDetections.getWebEntities().get(i).getDescription()+"\n";
                }
            }else{
                message="No se encontro ningún resultado en la búsqueda";
            }

            final String m =message;
            //Ya que el hilo de ejecución actual, es el utilizado para la conexión a internet,
            //se debe utilizar uno diferente para darle valor al TextView de la vista y mostrar la
            // información
            //a su vez se habilita el boton de guardar información, para evitar errores al momento
            //de acceder a datos inexistentes
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    info.setText(m);
                    save.setVisibility(View.VISIBLE);
                }
            });

        }
    });
}

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(GCVActivity.this,MainActivity.class);
        switch (v.getId()){
            case R.id.save:
                AsyncTask.execute(new Runnable() {
                                      @Override
                                      public void run() {
                                          try {
                                              guardarData(latitud.getText().toString(), longitud.getText().toString(), message, "Google Cloud Vision");
                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }

                                      }
                                  });
                startActivity(intent);
                finish();
                break;
        }
    }


public int guardarData(String latitud, String longitud, String message, String api) throws Exception {

        ReconocimientoDTO dto = new ReconocimientoDTO(byteArray, latitud, longitud, message, api);
        ReconocimientoDAO dao = new ReconocimientoDAO();
        return dao.registrarReconocimiento(dto);
}



}
