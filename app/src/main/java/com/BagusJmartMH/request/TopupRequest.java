package com.BagusJmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TopupRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8084/account/%d/topUp";
    private final Map<String, String> params;

    public TopupRequest(int id, double balance, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST, String.format(URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("balance", String.valueOf(balance));

    }

    public Map<String, String> getParams(){

        return params;
    }
}
