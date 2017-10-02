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
import android.util.Base64;

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


import static android.R.attr.prompt;
import static android.R.attr.tag;
import static com.example.athidya.mydataapp.R.id.textView;

public class code extends AppCompatActivity {

    //initiators for params needed in HTTP POST and GET requests
    String code = "";
    String CONSUMER_KEY = "dj0yJmk9aFl3bUFQTmdUUzdFJmQ9WVdrOWFGaHRObTlzTmpJbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD02MA--";
    String CONSUMER_SECRET = "e11fcfa6ffaa42d031421af91dbbd1ee2f7ffb41";
    String access_token = ""; String id_token = ""; String expires_in = ""; String token_type = ""; String refresh_token=""; String xoauth_yahoo_guid="";
    String jose_header = ""; String payload = ""; String signature = "";

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
                validation(queue); //takes given token and validates it
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
                params.put("client_secrect", CONSUMER_SECRET);
                return params;
            }
            //parameters for request header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Basic " + "ZGoweUptazlhRmwzYlVGUVRtZFVVemRGSm1ROVdWZHJPV0ZHYUhST2JUbHpUbXBKYldOSGJ6bE5RUzB0Sm5NOVkyOXVjM1Z0WlhKelpXTnlaWFFtZUQwMk1BLS06ZTExZmNmYTZmZmFhNDJkMDMxNDIxYWY5MWRiYmQxZWUyZjdmZmI0MQ==");
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
        access_token = getres(response[0]);
        refresh_token = getres(response[1]);
        expires_in = getres(response[2]);
        token_type = getres(response[3]);
        xoauth_yahoo_guid = getres(response[4]);
        id_token = getres(response[5]);
        String[] temp = id_token.split(".");
        Log.d("id_token", id_token);
        jose_header = Base64.decode(temp[0], Base64.DEFAULT).toString(); //giving null error currently :(
        payload = Base64.decode(temp[1], Base64.DEFAULT).toString();
        temp[2] = temp[2].replace("}", "");
        signature = Base64.decode(temp[2], Base64.DEFAULT).toString();

    }
    //repetitive splitting and assignment part of decodeResponse
    public String getres (String rep) {
        String[] temp = rep.split(":");
        return temp[1].replace("\"", "");
    }

    //used to validate id_token *work in progress*
    public void validation(RequestQueue queue) {
        final TextView prompt = (TextView) findViewById(textView);
        String valUrl = "https://login.yahoo.com/openid/v1/certs";
        StringRequest valReq = new StringRequest(Request.Method.GET, valUrl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                prompt.setText(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    prompt.setText("errorrrr");
                }
        });
        queue.add(valReq);
    }
}
