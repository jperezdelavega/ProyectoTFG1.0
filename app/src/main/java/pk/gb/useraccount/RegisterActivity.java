package pk.gb.useraccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {

    Button btn_registro;
    ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_registro = findViewById(R.id.joinus);
        back_arrow = findViewById(R.id.flecha_registro);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ds = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(ds);
            }
        });
    }

    public void LognIn(View view) {
        Intent ds = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(ds);

    }
}
