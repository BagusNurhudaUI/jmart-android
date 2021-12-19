package com.BagusJmartMH.request;

import com.android.volley.Response;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * request yang digunakan utnuk men-submit payment dari order
 */
public class SubmitPaymentRequest extends StringRequest {
    private static final String SUBMIT_URL = "http://10.0.2.2:8084/payment/%d/submit";
    private final Map<String, String> params;
    public SubmitPaymentRequest(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(SUBMIT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("receipt", receipt);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
