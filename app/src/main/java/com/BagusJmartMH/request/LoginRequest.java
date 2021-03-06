package com.BagusJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * untuk melakukan request login
 * @param <listener> untuk mendapatkan hasil listener
 */
public class LoginRequest<listener> extends StringRequest{
    private static final String URL = "http://10.0.2.2:8084/account/login";
    private final Map<String,String> params;

    /**
     * merupakan login request
     * @param email
     * @param password
     * @param listener
     * @param errorListener
     */
    public LoginRequest( String email,String password,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener){
        super(Method.POST,URL,listener,errorListener);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String,String> getParams(){
        return params;
    }
}