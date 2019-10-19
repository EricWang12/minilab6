package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    public String TAG = "STEPCOUNTER";


    private boolean start;

    private int steps = 0;
    private String orientation;
    public TextView tvStepCount;
    public TextView tvStepStatus;

    public TextView tvStepDirection;
    public Button btnStepStart;
    public Button btnStepStop;
    public Button btnStepReset;
    public Context thisContext;
    boolean locked;


    private SensorManager sensorManager;

    public StepCounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We won't use this, but it's required to be implemented
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerationHandler(event);
        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            orientationHandler(event);
        }
    }
    private void accelerationHandler(SensorEvent event){
        float Ax = event.values[0];
        float Ay = event.values[1];
        float Az = event.values[2];
        float A2 = Ax * Ax + Ay * Ay + Az * Az;
        float HIgherThreshold =  (float)this.getResources().getInteger( R.integer.HigherThreshold);
        float LowerThreshold  =  (float)this.getResources().getInteger( R.integer.LowerThreshold);

        String outputString = "accelerationHandler:" +
                " A2: " + String.valueOf(A2) +
                " x: " + String.valueOf(Ax) +
                " y: " + String.valueOf(HIgherThreshold) +
                " z: " + String.valueOf(LowerThreshold);




        if(A2 > HIgherThreshold && !locked){
            locked = true;

        }
        if(A2 < LowerThreshold && locked){
            locked = false;

            if(start)steps++;
            saveUserData();
            tvStepCount.post(new Runnable() {
                @Override
                public void run() {
                    tvStepCount.setText( steps +"");
                }
            });

        }

        //Log.d(TAG, outputString);
    }
    private void orientationHandler(SensorEvent event) {
        float degree = event.values[0];
// TODO: Develop the Orientation output with values above
        //Log.d(TAG, "orientationHandler: degree:" + degree);

        if(degree > 45 && degree <= 135){
            orientation = "East";
        }else if(degree > 135 && degree <= 225){
            orientation = "South";
        }else if (degree > 225 && degree <= 315){
            orientation = "West";
        }else{
            orientation = "North";
        }
        tvStepDirection.post(new Runnable() {
            @Override
            public void run() {
                tvStepDirection.setText( orientation );
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);
        tvStepCount = view.findViewById(R.id.tvStepCount);
        tvStepStatus = view.findViewById(R.id.tvStepStatus);

        tvStepDirection = view.findViewById(R.id.tvStepDirection);
        btnStepStart = view.findViewById(R.id.btnStepStart);
        btnStepStop = view.findViewById(R.id.btnStepStop);
        btnStepReset = view.findViewById(R.id.btnStepReset);

        thisContext = container.getContext();

        steps = 0;
        start = true;
        loadUserData();
        tvStepStatus.post(new Runnable() {
            @Override
            public void run() {
                tvStepStatus.setText( start ?   "Recording in Progress..." : "Stopped");
            }
        });

        tvStepCount.post(new Runnable() {
            @Override
            public void run() {
                tvStepCount.setText( steps +"");
            }
        });
        // Make sure private SensorManager sensorManager; is in the variable declaration
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        btnStepStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Start");
                start = true;
                saveUserData();
                tvStepStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        tvStepStatus.setText( "Recording in Progress...");
                    }
                });
            }
        });

        btnStepStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Stop");
                start = false;
                saveUserData();
                saveInstanceData();
                tvStepStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        tvStepStatus.setText( "Stopped");
                    }
                });
            }

        });

        btnStepReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steps = 0;
                saveUserData();
                tvStepCount.post(new Runnable() {
                    @Override
                    public void run() {
                        tvStepCount.setText( steps +"");
                    }
                });
                Log.d(TAG, "onClick: Reset");
            }
        });


        return view;
    }

    private void saveUserData() {
        String state = getString(R.string.step_counter_state);
        String stepCount = getString(R.string.step_counter_count);
        SharedPreferences statePrefs = this.getActivity().getSharedPreferences(state, MODE_PRIVATE);
        SharedPreferences.Editor editor = statePrefs.edit();
        editor.clear();
        editor.putBoolean(state, start);
        editor.putInt(stepCount, steps);
        editor.commit();
        return;
    }
    private void loadUserData() {
        String state = getString(R.string.step_counter_state);
        String stepCount = getString(R.string.step_counter_count);
        SharedPreferences statePrefs = this.getActivity().getSharedPreferences(state, MODE_PRIVATE);
        start = statePrefs.getBoolean(state, true);
        steps = statePrefs.getInt(stepCount, 0);
        return;
    }



//    private void saveInstanceData() {
//        String filename = "step_storage";
//        FileOutputStream outputStream;
//        Date currentTime = Calendar.getInstance().getTime();
//        int stepCount = steps;
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("time", currentTime.toString());
//            jsonObject.put("steps", stepCount);
//            outputStream = this.getActivity().openFileOutput(filename, Context.MODE_APPEND);
//            String lineStr = jsonObject.toString() + "\n"; // says end of line
//            outputStream.write(lineStr.getBytes());
//            outputStream.close();
//        } catch (JSONException jsonerror) {
//            Log.d(TAG, "saveInstanceData: json exception");
//        } catch (FileNotFoundException fnferror) {
//            Log.d(TAG, "saveInstanceData: file not found");
//        } catch (IOException ioerror) {
//            Log.d(TAG, "saveInstanceData: IO error");
//        }
//    }
    private void saveInstanceData() {
        String date = Calendar.getInstance().getTime().toString();
        final BuckyMobileDBHelper dbHelper = new BuckyMobileDBHelper(thisContext);
        final SQLiteDatabase writeDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StepsInstanceContract.StepsEntry.COLUMN_NAME_DATE, date);
        values.put(StepsInstanceContract.StepsEntry.COLUMN_NAME_STEPS, steps);
        values.put(StepsInstanceContract.StepsEntry.COLUMN_NAME_HAS_POSTED, 0); // where 0 is false for sqlite
        long newRowId = writeDB.insert(StepsInstanceContract.StepsEntry.TABLE_NAME, null, values);
        writeDB.close();
    }





}

