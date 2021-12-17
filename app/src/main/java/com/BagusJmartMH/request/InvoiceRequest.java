package com.BagusJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InvoiceRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:8084/payment/%s?%s=%s";
    private final Map<String, String> params;

    public InvoiceRequest(int id, boolean byAccount, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, byAccount ? "getByAccountId" : "getByStoreId", byAccount ? "buyerId" : "storeId", id), listener, errorListener);
        params = new HashMap<>();
        params.put(byAccount ? "buyerId" : "storeId", String.valueOf(id));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
