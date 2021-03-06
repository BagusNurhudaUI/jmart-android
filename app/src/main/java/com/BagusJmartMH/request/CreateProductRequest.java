package com.BagusJmartMH.request;

import com.BagusJmartMH.model.ProductCategory;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * merupakan request yang digunakan untuk ngambil request post mapping product/cretae
 */
public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8084/product/create";
    private final Map<String, String> params;

    public CreateProductRequest(int id, String NameProduct, int WeightProduct, boolean ConditionProduct, double priceProduct, double discountProduct, ProductCategory productCategory, byte shipmentPlans, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(id));
        params.put("name", NameProduct);
        params.put("weight", String.valueOf(WeightProduct));
        params.put("conditionUsed", String.valueOf(ConditionProduct));
        params.put("price", String.valueOf(priceProduct));
        params.put("discount", String.valueOf(discountProduct));
        params.put("category", productCategory.toString());
        params.put("shipmentPlans", String.valueOf(shipmentPlans));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
