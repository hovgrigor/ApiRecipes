package Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rustam.apirecipes.R;

import Classes.User;

public class Login extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private boolean preventSecondClickReg = false;
    private boolean preventSecondClickLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        Button b_login = findViewById(R.id.b_login);
        Button b_signUp = findViewById(R.id.b_signUp);

        b_signUp.setOnClickListener(v -> {
            if(!preventSecondClickReg) {
                preventSecondClickReg = true;
                Intent myIntent = new Intent(this, Register.class);
                startActivity(myIntent);
            }
        });

        b_login.setOnClickListener(v -> {
            if(!preventSecondClickLogin){
                preventSecondClickLogin = true;
                //TODO: Add verification
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                User loginUser = User.CreateUser("",email,password);

            }
        });
    }


}
