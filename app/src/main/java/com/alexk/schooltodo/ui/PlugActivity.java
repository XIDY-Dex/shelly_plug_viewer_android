package com.alexk.schooltodo.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexk.schooltodo.R;
import com.alexk.schooltodo.domain.PlugViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PlugActivity extends AppCompatActivity {
    TextView plugName, power, overPower, total, waitText;
    ImageView plugImage;
    CircularProgressIndicator progressIndicator;
    PlugViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        plugName = findViewById(R.id.plugName);
        power = findViewById(R.id.powerText);
        overPower = findViewById(R.id.overPowerText);
        total = findViewById(R.id.totalPowerConsumped);
        progressIndicator = findViewById(R.id.loadingBar);
        plugImage = findViewById(R.id.plugImage);
        waitText = findViewById(R.id.waitText);

        viewModel = new ViewModelProvider(this).get(PlugViewModel.class);
        viewModel.isLoading().observe(this, state -> {
            if(state == false) {
                progressIndicator.setVisibility(View.INVISIBLE);
                waitText.setVisibility(View.INVISIBLE);
                power.setVisibility(View.VISIBLE);
                overPower.setVisibility(View.VISIBLE);
                total.setVisibility(View.VISIBLE);
                plugImage.setVisibility(View.VISIBLE);
            }
        });
        viewModel.plugInfo().observe(this, plugInfo -> {
            if(plugInfo != null) {
                power.setText("Напряжение: " + plugInfo.power);
                overPower.setText("Превышение нормы: " + plugInfo.overpower);
                total.setText("Всего затрачено энергии (Ватт/мин): " + plugInfo.total);
            }
        });
        viewModel.errorStatus().observe(this, errorStatus -> {
            if(errorStatus) {
                ShowErrorDialog();
            }
        });
        int plugId = (int) getIntent().getExtras().get("plugId");
        plugName.setText(viewModel.getPlugName(plugId));
        viewModel.getPlugInfo(plugId);
        viewModel.startPollPlug(plugId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.endPollPlug();
    }

    @Override
    protected void onPause(){
        super.onPause();
        viewModel.endPollPlug();
    }

    private void ShowErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка!");

        View dialogView = getLayoutInflater().inflate(R.layout.plug_error_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("Ок", (dialogInterface, i) -> finish());

        builder.create().show();
    }
}