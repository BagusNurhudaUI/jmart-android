package com.BagusJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * digunakan untuk mendapatkan request payment
 */
public class DeclinePaymentRequest extends StringRequest {
    public static final String CANCEL_URL = "http://10.0.2.2:8084/payment/%d/cancel";
    private final Map<String, String> params;

    public DeclinePaymentRequest(Response.Listener<String> listener , int id, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(CANCEL_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
