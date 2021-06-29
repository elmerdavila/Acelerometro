package com.summer.orientacion_danp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager miManager;
    private Sensor miSensorAcelerometro;

    private TextView valuex;
    private TextView valuey;
    private TextView valuez;

    private TextView maxX;
    private TextView maxY;
    private TextView maxZ;
    private Button reiniciar;

    private float maximox;
    private float maximoy;
    private float maximoz;

    float gravity[]={0,0,0};
    float linearAceleration[]={0,0,0};
    @Override
    protected void onResume() {
        super.onResume();
        miManager.registerListener(this,miSensorAcelerometro,SensorManager.SENSOR_DELAY_NORMAL);
        maximox=0;
        maximoy=0;
        maximoz=0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        miManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valuex=findViewById(R.id.valorx);
        valuey=findViewById(R.id.valory);
        valuez=findViewById(R.id.valorz);

        maxX=findViewById(R.id.maxX);
        maxY=findViewById(R.id.maxY);
        maxZ=findViewById(R.id.maxZ);
        reiniciar=findViewById(R.id.button);

        reiniciar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                maxX.setText("0");
                maxY.setText("0");
                maxZ.setText("0");
                maximox=0;
                maximoy=0;
                maximoz=0;
            }
        });


        miManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        miSensorAcelerometro=miManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float[] rotationMatrix = new float[9];

        final float alpha=0.8f;
        gravity[0]=alpha * gravity[0] + (1-alpha) * event.values[0];
        gravity[1]=alpha * gravity[1] + (1-alpha) * event.values[1];
        gravity[2]=alpha * gravity[2] + (1-alpha) * event.values[2];

        linearAceleration[0]=event.values[0] - gravity [0];
        linearAceleration[1]=event.values[1] - gravity [1];
        linearAceleration[2]=event.values[2] - gravity [2];

        DecimalFormat format = new DecimalFormat("0.00");
        valuex.setText(""+format.format(linearAceleration[0]));
        valuey.setText(""+format.format(linearAceleration[1]));
        valuez.setText(""+format.format(linearAceleration[2]));


        evaluarMaximos(linearAceleration[0],linearAceleration[1],linearAceleration[2]);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void evaluarMaximos( float x, float y, float z){
        if(Math.abs(x)>Math.abs(maximox)){
            maximox=x;
        }
        if (Math.abs(x)>Math.abs(maximoy)){
            maximoy=y;
        }
        if (Math.abs(z)>Math.abs(maximoz)){
            maximoz=z;
        }
        maxX.setText(String.valueOf(maximox));
        maxY.setText(String.valueOf(maximoy));
        maxZ.setText(String.valueOf(maximoz));
    }
}