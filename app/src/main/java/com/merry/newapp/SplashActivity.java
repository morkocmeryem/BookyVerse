package com.merry.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity; //Android aktiviteleri için temel sınıf.

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); //xml i bağla

        TextView textSplash = findViewById(R.id.textSplash);
        String bookyVerse = "BookyVerse"; //animasyonlu metin

        animateText(textSplash, bookyVerse);
    }
   //animateText metodu ile metni animasyonlu gösterme
    private void animateText(TextView textView, String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < text.length(); i++) {
            int finalI = i;
            handler.postDelayed(() -> {
                textView.setText(textView.getText().toString() + text.charAt(finalI));

                if (finalI == text.length() - 1) {
                    // Harfler tamamlandıktan sonra 1 saniye bekle ve diğer ekrana geç
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1000);
                }
            }, 300 * i); // Harfler arası gecikme
        }
    }
}
