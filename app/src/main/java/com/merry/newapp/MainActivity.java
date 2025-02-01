package com.merry.newapp;

import android.content.Intent;
import android.content.SharedPreferences; //Kullanıcı tercihlerini kaydetmek ve okumak için.
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; //Uygulamanın temasını ayarlamak için.
import com.google.android.material.bottomnavigation.BottomNavigationView; //Alt menü için.

public class MainActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etGenre, etPages;
    private RatingBar ratingBar;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences ile Dark Mode durumunu kontrol et
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        setContentView(R.layout.activity_main); //xml i bağla

        initializeViews(); // View'ları başlatma metodu
        dbHelper = new DatabaseHelper(this);
        setupBottomNavigation();

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etGenre = findViewById(R.id.et_genre);
        etPages = findViewById(R.id.et_pages);
        ratingBar = findViewById(R.id.ratingBar);
        btnSave = findViewById(R.id.btn_save);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_booky) {
                return true;
            } else if (itemId == R.id.menu_universe) {
                startActivity(new Intent(this, UniverseActivity.class));
                return true;
            } else if (itemId == R.id.menu_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            } else {
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.menu_booky);
    }

    private void saveBook() {
        try {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            int pages = Integer.parseInt(etPages.getText().toString().trim());
            float rating = ratingBar.getRating();

            Book book = new Book(title, author, genre, pages, rating);
            long result = dbHelper.addBook(book);

            if (result != -1) {
                Toast.makeText(this, "Kitap başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Kitap kaydedilemedi", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lütfen tüm alanları doğru formatta doldurun", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etTitle.setText("");
        etAuthor.setText("");
        etGenre.setText("");
        etPages.setText("");
        ratingBar.setRating(0);
    }
}
