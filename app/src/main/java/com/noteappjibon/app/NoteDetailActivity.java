package com.noteappjibon.app;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText;
    private DatabaseHelper databaseHelper;
    private boolean isNewNote;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        
        databaseHelper = new DatabaseHelper(this);
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        
        isNewNote = getIntent().getBooleanExtra("isNewNote", true);
        
        if (!isNewNote) {
            noteId = getIntent().getIntExtra("noteId", -1);
            loadNote(noteId);
        }
        
        findViewById(R.id.saveButton).setOnClickListener(v -> saveNote());
        findViewById(R.id.deleteButton).setOnClickListener(v -> deleteNote());
        
        if (isNewNote) {
            findViewById(R.id.deleteButton).setVisibility(View.GONE);
        }
    }

    private void loadNote(int id) {
        Note note = databaseHelper.getNote(id);
        titleEditText.setText(note.getTitle());
        contentEditText.setText(note.getContent());
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        
        if (title.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        
        Note note = new Note(title, content, timestamp);
        
        if (isNewNote) {
            long id = databaseHelper.addNote(note);
            if (id != -1) {
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            note.setId(noteId);
            databaseHelper.updateNote(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void deleteNote() {
        databaseHelper.deleteNote(noteId);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
