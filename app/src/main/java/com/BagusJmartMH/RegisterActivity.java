package com.BagusJmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.BagusJmartMH.request.RegisterRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * merupakan class yang digunakan untuk membuat account
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState menyimpan hasil state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText edtNameRegister = findViewById(R.id.name_register);
        EditText edtEmailRegister = findViewById(R.id.email_register);
        EditText edtPasswordRegister = findViewById(R.id.password_register);
        Button btnRegister = findViewById(R.id.button_register);

        /**
         * proses saat button register diclick
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent); //langsung mengganti ke activity login untuk login
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Failed Bro", Toast.LENGTH_SHORT).show();
                    }
                };
                String name = edtNameRegister.getText().toString();
                String email = edtEmailRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString();
                RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}
