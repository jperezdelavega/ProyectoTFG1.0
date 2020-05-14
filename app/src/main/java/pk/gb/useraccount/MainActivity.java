package pk.gb.useraccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_login,btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login=findViewById(R.id.login_button);
        btn_registrar=findViewById(R.id.registrar_button);
    }

    public void Ir_registro(View view) {
        Intent ic = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(ic);
    }
}
