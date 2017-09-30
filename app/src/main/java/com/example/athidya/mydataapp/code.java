package com.example.athidya.mydataapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.tag;

public class code extends AppCompatActivity {
    String code = "";
    String CONSUMER_KEY = "";
    String CONSUMER_SECRET = "";
    String TokenResponse = "";
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final TextView prompt = (TextView) findViewById(R.id.textView);
        final Button button = (Button) findViewById(R.id.button7);

        final EditText entercode = (EditText) findViewById(R.id.editText);
        entercode.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //login webview attempt
                code = entercode.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://api.login.yahoo.com/oauth2/get_token";
                StringRequest tokenreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                        TokenResponse = response.toString();
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        prompt.setText(error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("grant_type", "authorization_code");
                        params.put("redirect_uri", "oob");
                        params.put("code", code);
                        params.put("client_id", CONSUMER_KEY);
                        params.put("client_secrect", CONSUMER_SECRET);
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        headers.put("Authorization", "Basic " + "ZGoweUptazlhRmwzYlVGUVRtZFVVemRGSm1ROVdWZHJPV0ZHYUhST2JUbHpUbXBKYldOSGJ6bE5RUzB0Sm5NOVkyOXVjM1Z0WlhKelpXTnlaWFFtZUQwMk1BLS06ZTExZmNmYTZmZmFhNDJkMDMxNDIxYWY5MWRiYmQxZWUyZjdmZmI0MQ==");
                        return headers;
                    }
                };
                queue.add(tokenreq);

            }
        });

    }

}
