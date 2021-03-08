package com.example.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.terminal.socket.Socket;
import com.example.terminal.socket.SocketService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends ToolbarBuilder {

    //Field Injection
//    @Inject
//    SomeClass someClass;
//
//    @Inject
//    ProvidesClass providesClass;
//
//    @Inject
//    mySQL mySQL;
//
//    @Inject
//    mongoDB mongoDB;
//
//    @Inject
//    Backend backend;

    @Inject
    Socket client;

    @Inject
    SocketService socketService;

    @Inject
    Socket socket;

    Button btn, CMD1, CMD2;
    EditText enterPLU;

    @Override
    protected int getLayoutResource() {
        return R.layout.enter_plu;
    }

    @Override
    protected String getToolbarTitle() {
        return "LOG IN";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start_service();
        initialize_layouts();


        /*clientService.restartSocket();

        backend.setDatabase(mySQL);
        backend.database.Insert("User1");
        backend.database.Insert("User2");
        backend.database.Delete("User1");


        Log.e("Log", someClass.doSomething());
        Log.e("Log", someClass.doSomethingElse());
        Log.e("Log", providesClass.doSomething());*/

    }

    private void initialize_layouts(){

        enterPLU = new EditText(this);
        enterPLU = findViewById(R.id.enter_plu);

        btn = new Button(this);
        btn = findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.validateEditText(enterPLU, "PLU must be a length of minimum 6", 6)){
                    Intent intent = new Intent(MainActivity.this, ScalesDisplay.class);
                    startActivity(intent);
                }
            }
        });

        CMD1 = new Button(this);
        CMD1 = findViewById(R.id.CMD1);
        CMD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommandMenu.class);
                startActivity(intent);
            }
        });

        CMD2 = new Button(this);
        CMD2 = findViewById(R.id.CMD2);
        CMD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CommandMenu.class);
                startActivity(intent);
            }
        });
    }

    private void start_service() {
        Intent intent = new Intent(getApplicationContext(), SocketService.class);
        getApplicationContext().startForegroundService(intent);
    }
}

@Singleton
class SomeClass {
    SomeOtherClass someOtherClass;

    @Inject
    public SomeClass(SomeOtherClass someOtherClass) {
        this.someOtherClass = someOtherClass;
    }

    public String doSomething() {
        return "LOOK";
    }

    public String doSomethingElse() {
        return someOtherClass.doSomethingElse();
    }

}


class SomeOtherClass {
    @Inject
    public SomeOtherClass() {
    }

    public String doSomethingElse() {
        return "LOOK ELSE";
    }
}

@Singleton
class ProvidesClass {

    SomeInterfaceImp someInterfaceImp;

    @Inject
    public ProvidesClass(SomeInterfaceImp someInterfaceImp) {
        this.someInterfaceImp = someInterfaceImp;
    }

    public String doSomething() {
        return (this.someInterfaceImp.getSomething());
    }

}

interface someInterface {
    String getSomething();
}

class SomeInterfaceImp implements someInterface {

    public String someDependency = "DEPENDENCY X";

    @Inject
    public SomeInterfaceImp() {
        Log.e("Interface ", "generating");
    }

    @Override
    public String getSomething() {
        return "GOT SOMETHING";
    }
}

//@InstallIn(SingletonComponent.class)
//@Module
//class MyModule {
//
//    @Inject
//    SomeInterfaceImp someInterfaceImp;
//
//    @Singleton
//    @Provides
//    public String provideSomeString() {
//        return someInterfaceImp.someDependency;
//    }
//
//    @Singleton
//    @Provides
//    public someInterface provideSomeInterface() {
//        return someInterfaceImp;
//    }
//
//}



