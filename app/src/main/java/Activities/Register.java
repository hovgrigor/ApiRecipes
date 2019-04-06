package Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.rustam.apirecipes.R;

import Classes.User;

public class Register extends AppCompatActivity {

    private String username;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText et_username = findViewById(R.id.et_usernameReg);
        EditText et_email = findViewById(R.id.et_emailReg);
        EditText et_password = findViewById(R.id.et_passReg);
        Button b_register = findViewById(R.id.b_register);

        b_register.setOnClickListener(v -> {
            //TODO: Send To DataBase
            username = et_username.getText().toString();
            email = et_email.getText().toString();
            password = et_password.getText().toString();
            User regUser = User.CreateUser(username,email,password);

        });
    }
}
