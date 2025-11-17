package com.example.safesip;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MotivQuoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motiv_quote);

        TextView tv = findViewById(R.id.text_motivquote);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Response.Listener<String> responseListener = new MotivQuoteResponseListener(tv);
        Response.ErrorListener errorListener = new MotivQuoteErrorListener();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://zenquotes.io/api/quotes",
                responseListener,
                errorListener
        );

        requestQueue.add(request);
    }
}