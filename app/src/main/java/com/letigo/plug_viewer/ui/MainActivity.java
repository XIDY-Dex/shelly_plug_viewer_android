package com.letigo.plug_viewer.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.alexk.schooltodo.R;
import com.letigo.plug_viewer.data.db.PlugEntity;
import com.letigo.plug_viewer.domain.PlugListAdapter;
import com.letigo.plug_viewer.domain.PlugListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    PlugListViewModel viewModel;
    RecyclerView plugListView;
    FloatingActionButton newPlugButton;
    PlugEntity selectedPlug = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plugListView = findViewById(R.id.plugList);
        registerForContextMenu(plugListView);
        viewModel = new ViewModelProvider(this).get(PlugListViewModel.class);
        PlugListAdapter.OnPlugClick onPlugClick = new PlugListAdapter.OnPlugClick() {
            @Override
            public void onPlugClick(PlugEntity plug, int position) {
                Intent intent = new Intent(getApplicationContext(), PlugActivity.class);
                intent.putExtra("plugId", plug.plugId);
                startActivity(intent);
            }
        };
        PlugListAdapter.onPlugLongClick onPlugLongClick = new PlugListAdapter.onPlugLongClick() {
            @Override
            public void onPlugLongClick(PlugEntity plug, int position) {
                selectedPlug = plug;
                MainActivity.this.openContextMenu(plugListView);
            }
        };
        PlugListAdapter adapter = new PlugListAdapter(viewModel.plugs().getValue(), onPlugClick, this, onPlugLongClick);
        plugListView.setAdapter(adapter);
        registerForContextMenu(plugListView);
        viewModel.plugs().observe(this, plugs -> {
            adapter.updatePlugs(plugs);
        });
        newPlugButton = findViewById(R.id.newPlugButton);
        newPlugButton.setOnClickListener(view -> { showNewPlugDialog(viewModel); });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.plug_item_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteOption) {
            viewModel.deletePlug(selectedPlug);
            selectedPlug = null;
            return true;
        }
        else if(item.getItemId() == R.id.updateOption) {
            updatePlugData(viewModel);
            return true;
        }
        return super.onContextItemSelected(item);
    }



    public void showNewPlugDialog(PlugListViewModel viewModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подключение новой розетки");

        View dialogView = getLayoutInflater().inflate(R.layout.new_plug_dialog, null);
        TextInputEditText nameText, addressText;
        nameText = dialogView.findViewById(R.id.nameText);
        addressText = dialogView.findViewById(R.id.addressText);
        builder.setView(dialogView);

        builder.setCancelable(true);
        builder.setPositiveButton("Добавить", (dialogInterface, i) -> {
            PlugEntity entity = new PlugEntity();
            entity.plugName = nameText.getText().toString();
            entity.address = addressText.getText().toString();
            viewModel.addPlugs(entity);
            selectedPlug = null;
        });

        builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create().show();
    }

    public void updatePlugData(PlugListViewModel viewModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Изменение розетки");

        View dialogView = getLayoutInflater().inflate(R.layout.new_plug_dialog, null);
        TextInputEditText nameText, addressText;
        nameText = dialogView.findViewById(R.id.nameText);
        addressText = dialogView.findViewById(R.id.addressText);
        builder.setView(dialogView);

        builder.setCancelable(true);
        builder.setPositiveButton("Изменить", (dialogInterface, i) -> {

            selectedPlug.plugName = nameText.getText().toString();
            selectedPlug.address = addressText.getText().toString();
            viewModel.updatePlug(selectedPlug);
        });

        builder.setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create().show();
    }
}