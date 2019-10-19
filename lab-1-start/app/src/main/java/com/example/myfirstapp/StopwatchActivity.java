package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StopwatchActivity extends AppCompatActivity {

    public final String TAG = "MAIN";
    public TextView tvTime;
    public Button btnSt;
    public Button btnPR;

    public Button btnRes;
    public Button btnReturn;
    public long seconds = 0 ;
    public boolean run = false;
    public boolean stop = false;

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

                        tvTime.setText( (min<10 ? "0" + min : min)  + ":" + (sec<10 ? "0" + sec : sec) + ":" + (milli<10 ? "0" + milli : milli) );
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        tvTime = (TextView) findViewById(R.id.tvTime);
        btnSt = (Button) findViewById(R.id.btnSt);
        btnPR = (Button) findViewById(R.id.btnPR);
        btnRes = (Button) findViewById(R.id.btnRes);
        btnReturn = (Button) findViewById(R.id.btnReturn);

        btnSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: start button");
                run = true;
                stop = false;
                if(!stopWatch.isAlive())stopWatch.start();
            }
        });
        btnPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pause/resume button");
                run = !run;
            }
        });
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: reset button");
                stop = true;
                run = false;
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: return button");
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity((intent));
            }
        });
    }

}
