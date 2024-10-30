package com.example.contextmenupresentationdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.ChapterAdapter;
import model.Chapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private ActionMode actionMode;
    private List<String> selectedChapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Chapter c1 = new Chapter(R.drawable.ic_launcher_foreground, "Chapter one", "Introduce Android");
        Chapter c2 = new Chapter(R.drawable.ic_launcher_foreground, "Chapter two", "Layout Android");
        Chapter c3 = new Chapter(R.drawable.ic_launcher_foreground, "Chapter three", "Activity Android");

        List<Chapter> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        list.add(c3);

        adapter = new ChapterAdapter(this, list);
        recyclerView = findViewById(R.id.rec_chapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_edit) {
            Log.d("Floating Context Menu", "Edit");
            return true;
        } else if (itemId == R.id.action_delete) {
            Log.d("Floating Context Menu", "Delete");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // Method to start Action Mode
    private void startActionMode() {
        if (actionMode == null) {
            actionMode = startActionMode(actionModeCallback);
        }
    }

    // ActionMode callback
    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
            return true; // Return true to create the action mode
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                mode.finish(); // Close action mode
                return true;
            } else if (itemId == R.id.action_delete) {
                mode.finish(); // Close action mode
                return true;
            }
            return false;
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null; // Clear action mode reference
            selectedChapters.clear(); // Clear selected chapters
            adapter.clearSelections(); // Clear selections in adapter
        }
    };

    // Method to handle item selection
    public void onItemSelected(String chapter, boolean isSelected) {
        if (isSelected) {
            selectedChapters.add(chapter);
        } else {
            selectedChapters.remove(chapter);
        }

        // Start Action Mode if an item is selected
        if (selectedChapters.isEmpty() && actionMode != null) {
            actionMode.finish(); // Finish action mode if no selections
        } else if (actionMode == null) {
            startActionMode(); // Start action mode for the first selection
        }

        // Update action mode title
        if (actionMode != null) {
            actionMode.setTitle(selectedChapters.size() + " selected");
        }
    }
}