package com.BagusJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.request.RegisterStoreRequest;
import com.BagusJmartMH.request.RequestFactory;
import com.BagusJmartMH.request.TopupRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutMeActivity extends AppCompatActivity {
    private Button b1;
    private LinearLayout l2;
    private LinearLayout l3;
    private Button b2;
    private Button b3;
    private EditText edttopup;
    private Button btntopup;
    private Button btnRegisterStore;
    private Button btnCancelStore;
    private EditText NameStore;
    private EditText AddressStore;
    private EditText PhoneStore;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvBalance;
    private TextView tvHasilName;
    private TextView tvHasilAddress;
    private TextView tvHasilPhone;
    private static final Gson gson = new Gson();


    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        btnRegisterStore = findViewById(R.id.id_btn_register_store);
        btnCancelStore = findViewById(R.id.id_btn_cancel_store);
        NameStore = findViewById(R.id.name_register_store);
        AddressStore = findViewById(R.id.address_register_store);
        PhoneStore = findViewById(R.id.Phone_Number_register_store);
        edttopup = findViewById(R.id.edt_topup);
        btntopup = findViewById(R.id.btn_Topup_account_details);
        b1 = findViewById(R.id.btn_register_store);
        l2 = findViewById(R.id.id_form_register_store);
        l3 = findViewById(R.id.hasil_register_store);
        tvName = findViewById(R.id.name_account_details);
        tvEmail = findViewById(R.id.email_account_details);
        tvBalance = findViewById(R.id.balance_account_details);
        tvHasilName = findViewById(R.id.name_store);
        tvHasilAddress = findViewById(R.id.address_store);
        tvHasilPhone = findViewById(R.id.phone_store);
        account = LoginActivity.getLoggedAccount();
        tvName.setText(account.name);
        tvEmail.setText(account.email);
        tvBalance.setText("" + account.balance);
        if (account.store != null){
            b1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.VISIBLE);
            tvHasilName.setText(account.store.name);
            tvHasilAddress.setText(account.store.address);
            tvHasilPhone.setText(account.store.phoneNumber);
        }
        else {
            b1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setVisibility(v.GONE);
                l2.setVisibility(v.VISIBLE);
                Toast.makeText(getApplicationContext(), "Register Store di click", Toast.LENGTH_SHORT).show();
            }
        });

        btntopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean object = Boolean.valueOf(response);
                        if(object){
                            refreshBalance();
                            edttopup.getText().clear();
                            Toast.makeText(AboutMeActivity.this, "Topup Berhasil", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AboutMeActivity.this, "Topup Gagal", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Topup Gagal", Toast.LENGTH_SHORT).show();
                    }
                };
                account = LoginActivity.getLoggedAccount();
                double balance = Double.valueOf(edttopup.getText().toString());
                TopupRequest topupRequest = new TopupRequest(account.id, balance, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(topupRequest);
            }
        });

        btnRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Register Store Sukses", Toast.LENGTH_SHORT).show();
                                l2.setVisibility(v.GONE);
                                l3.setVisibility(v.VISIBLE);
                                account=gson.fromJson(response, Account.class);
                                tvHasilName.setText(account.store.name);
                                tvHasilAddress.setText(account.store.address);
                                tvHasilPhone.setText(account.store.phoneNumber);

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AboutMeActivity.this, "Register Store Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Register Store Gagal", Toast.LENGTH_SHORT).show();
                    }
                };
                account = LoginActivity.getLoggedAccount();
                String name = NameStore.getText().toString();
                String address = AddressStore.getText().toString();
                String phone = PhoneStore.getText().toString();
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, name, address, phone, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(registerStoreRequest);
            }
        });

    }

    public void refreshBalance(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvBalance.setText("" + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutMeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}