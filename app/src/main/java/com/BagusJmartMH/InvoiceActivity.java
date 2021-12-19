package com.BagusJmartMH;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.model.Payment;
import com.BagusJmartMH.request.InvoiceRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * class untuk menjalankan invoice applikasi
 */
public class InvoiceActivity extends AppCompatActivity {

    private TabLayout tlInvoice;
    private RecyclerView rvAccount, rvStore;
    public static final Gson gson = new Gson();
    private ArrayList<Payment> AccountP = new ArrayList<>();
    private ArrayList<Payment> StoreP = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();


    /**
     * metod on create untuk menjalankan activity dan xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        tlInvoice = findViewById(R.id.layout_tab_invoice);
        getAccountPaymentList();
        getStorePaymentList();
        rvAccount = findViewById(R.id.rv_account);
        rvStore = findViewById(R.id.rv_store);

        /**
         * listener untuk mengganti tab layout didalam invoice activity
         */
        tlInvoice.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        rvAccount.setVisibility(View.VISIBLE);
                        rvStore.setVisibility(View.GONE);
                        break;
                    case 1:
                        rvAccount.setVisibility(View.GONE);
                        rvStore.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        showRecycler();

    }

    private void showRecycler(){
    }


    /**
     * digunakan untuk mengambil informasi dari account json dan menampilkan didalam
     */
    private void getAccountPaymentList(){
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener

            @Override
            public void onResponse(String response) {
                try {
                    AccountP.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();
                    AccountP = gson.fromJson(response, paymentListType);
                    rvAccount.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceAccAdapt invoiceAccountAdapter = new InvoiceAccAdapt(AccountP);
                    rvAccount.setAdapter(invoiceAccountAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest InvoiceRequest = new InvoiceRequest(account.id, true, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(InvoiceRequest);
    }

    /**
     * digunakan untuk mendapatkan data payment list
     */
    private void getStorePaymentList(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    StoreP.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();
                    StoreP = gson.fromJson(response, paymentListType);
                    rvStore.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
                    InvoiceStoreAdapt invoiceStoreAdapter = new InvoiceStoreAdapt(StoreP);
                    rvStore.setAdapter(invoiceStoreAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, false, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(invoiceRequest);
    }
}