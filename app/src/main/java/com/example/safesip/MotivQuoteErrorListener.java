package com.example.safesip;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Objects;

public class MotivQuoteErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("MotivQuote", Objects.requireNonNull(error.getMessage()));
        Log.i("ERROR", "Erreur API : " + error.toString());
    }
}
