package com.example.alkewallet;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_Login_SingUp_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AlkeWallet);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_singup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void irAlLogin(View view) {
        Intent intent = new Intent(this, Activity_Login_Page.class);
        startActivity(intent);
    }

    public void irASingUp(View view) {
        Intent intent = new Intent(this, Activity_Singup_Page.class);
        startActivity(intent);
    }
}