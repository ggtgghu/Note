package com.noteappjibon.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> notes;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        databaseHelper = new DatabaseHelper(this);
        
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            intent.putExtra("isNewNote", true);
            startActivity(intent);
        });
        
        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        notes = databaseHelper.getAllNotes();
        if (adapter == null) {
            adapter = new NoteAdapter(notes, new NoteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Note note = notes.get(position);
                    Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                    intent.putExtra("isNewNote", false);
                    intent.putExtra("noteId", note.getId());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(int position) {
                    Note note = notes.get(position);
                    databaseHelper.deleteNote(note.getId());
                    loadNotes();
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
