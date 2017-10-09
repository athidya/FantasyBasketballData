package com.example.athidya.mydataapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.security.*;


import static com.example.athidya.mydataapp.R.id.textView;

public class code extends AppCompatActivity {

    //initiators for params needed in HTTP POST and GET requests
    String code = "";
    String CONSUMER_KEY = "";
    String CONSUMER_SECRET = "";
    String access_token, id_token, expires_in, token_type, refresh_token, xoauth_yahoo_guid = "";
    String jose_header, payload, signature = "";
    String JHalg, JHkid, respalg1, respalg2, respkid1, respkid2, thealg, thekid = "";
    String[] KeyObj;
    String xstr, ystr = "";


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

        final Button nextButton = (Button) findViewById(R.id.button7);

        //editable text field for user to input given code & limited to 7 characters b/c code length is always the same
        final EditText entercode = (EditText) findViewById(R.id.editText);
        entercode.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //NEXT button that initiates trying to get token from yahoo given the code *working*
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //login webview attempt
                code = entercode.getText().toString(); //reads code entered in by the user to use for validation
                tokenReq(queue); //initiates token request using given code
            }
        });

        final Button valButton = (Button) findViewById(R.id.button2);
        valButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationReq(queue); //takes given token and validates it
            }
        });

        final Button inforeq = (Button) findViewById(R.id.button3);
        inforeq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url ="https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasy" +
                        "sports.players.ownership%20where%20player_key%3D%27238.p.6619%27%20and%20league_key%3D%27238.l.627060%27&diagnostics=true";
                // Request a string response from the provided URL.
                StringRequest jsonReq = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("Data call back", response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("data error","That didn't work!");
                        error.printStackTrace();
                    }
                }){/*
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("client_id", CONSUMER_KEY);
                        params.put("client_secret", CONSUMER_SECRET);
                        return params;
                    }*/
                    //parameters for request header
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
        });
    }

    public void tokenReq(final RequestQueue queue) {
        String url = "https://api.login.yahoo.com/oauth2/get_token";
        final TextView prompt = (TextView) findViewById(textView);

        //set up request HTTP POST with header and body params
        StringRequest tokenreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            //response handler
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                decodeResponse(response); //assigns TokenResponse values to the necessary parameters eg. access_token, id_token and such
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                prompt.setText(error.toString());
            }
        }){
            //parameters for request body
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", "oob");
                params.put("code", code);
                params.put("client_id", CONSUMER_KEY);
                params.put("client_secret", CONSUMER_SECRET);
                return params;
            }
            //parameters for request header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic " + "");
                return headers;
            }
        };
        queue.add(tokenreq); //start request
    }


    /*
    separates values from response given by our request, assigns them to local variables
    also decodes id_token values from base64 encoding
     */
    public void decodeResponse(String Tokenresponse) {
        String[] response = Tokenresponse.split(",");
        access_token = clean(response[0]);
        Log.d("access_token", access_token);
        refresh_token = clean(response[1]);
        expires_in = clean(response[2]);
        token_type = clean(response[3]);
        xoauth_yahoo_guid = clean(response[4]);
        id_token = clean(response[5]);
        //handle id_token params and decode them from base64 encoding
        String[] temp = id_token.split(Pattern.quote("."));
        byte[] jose = Base64.decode(temp[0], Base64.DEFAULT);
        try {
            jose_header = new String(jose, "UTF-8");
            String[] subtemp = jose_header.split(Pattern.quote(","));
            JHalg = clean(subtemp[0]);
            JHkid = clean(subtemp[1]);
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] pay = Base64.decode(temp[1], Base64.DEFAULT);
        try {
            payload = new String(pay, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        temp[2] = temp[2].replaceAll("\\}", "");
        signature = temp[2];
    }



    //repetitive splitting and assignment part of decodeResponse
    public String clean(String rep) {
        String[] temp = rep.split(":");
        return temp[1].replace("\"", "");
    }

    //request for getting the public keys
    public void validationReq(RequestQueue queue) {
        final TextView prompt = (TextView) findViewById(textView);
        String valUrl = "https://login.yahoo.com/openid/v1/certs";
        StringRequest valReq = new StringRequest(Request.Method.GET, valUrl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Validation Response", response);
                validate(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    prompt.setText("errorrrr");
                }
        });
        queue.add(valReq);
    }
    //checking for which public key corresponds to our key given by jose_header
    public void validate(String response) {
        String[] temp = response.split("\\},\\{");
        temp[0] = temp[0].replace("\"keys\":[\\{", "");
        String[] Obj1 = temp[0].split(",");
        String[] Obj2 = temp[1].split(",");
        respalg1 = clean(Obj1[1]);
        respalg2 = clean(Obj2[1]);
        //assignment the matching object to KeyObj for later comparison
        if (respalg1.equals(JHalg)) {
            KeyObj = Obj1;
            Log.d("compare", "Obj1");
        }
        else if (respalg2.equals(JHalg)) {
            KeyObj = Obj2;
            Log.d("compare", "Obj2");
        }
        else {Log.d("Error", "error in alg comparison");}

        //checking for cryptographic algo type to decode x & y using
        thealg = clean(KeyObj[1]);
        if(thealg.equals("ES256")) {
            //use ES256 crypt key
        }
        else if (thealg.equals("RS256")) {
            //use RS256 crypt key
        }
    }
    public boolean validateSignature(String x, String y) throws Throwable{
        byte[] xbyte = Base64.decode(x, Base64.DEFAULT);
        try {
            xstr = new String(xbyte, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] ybyte = Base64.decode(x, Base64.DEFAULT);
        try {
            ystr = new String(ybyte, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return true;
    }
}
