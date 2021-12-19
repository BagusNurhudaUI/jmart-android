package com.BagusJmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * untuk membuat request payment
 */
public class PaymentRequest extends StringRequest {
    private static final String CREATE_URL = "http://10.0.2.2:8084/payment/create";
    public static final String SUBMIT_URL = "http://10.0.2.2:8084/payment/%d/submit";
    private final Map<String, String> params;

    /**
     * untuk membuat payment
     * @param buyerId merupakan id dari buyer
     * @param productId id dari product
     * @param productCount total product yang dibeli
     * @param shipmentAddress address untuk shipment
     * @param shipmentPlan tipe pengiriman
     * @param storeId id dari storenya
     * @param listener merupakan parameter untuk listtener
     * @param errorListener merupakan paramater untuk error listenernya
     */

    public PaymentRequest(int buyerId, int productId, int productCount, String shipmentAddress, byte shipmentPlan, int storeId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, CREATE_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
        params.put("storeId", String.valueOf(storeId));
    }

    /**
     * untuk submit payment
     * @param id id untuk buyer
     * @param receipt parameter untuk nomor resi
     * @param listener merupakan parameter untuk listtener
     * @param errorListener merupakan paramater untuk error listenernya
     */
    public PaymentRequest(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(SUBMIT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("receipt", receipt);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
