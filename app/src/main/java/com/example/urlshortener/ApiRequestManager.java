package com.example.urlshortener;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
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
        void onLinkShortened(Link link_with_hashId);
    }

    public interface onLinkReadListener {
        void onLinkRead(Link link_with_url);
    }

    ApiRequestManager() {
        this.onLinkReadListener = null;
        this.onLinkShortenedListener = null;
    }

    public void createShortenedLink(Context context, final Link url) {

        HashMap<String, String> data = new HashMap<>();
        data.put("url", url.getUrl());

        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            url.setHashId(response.getString("hashid"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("log", "url set hash id problem");
                        }
                        if (onLinkShortenedListener != null)
                            onLinkShortenedListener.onLinkShortened(url);
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

    public void readShortenedLink(Context context, final Link hashId) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, apiUrl + hashId.getHashId() + "/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // присвоить значение url
                        try {
                            hashId.setUrl(response.getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("log", "set hash id problem");
                        }
                        if (onLinkReadListener != null)
                            onLinkReadListener.onLinkRead(hashId);
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
