package com.example.safesip;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MotivQuoteResponseListener implements Response.Listener<String> {

    private TextView tv;
    public MotivQuoteResponseListener(TextView textView) {
        tv = textView;
    }

    @Override
    public void onResponse(String response ) {

        try {
            JSONArray array = new JSONArray(response);

            int randomIndex = (int) (Math.random() * array.length());

            JSONObject obj = array.getJSONObject(randomIndex);

            Log.i("CHECK", "onResponse CALLED !");


            String quote = obj.getString("q");
            String author = obj.getString("a");

            Log.i("quote", quote);
            Log.i("author", author);


            tv.setAlpha(0f);
            tv.setText(quote + "\n" + "\n"  + "—" + author );
            tv.animate().alpha(1f).setDuration(600);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}
