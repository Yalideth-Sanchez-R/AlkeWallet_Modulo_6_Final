package com.example.alkewallet;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class Activity_Profile_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Picasso para la imagen de perfil
        ImageView ivFotoAmanda = findViewById(R.id.ivFotoAmanda);

        if (ivFotoAmanda != null) {
            String urlImagenPerfil = "https://picsum.photos";

            Picasso.get()
                    .load(urlImagenPerfil)
                    .memoryPolicy(com.squareup.picasso.MemoryPolicy.NO_CACHE, com.squareup.picasso.MemoryPolicy.NO_STORE)
                    .networkPolicy(com.squareup.picasso.NetworkPolicy.NO_CACHE)
                    .into(ivFotoAmanda);
        }
    }

    public void volverAlHome(View view) {
        finish();
    }
}