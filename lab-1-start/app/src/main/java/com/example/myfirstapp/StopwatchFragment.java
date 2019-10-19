package com.example.myfirstapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopwatchFragment extends Fragment {
    public final String TAG = "MAIN";
    public TextView tvTime;
    public Button btnSt;
    public Button btnPR;
    public Button btnRes;
    public long seconds ;
    public boolean run ;
    public boolean stop ;

    public String timeString;

    Context thisContext;

    Thread stopWatch = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                tvTime.post(new Runnable() {
                    @Override
                    public void run() {
                        if(run)seconds++;
                        if(stop) seconds = 0;
                        long milli = seconds % 100;
                        long sec = (seconds / 100) % 60;
                        long min = (seconds / 6000) % 60 ;

                        timeString = (min<10 ? "0" + min : min)  + ":" + (sec<10 ? "0" + sec : sec) + ":" + (milli<10 ? "0" + milli : milli);
                        tvTime.setText( timeString );
                    }
                });
                try {
                    Thread.sleep(10);
                } catch(InterruptedException e) {
                    Log.d(TAG, "got interrupted!");
                }
            }
        }
    });


    public StopwatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadUserData();
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        super.onCreate(savedInstanceState);
        thisContext = container.getContext();


        tvTime = (TextView) view.findViewById(R.id.tvTime);
        btnSt = (Button) view.findViewById(R.id.btnSt);
        btnPR = (Button) view.findViewById(R.id.btnPR);
        btnRes = (Button) view.findViewById(R.id.btnRes);

        if(!stopWatch.isAlive())stopWatch.start();

        btnSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: start button");
                run = true;
                stop = false;
                if(!stopWatch.isAlive())stopWatch.start();
                saveUserData();
            }
        });
        btnPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pause/resume button");
                run = !run;
                saveUserData();
            }
        });
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: reset button");
                stop = true;
                run = false;
                saveUserData();
                saveInstanceData();
            }
        });
        return view;
    }

    private void saveUserData() {
        String state_run = getString(R.string.stopwatch_run);
        String state_stop = getString(R.string.stopwatch_stop);
        String state_time = getString(R.string.stopwatch_time);
        SharedPreferences statePrefs = this.getActivity().getSharedPreferences(state_run, MODE_PRIVATE);
        SharedPreferences.Editor editor = statePrefs.edit();

        editor.clear();
        editor.putBoolean(state_run, run);
        editor.putBoolean(state_stop, stop);
        editor.putLong(state_time, seconds);
        editor.commit();
        return;
    }

    private void loadUserData() {
        String state_run = getString(R.string.stopwatch_run);
        String state_stop = getString(R.string.stopwatch_stop);
        String state_time = getString(R.string.stopwatch_time);



        SharedPreferences statePrefs = this.getActivity().getSharedPreferences(state_run, MODE_PRIVATE);

        run = statePrefs.getBoolean(state_run, false);
        stop = statePrefs.getBoolean(state_stop, false);
        seconds = statePrefs.getLong(state_time, 0);

        return;
    }


    private void saveInstanceData() {
        String date = Calendar.getInstance().getTime().toString();
        final BuckyMobileDBHelper dbHelper = new BuckyMobileDBHelper(thisContext);
        final SQLiteDatabase writeDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WatchInstanceContract.WatchEntry.COLUMN_NAME_DATE, date);
        values.put(WatchInstanceContract.WatchEntry.COLUMN_NAME_SECOND, seconds);
        values.put(WatchInstanceContract.WatchEntry.COLUMN_NAME_HAS_POSTED, 0); // where 0 is false for sqlite
        long newRowId = writeDB.insert(WatchInstanceContract.WatchEntry.TABLE_NAME, null, values);
        writeDB.close();
    }



}
