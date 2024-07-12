package com.example.snackattack_2.Game_2.utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDetector {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private CallBack_Movement callback_movement;
    private long timeStamp = 0;

    public SensorDetector(Context context, CallBack_Movement callback_movement) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.callback_movement = callback_movement;

    }

    public interface CallBack_Movement {
        void moveRight();

        void moveLeft();
    }

    public void start() {
        sensorManager.registerListener(sensorEventListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void stop() {
        sensorManager.unregisterListener(sensorEventListenerAccelerometer);
    }

    SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            calculateMovement(x);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void calculateSideMovement(float x) {

        if (x < -2.5) {
            if (callback_movement != null) {
                callback_movement.moveRight();
            }
        }

        if (x > 2.5) {
            if (callback_movement != null) {
                callback_movement.moveLeft();
            }
        }
    }

    private void calculateMovement(float x) {
        if (System.currentTimeMillis() - timeStamp > 500) {
            timeStamp = System.currentTimeMillis();
            calculateSideMovement(x);
        }
    }


}
