package com.BagusJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AccPaymentRequest extends StringRequest {
    private static final String ACCEPT_URL = "http://10.0.2.2:8084/payment/%d/accept";
    private final Map<String, String> params;

    public AccPaymentRequest(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(ACCEPT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
