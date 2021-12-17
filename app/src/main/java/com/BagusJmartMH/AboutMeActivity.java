package com.BagusJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.model.Store;
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
    private Button btnRegist;
    private LinearLayout formRegist;
    private LinearLayout hasilStore;
    private EditText edittopup;
    private Button btntopup;
    private Button invoiceBtn;
    private Button afterBtnRegisterStore;
    private Button afterBtnCancelStore;
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

    /**
     *
     * @param savedInstanceState digunakan untuk mengambil  file xml yang ditautkan
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        //inisialisasi
        invoiceBtn = findViewById(R.id.invoice);
        afterBtnRegisterStore = findViewById(R.id.id_btn_register_store);
        afterBtnCancelStore = findViewById(R.id.id_btn_cancel_store);
        NameStore = findViewById(R.id.name_register_store);
        AddressStore = findViewById(R.id.address_register_store);
        PhoneStore = findViewById(R.id.Phone_Number_register_store);
        edittopup = findViewById(R.id.edt_topup);
        btntopup = findViewById(R.id.btn_Topup_account_details);
        btnRegist = findViewById(R.id.btn_register_store);
        formRegist = findViewById(R.id.id_form_register_store);
        hasilStore = findViewById(R.id.hasil_register_store);
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

        /**
         * check account store sudah dibuat atau belum
         * jika belum maka tidak akan menampilkan linear layout form register
         */
        if (account.store != null){
            btnRegist.setVisibility(View.GONE);
            formRegist.setVisibility(View.GONE);
            hasilStore.setVisibility(View.VISIBLE);
            tvHasilName.setText(account.store.name);
            tvHasilAddress.setText(account.store.address);
            tvHasilPhone.setText(account.store.phoneNumber);
        }
        else {
            btnRegist.setVisibility(View.VISIBLE);
            formRegist.setVisibility(View.GONE);
            hasilStore.setVisibility(View.GONE);
        }

        /**
         * digunakan untuk register store dan menampilkan form register
         */
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegist.setVisibility(v.GONE);
                formRegist.setVisibility(v.VISIBLE);
                Toast.makeText(getApplicationContext(), "Register Store di click", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * digunakan untuk menambahkan balance
         */
        btntopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean object = Boolean.valueOf(response);
                        if(object){
                            refreshBalance();
                            edittopup.getText().clear();
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
                double balance = Double.valueOf(edittopup.getText().toString());
                TopupRequest topupRequest = new TopupRequest(account.id, balance, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(topupRequest);
            }
        });

        /**
         * menampilkan form setelah button register di click
         */
        afterBtnRegisterStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null) {
                                Toast.makeText(AboutMeActivity.this, "Register Store Berhasil", Toast.LENGTH_SHORT).show();
                            }
                                account.store = gson.fromJson(object.toString(), Store.class);
                                formRegist.setVisibility(v.GONE);
                                hasilStore.setVisibility(v.VISIBLE);
                                tvHasilName.setText(account.store.name);
                                tvHasilAddress.setText(account.store.address);
                                tvHasilPhone.setText(account.store.phoneNumber);

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

        /**
         * menampilkan proses ketika membatalkan untuk membuat store
         */
        afterBtnCancelStore.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol cancel pada cardview yang muncul setelah tombol registerStore ditekan
            @Override
            public void onClick(View view) {
                hasilStore.setVisibility(View.GONE);
                btnRegist.setVisibility(View.VISIBLE);
            }
        });

        /**
         * digunakan untuk menampilkan invoice setelah button di click
         */
        invoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(AboutMeActivity.this, InvoiceActivity.class);
                Toast.makeText(getApplicationContext(), "Invoice Button clicked", Toast.LENGTH_SHORT).show();
                startActivity(moveIntent);
            }
        });
    }


    /**
     * digunakan untuk memperbarui balance yang telah diubah atau ditambahkan
     */

    public void refreshBalance(){
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener untuk mengambil request
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvBalance.setText("Rp " + account.balance);
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

    /**
     * method untuk memanggil function refresh balance secara terus menerus
     */
    @Override
    protected void onResume() {
        refreshBalance();
        super.onResume();
    }
}