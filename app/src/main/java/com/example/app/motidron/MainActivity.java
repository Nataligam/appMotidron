package com.example.app.motidron;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton botonGCV,botonIBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonGCV = (ImageButton) findViewById(R.id.botonGCV);
        botonGCV.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,PhotoSelectionActivity.class);
        switch(v.getId()){
            case R.id.botonGCV:
                intent.putExtra("nube","GCV");
                startActivity(intent);
                finish();
                break;

            //Aca irán los case para cada opcion de nube publica, en este caso solo se ha utilizado la de google
        }
    }
}
