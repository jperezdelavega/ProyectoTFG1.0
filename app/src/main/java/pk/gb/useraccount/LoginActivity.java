package pk.gb.useraccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.MenuActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btn_login,btn_registrar;
    EditText campo_email, campo_pass;
    String URL = "http://3.15.228.207/connect/login.php";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (cargarUsuario()){
            Intent in = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(in);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login=findViewById(R.id.login_button);
        btn_registrar=findViewById(R.id.registrar_button);
        campo_email = findViewById(R.id.email_login);
        campo_pass = findViewById(R.id.pass_login);
        requestQueue = Volley.newRequestQueue(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("LOGGED")){
                            guardarUsuario(campo_email);
                            Intent in = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(in);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email",campo_email.getText().toString());
                        params.put("pass",campo_pass.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });
    }

    public boolean cargarUsuario() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        String user = preferences.getString("user","null");

        return !(user.equals("null"));

    }

    public void guardarUsuario(EditText campo_email) {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",campo_email.getText().toString());
        editor.commit();
    }

    public void Ir_registro(View view) {
        Intent ic = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(ic);
    }
}
