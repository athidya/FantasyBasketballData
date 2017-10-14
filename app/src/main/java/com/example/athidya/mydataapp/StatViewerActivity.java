package com.example.athidya.mydataapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.athidya.mydataapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.*;

public class StatViewerActivity extends AppCompatActivity {

    String access_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle info = getIntent().getExtras();
        access_token = info.getString("access_token");
        Log.d("access token",access_token);
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        initialStatView(queue);
    }

    public void initialStatView(RequestQueue queue) {
        String queryurl ="https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games;game_keys=375/players";
        // Request a string response from the provided URL.
        StringRequest jsonReq = new StringRequest(Request.Method.GET, queryurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // displays all recieved information correctly in the Log without cutoff
                        List<String> printlog = new ArrayList<String>();
                        int index = 0;
                        while (index < response.length()) {
                            printlog.add(response.substring(index, Math.min(index + 2000, response.length())));
                            index +=2000;
                        }
                        for (int i = 0; i<printlog.size(); i++) {
                            Log.d("data call", printlog.get(i));
                        }
                        //Log.d("Data call back", response);
                        JSONObject jsonresponse = null;
                        try {
                            jsonresponse = XML.toJSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("json", jsonresponse.toString());
                        responseParse(jsonresponse);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("data error","That didn't work!");
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + access_token);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsonReq);
    }

    public void responseParse(JSONObject response) {

    }
}
