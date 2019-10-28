package com.example.urlshortener;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class ApiRequestManager {

    private String apiUrl = "https://rel.ink/api/links/";
    private RequestQueue queue;
    private onLinkShortenedListener onLinkShortenedListener;
    private onLinkReadListener onLinkReadListener;

    public void setOnLinkShortenedListener(ApiRequestManager.onLinkShortenedListener onLinkShortenedListener) {
        this.onLinkShortenedListener = onLinkShortenedListener;
    }

    public void setOnLinkReadListener(ApiRequestManager.onLinkReadListener onLinkReadListener) {
        this.onLinkReadListener = onLinkReadListener;
    }

    public interface onLinkShortenedListener {
        void onLinkShortened(String hashId);
    }

    public interface onLinkReadListener {
        void onLinkRead(String url);
    }

    ApiRequestManager() {
        this.onLinkReadListener = null;
        this.onLinkShortenedListener = null;
    }

    public void createShortenedLink(Context context, String url) {

        final String hashId = null;
        HashMap<String, String> data = new HashMap<>();
        data.put("url", url);

        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("log", "success");

                        // присвоить значение hashId

                        if (onLinkShortenedListener != null)
                            onLinkShortenedListener.onLinkShortened(hashId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("log", "response error on shortening");
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    public void readShortenedLink(Context context, String hashId) {
        queue = Volley.newRequestQueue(context);

        final String url = null;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, apiUrl + hashId + "/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // присвоить значение url

                        if (onLinkReadListener != null)
                            onLinkReadListener.onLinkRead(url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("log", "response error on read");
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }
}
