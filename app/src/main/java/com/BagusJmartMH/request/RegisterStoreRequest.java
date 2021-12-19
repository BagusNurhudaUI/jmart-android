package com.BagusJmartMH.request;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * merupakan request yang digunakan untuk  register store
 */
public class RegisterStoreRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8084/account/%d/registerStore";
    private final Map<String, String> params;

    public RegisterStoreRequest(int id, String StoreName, String StoreAddress, String StorePhone, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST, String.format(URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("name", StoreName);
        params.put("address", StoreAddress);
        params.put("phoneNumber", StorePhone);

    }

    public Map<String, String> getParams(){

        return params;
    }

}