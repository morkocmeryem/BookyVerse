package com.merry.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bookyverse.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_PAGES = "pages";
    public static final String COLUMN_RATING = "rating";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_AUTHOR + " TEXT NOT NULL, " +
                    COLUMN_GENRE + " TEXT NOT NULL, " +
                    COLUMN_PAGES + " INTEGER NOT NULL, " +
                    COLUMN_RATING + " FLOAT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Kitap ekleme
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_GENRE, book.getGenre());
        values.put(COLUMN_PAGES, book.getPages());
        values.put(COLUMN_RATING, book.getRating());
        return db.insert(TABLE_BOOKS, null, values);
    }

    // Kitap güncelleme
    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_GENRE, book.getGenre());
        values.put(COLUMN_PAGES, book.getPages());
        values.put(COLUMN_RATING, book.getRating());
        int rowsUpdated = db.update(TABLE_BOOKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        return rowsUpdated > 0; // Güncelleme başarılıysa true döndür
    }

    // Kitap silme
    public boolean deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_BOOKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase(); // Veritabanını okuma modunda aç
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getFloat(5)
                );
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    public Book getBookAtPosition(int position) {
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS + " LIMIT 1 OFFSET " + position;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getFloat(5)
            );
            cursor.close();
            return book;
        }
        return null;
    }
}