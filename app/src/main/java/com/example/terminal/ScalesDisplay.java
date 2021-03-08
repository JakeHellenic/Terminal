package com.example.terminal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.terminal.socket.Socket;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;

@AndroidEntryPoint
public class ScalesDisplay extends ToolbarBuilder {

    Button change_layout, change_layout1;
    LinearLayout single_weight, double_weight;


    @Inject
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        change_layout = findViewById(R.id.change_layout);
        change_layout1 = findViewById(R.id.change_layout1);

        single_weight = findViewById(R.id.single_weight);
        double_weight = findViewById(R.id.double_weight);


        single_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                single_weight.setVisibility(View.GONE);
                double_weight.setVisibility(View.VISIBLE);
            }
        });

        double_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double_weight.setVisibility(View.GONE);
                single_weight.setVisibility(View.VISIBLE);
            }
        });

//        change_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                /*socket.new send("Send", "FOPS");
//                socket.new parentSend("ParentSend");
//                socket.new send("Send", "FOPS");
//                socket.new parentSend("ParentSend");
//                socket.new send("Send", "FOPS");
//                socket.new parentSend("ParentSend");
//                socket.new send("Send", "FOPS");
//                socket.new parentSend("ParentSend");
//                socket.new send("Send", "FOPS");
//                socket.new parentSend("ParentSend");*/
//
//                single_weight.setVisibility(View.GONE);
//                double_weight.setVisibility(View.VISIBLE);
//            }
//        });
//
//        change_layout1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                single_weight.setVisibility(View.VISIBLE);
//                double_weight.setVisibility(View.GONE);
//            }
//        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.scales_display;
    }

    @Override
    protected String getToolbarTitle() {
        return "ADD TO STOCK";
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}