package com.example.myfirstapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Context thisContext;

    ArrayList<StepsInstance> stepsList;
    ArrayList<WatchInstance> timeList;

    public final String TAG = "MAIN";
    public Button btnToStopwatch;
    public Button btnShowMenu;
    public HomeFragment() {
        // Required empty public constructor
    }

    public StepsAdapter adapter;
    public RecyclerView recyclerView;
    public RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        stepsList = new ArrayList<>();
        timeList = new ArrayList<>();
        thisContext= container.getContext();




//        btnToStopwatch = view.findViewById(R.id.btnToStopwatch);
//        btnToStopwatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), StopwatchActivity.class);
//                startActivity(intent);
//            }
//        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_home);
        String ApiURL = "https://dev.bucky-mobile.com/epsilon/steps";

        JsonObjectRequest stepsJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resp = (JSONArray) response.get("instances");
                            for (int i = 0; i < resp.length(); i++){
                                JSONObject instance = (JSONObject) resp.get(i);
                                String date = (String)instance.get("date");
                                int steps = (int) instance.get("steps");
                                StepsInstance newInstance = new StepsInstance();
                                newInstance.setTime(date);
                                newInstance.setSteps(steps);
                                stepsList.add(newInstance);
                            }
                            adapter = new StepsAdapter(stepsList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException exception) {
                            Log.d(TAG, "JSON EXCEPTION for request");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                });


//        JsonObjectRequest watchJsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, ApiURL, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray resp = (JSONArray) response.get("watchInstances");
//                            for (int i = 0; i < resp.length(); i++){
//                                JSONObject instance = (JSONObject) resp.get(i);
//                                String date = (String)instance.get("date");
//                                long second = (long) instance.get("time");
//                                WatchInstance newInstance = new WatchInstance();
//                                newInstance.setDate(date);
//                                newInstance.setSeconds(second);
//                                timeList.add(newInstance);
//                            }
//                            WatchAdapter adapter = new WatchAdapter(timeList);
//                            recyclerView.setAdapter(adapter);
//
//                        } catch (JSONException exception) {
//                            Log.d(TAG, "JSON EXCEPTION for request");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "onErrorResponse: " + error.toString());
//                    }
//                });
        queue = Volley.newRequestQueue(thisContext);
        queue.add(stepsJsonObjectRequest);
        //queue.add(watchJsonObjectRequest);



        btnShowMenu = (Button) view.findViewById(R.id.btn_show_dropdown);
        btnShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(thisContext, btnShowMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.home_drop_down, dropDownMenu.getMenu());
                btnShowMenu.setText("Choose Data to View");
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(thisContext, "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();

                        switch ((String)menuItem.getTitle()){
                            case "Steps Data":
                                //loadStepInstanceData();
                                StepsAdapter stepsAdapter = new StepsAdapter(stepsList);
                                recyclerView.setAdapter(stepsAdapter);
                                break;
                            case "Stopwatch Data":
                                //loadWatchInstanceData();
                                WatchAdapter watchAdapter = new WatchAdapter(timeList);
                                recyclerView.setAdapter(watchAdapter);
                                break;
                            default:

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));


                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        return view;
    }
//    private void loadStepInstanceData() {
//        String filename = "step_storage";
//        FileInputStream inputStream;
//        int ch;
//        StringBuffer fileContent = new StringBuffer("");
//        try {
//            inputStream = this.getActivity().openFileInput(filename);
//            try {
//                while( (ch = inputStream.read()) != -1)
//                    fileContent.append((char)ch);
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException ioerror) {
//            ioerror.printStackTrace();
//        }
//        String data = new String(fileContent);
//        String[] lines= data.split("\n");
//        for (int i = 0; i < lines.length; i ++) {
//            Log.d(TAG, "loadStepInstanceData: " + lines[i].toString());
//        }
//        return;
//    }
    private void loadStepInstanceData() {
        final BuckyMobileDBHelper dbHelper = new BuckyMobileDBHelper(thisContext);
        SQLiteDatabase readDB = dbHelper.getReadableDatabase();
        String[] projection = {
                StepsInstanceContract.StepsEntry.COLUMN_NAME_DATE,
                StepsInstanceContract.StepsEntry.COLUMN_NAME_STEPS,
                StepsInstanceContract.StepsEntry.COLUMN_NAME_HAS_POSTED,
        };
        String sortOrder = StepsInstanceContract.StepsEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = readDB.query(StepsInstanceContract.StepsEntry.TABLE_NAME, projection,
                null, null, null, null, sortOrder);
        stepsList.clear();
        while (cursor.moveToNext()) {
            StepsInstance si = new StepsInstance();
            si.setTime(cursor.getString(0));
            si.setSteps(cursor.getInt(1));

            stepsList.add(si);
        }
    }
    private void loadWatchInstanceData() {
        final BuckyMobileDBHelper dbHelper = new BuckyMobileDBHelper(thisContext);
        SQLiteDatabase readDB = dbHelper.getReadableDatabase();
        String[] projection = {
                WatchInstanceContract.WatchEntry.COLUMN_NAME_DATE,
                WatchInstanceContract.WatchEntry.COLUMN_NAME_SECOND,
                WatchInstanceContract.WatchEntry.COLUMN_NAME_HAS_POSTED,
        };
        String sortOrder = WatchInstanceContract.WatchEntry.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = readDB.query(WatchInstanceContract.WatchEntry.TABLE_NAME, projection,
                null, null, null, null, sortOrder);
        timeList.clear();
        while (cursor.moveToNext()) {
            WatchInstance wi = new WatchInstance();
            wi.setDate(cursor.getString(0));
            wi.setSeconds(cursor.getLong(1));
            timeList.add(wi);
        }
    }


}
