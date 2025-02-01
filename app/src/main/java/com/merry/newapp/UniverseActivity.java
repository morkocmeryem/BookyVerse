package com.merry.newapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UniverseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universe);

        TextView tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText("My Universe");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new DatabaseHelper(this);
        List<Book> books = dbHelper.getAllBooks();

        adapter = new BookAdapter(books);
        recyclerView.setAdapter(adapter);
    }

    private class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
        private List<Book> books; //kitapları tutan liste

        public BookAdapter(List<Book> books) {
            this.books = books;
        }

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int position) {
            Book book = books.get(position);  //kitap nesnesini al
            holder.bind(book);  //kitap nesnesini bağla
        }

        @Override
        public int getItemCount() {
            return books.size();
        }

        class BookViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvRating;

            public BookViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_book_title);
                tvRating = itemView.findViewById(R.id.tv_book_rating);

                itemView.setOnClickListener(v -> showBookDialog(getAdapterPosition()));
            }

            public void bind(Book book) {
                tvTitle.setText(book.getTitle());
                tvRating.setText(book.getRating() + "/5");
            }
        }
    }

    private void showBookDialog(int position) {
        Book selectedBook = dbHelper.getBookAtPosition(position);

        if (selectedBook == null) {
            Toast.makeText(this, "Kitap bulunamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_delete, null); //xml
        EditText etTitle = dialogView.findViewById(R.id.et_title);
        EditText etAuthor = dialogView.findViewById(R.id.tv_author);
        EditText etGenre = dialogView.findViewById(R.id.et_genre);
        EditText etPages = dialogView.findViewById(R.id.et_pages);
        RatingBar rbRating = dialogView.findViewById(R.id.rb_rating);
        Button btnUpdate = dialogView.findViewById(R.id.btn_update);
        Button btnDelete = dialogView.findViewById(R.id.btn_delete);

        // diaolg içeriğini doldur
        etTitle.setText(selectedBook.getTitle());
        etAuthor.setText(selectedBook.getAuthor());
        etGenre.setText(selectedBook.getGenre());
        etPages.setText(String.valueOf(selectedBook.getPages()));
        rbRating.setRating(selectedBook.getRating());

        AlertDialog.Builder builder = new AlertDialog.Builder(UniverseActivity.this);
        builder.setTitle("Kitap Güncelle");
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            selectedBook.setTitle(etTitle.getText().toString());
            selectedBook.setAuthor(etAuthor.getText().toString());
            selectedBook.setGenre(etGenre.getText().toString());
            selectedBook.setPages(Integer.parseInt(etPages.getText().toString()));
            selectedBook.setRating(rbRating.getRating());

            if (dbHelper.updateBook(selectedBook)) {
                Toast.makeText(UniverseActivity.this, "Kitap Güncellendi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refreshBooks();
            } else {
                Toast.makeText(UniverseActivity.this, "Güncelleme Başarısız", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (dbHelper.deleteBook(selectedBook.getId())) {
                Toast.makeText(UniverseActivity.this, "Kitap Silindi", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refreshBooks();
            } else {
                Toast.makeText(UniverseActivity.this, "Silme Başarısız", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void refreshBooks() {
        List<Book> books = dbHelper.getAllBooks();
        adapter = new BookAdapter(books); //adapter nesnesini yeniden oluştur
        recyclerView.setAdapter(adapter);
    }
}
