package pk.gb.useraccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity  {

    Button btn_registro;
    ImageView back_arrow;
    EditText email,pass,user_name,passr;
    SecretKey secretKey;
    String URL = "http://3.15.228.207/connect/registrar.php";
    RequestQueue requestQueue;

    JsonObjectRequest json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//Definicion de los campos que interactuamos
        btn_registro = (Button) findViewById(R.id.joinus);
        back_arrow = (ImageView) findViewById(R.id.flecha_registro);
        email = (EditText) findViewById(R.id.campo_email);
        pass = (EditText) findViewById(R.id.campo_pass);
        passr = (EditText) findViewById(R.id.campo_repeatpass);
        user_name = (EditText) findViewById(R.id.campo_username);
        requestQueue = Volley.newRequestQueue(this);
        byte[] keyStart = "patata".getBytes();
        secretKey = new SecretKeySpec(keyStart,"AES");

//Lo que hace la activdad

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ds = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(ds);
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }

            private void registro() {
                if(pass.getText().toString().equals(passr.getText().toString()) && esCorreoCorrecto(email.getText().toString())){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("error_usuario")){
                                Toast.makeText(getApplicationContext(),"El usuario ya existe",Toast.LENGTH_LONG).show();
                            }else if(response.equals("error_email")){
                                Toast.makeText(getApplicationContext(),"El correo electrónico ya existe",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                Intent ds = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(ds);
                                finish();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    })
                            // Aqui termina la creacion de la request
                    {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email.getText().toString());
                            params.put("pass", pass.getText().toString());
                            params.put("username",user_name.getText().toString());
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }else{
                    if(esCorreoCorrecto(email.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),"El formato del correo no es correcto", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private boolean esCorreoCorrecto(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }


}
