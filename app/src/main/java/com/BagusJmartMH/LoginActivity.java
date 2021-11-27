package com.BagusJmartMH;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.request.LoginRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.id_email);
        editPassword = findViewById(R.id.id_password);
        buttonLogin = findViewById(R.id.id_login);
        buttonRegister = findViewById(R.id.button_register);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.id_login){
            String dataEmail = editEmail.getText().toString().trim();
            String dataPassword = editPassword.getText().toString().trim();
            LoginRequest loginRequest = new LoginRequest(dataEmail, dataPassword, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(loginRequest);
        }
        else if(v.getId()==R.id.button_register){
            Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(moveIntent);
        }

    }

    @Override
    public void onResponse(String response) {
        Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
        try{
            JSONObject jsonObject = new JSONObject(response);
            moveIntent.putExtra("id", jsonObject.getInt("id"));
        }catch (Exception e){
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        startActivity(moveIntent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }

    private static class Gson {
    }
}