package com.BagusJmartMH;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.request.LoginRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * class yang digunakan untuk menampilkan tampilan login
 * tampilan ini merupakan tampilan awal dari aplikasi
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener {

    private static final Gson gson = new Gson();
    private static Account loggedAccount;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonLogin;
    private TextView buttonRegister;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    /**
     *
     * @param savedInstanceState menyimpan state sebelumnya
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.id_email);
        editPassword = findViewById(R.id.id_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonRegister = findViewById(R.id.button_register_login);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    /**
     * merupakan if condition untuk login atau register
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_login){
            String dataEmail = editEmail.getText().toString().trim();
            String dataPassword = editPassword.getText().toString().trim();
            LoginRequest loginRequest = new LoginRequest(dataEmail, dataPassword, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(loginRequest);
        }
        else if(v.getId()==R.id.button_register_login){
            Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(moveIntent);
        }

    }

    /**
     * digunakan untuk mennyimpan account yang berhasil login
     * akan melanjutkan ke main activity setelah login
     * @param response untuk mendapatkan response dari login
     */
    @Override
    public void onResponse(String response) {
        Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
        try{
            JSONObject jsonObject = new JSONObject(response);
            loggedAccount = gson.fromJson(jsonObject.toString(), Account.class);
        }catch (Exception e){
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        startActivity(moveIntent);
    }

    /**
     * digunakan untuk menangkap error pada saat login
     * @param error untuk menangkap error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(this, "Login Failed Connection", Toast.LENGTH_LONG).show();
    }
}