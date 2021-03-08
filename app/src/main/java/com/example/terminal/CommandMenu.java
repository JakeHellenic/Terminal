package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class CommandMenu extends ToolbarBuilder {

    TextView toolbarTitle;

    @Override
    protected int getLayoutResource() {
        return R.layout.command_menu;
    }

    @Override
    protected String getToolbarTitle() {
        return "COMMAND VIEW";
    }

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back = new Button(getApplicationContext());
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}