package com.danielrutz.acceleratortest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private Sensor mAccelerometer;
    private Sensor mTempSensor;
    private SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mTempSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccelerometer != null)
            mSensorManager.registerListener(this, mAccelerometer, 100000000, SensorManager.SENSOR_DELAY_UI);
        if (mTempSensor != null)
            mSensorManager.registerListener(this, mTempSensor, 1000000000, SensorManager.SENSOR_DELAY_UI);
        else
            ((TextView) findViewById(R.id.tempContent)).setText("Could not find temperature sensor!");

        if (mLocationManager != null)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 0, this);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.timeContent)).setText(mSDF.format(new Date()));
                    }
                });
            }
        }, 1000, 1000);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                ((TextView) findViewById(R.id.xContent)).setText(String.format(Locale.US, "%.2f", event.values[0]));
                ((TextView) findViewById(R.id.yContent)).setText(String.format(Locale.US, "%.2f", event.values[1]));
                ((TextView) findViewById(R.id.zContent)).setText(String.format(Locale.US, "%.2f", event.values[2]));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                ((TextView) findViewById(R.id.tempContent)).setText(String.format(Locale.US, "%.2fK", event.values[0] + 273.15));
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ((TextView) findViewById(R.id.latitudeContent)).setText(String.format("%.4f°", location.getLatitude()));
        ((TextView) findViewById(R.id.longitudeContent)).setText(String.format("%.4f°", location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}