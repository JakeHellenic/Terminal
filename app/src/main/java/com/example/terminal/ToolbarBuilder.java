package com.example.terminal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.terminal.socket.Socket;
import com.example.terminal.socket.SocketService;

import org.w3c.dom.Text;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;

@AndroidEntryPoint
public abstract class ToolbarBuilder extends AppCompatActivity{

    @Inject
    Socket socket;

    @Inject
    SocketService socketService;

    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView wifiSuccess, wifiFailed;
    private ProgressBar wifiProgress;

    private androidx.lifecycle.Observer connectedObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer connectionState) {
            Log.e("Connection State", connectionState.toString());

            switch (connectionState){
                case 0:  // No connection
                    Toast.makeText(getApplicationContext(), "Connection not available", Toast.LENGTH_SHORT).show();
                    wifiSuccess.setVisibility(View.GONE);
                    wifiProgress.setVisibility(View.GONE);
                    wifiFailed.setVisibility(View.VISIBLE);
                    break;
                case 1: //Attempting connection
                    Toast.makeText(getApplicationContext(), "Attempting connection..", Toast.LENGTH_SHORT).show();
                    wifiProgress.setVisibility(View.VISIBLE);
                    wifiFailed.setVisibility(View.GONE);
                    //wifiSuccess.setVisibility(View.GONE);
                    break;
                case 2: //Connected
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                    wifiFailed.setVisibility(View.GONE);
                    wifiProgress.setVisibility(View.GONE);
                    wifiSuccess.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        createToolbar();
        socket.hasConnected.observeForever(connectedObserver);
    }

    public void createToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getToolbarTitle());
        wifiSuccess = new ImageView(this);
        wifiProgress = new ProgressBar(this);
        wifiFailed = new ImageView(this);
        wifiProgress = findViewById(R.id.wifi_progress);
        wifiSuccess = findViewById(R.id.wifi_success);
        wifiFailed = findViewById(R.id.wifi_failed);
        wifiFailed .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.hasConnected.setValue(1);
                socketService.restartSocket();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.hasConnected.removeObserver(connectedObserver);
    }

    protected abstract int getLayoutResource();
    protected abstract String getToolbarTitle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        setSupportActionBar(toolbar);

    }

    public void buildToolbar(){



//
//        this.wifiFailed =  findViewById(R.id.wifi_success);
//        this.wifiSuccess =  findViewById(R.id.wifi_success);
//        this.wifiProgress = findViewById(R.id.wifi_progress);
//
//
//
//        socket.hasConnected.observeForever(connectedObserver);
    }






}
