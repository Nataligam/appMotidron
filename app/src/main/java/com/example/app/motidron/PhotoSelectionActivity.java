package com.example.app.motidron;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class PhotoSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv;
    Button btnTp, btnAna, btnSelect;
    Bitmap mImageBitmap;
    Uri imageUri;


    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);

        iv = (ImageView) findViewById(R.id.imageTaken);
        btnTp = (Button) findViewById(R.id.takePic);
        btnAna = (Button) findViewById(R.id.analizar);
        btnSelect = (Button) findViewById(R.id.select);

        btnTp.setOnClickListener(this);
        btnAna.setOnClickListener(this);
        btnSelect.setOnClickListener(this);



    }





    //intent para lanzarla camara y tomar una foto
    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    //metodo para saber si la app puede soportar el intent
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode== RESULT_OK){
            handleSmallCameraPhoto(data);
        }else if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            iv.setImageURI(imageUri);
            iv.buildDrawingCache();
            mImageBitmap = iv.getDrawingCache();
            btnAna.setVisibility(View.VISIBLE);
        }
    }


    //convierte el bitmap de la imagen obtenida en una imagen para mostrarla en un ImageView
    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        iv.setImageBitmap(mImageBitmap);
        btnAna.setVisibility(View.VISIBLE);
    }

    private void llamarAPINubePublica(){
        Bundle bundle = getIntent().getExtras();
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        mImageBitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[] byteArray = bStream.toByteArray();
        if(bundle!=null){
            String nube = bundle.getString("nube");
            if(nube.equalsIgnoreCase("GCV")){
                Intent in = new Intent(PhotoSelectionActivity.this, GCVActivity.class);
                in.putExtra("imagen",byteArray);
                startActivity(in);
                finish();
            }
        }
    }


    private void abrirGaleria(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.takePic:
                dispatchTakePictureIntent(1);
                break;

            case R.id.analizar:
                llamarAPINubePublica();
                finish();
                break;

            case R.id.select:
                abrirGaleria();
                break;

        }
    }
}
