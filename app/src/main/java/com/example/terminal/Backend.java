package com.example.terminal;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.AndroidEntryPoint;

@Singleton
public class Backend {

    public Database database; //Accessor

    @Inject
    public Backend(){}

    public void print(){
        Log.i("Backend", " Injected");
    }

    public void setDatabase(Database database){
        this.database = database;
    }
}

@Singleton
class mongoDB implements Database {

    List<String> data;

    @Inject
    public mongoDB(){
        System.out.println("MongoDB Generated");
        data = new ArrayList<String>();
    }

    @Override
    public void Insert(String data) {
        this.data.add(data);
        System.out.println(data + " added to mongoDB");
    }

    @Override
    public void Delete(String data) {
        if(this.data.contains(data)){
            this.data.remove(data);
            System.out.println(data + " removed from mongoDB");
        }
        else
            System.out.println(data + " not found");
    }

    @Override
    public void Update(String data, String update) {
        if(this.data.contains(data)){
            for(String str : this.data){
                if(str.equals(data))
                    str = update;
            }
            System.out.println(data + " updated to " + update + " in mongoDB");
        }
        else
            System.out.println(data + " not found");
    }
}


@Singleton
class mySQL implements Database {

    List<String> data;

    @Inject
    public mySQL(){
        System.out.println("mySQL Generated");
        data = new ArrayList<String>();
    }

    @Override
    public void Insert(String data) {
        this.data.add(data);
        System.out.println(data + " added to mySQL");
    }

    @Override
    public void Delete(String data) {
        if(this.data.contains(data)){
            this.data.remove(data);
            System.out.println(data + " removed from mySQL");
        }
        else
            System.out.println(data + " not found");
    }

    @Override
    public void Update(String data, String update) {
        if(this.data.contains(data)){
            for(String str : this.data){
                if(str.equals(data))
                    str = update;
            }
            System.out.println(data + " updated to " + update + " in mySQL");
        }
        else
            System.out.println(data + " not found");
    }
}

interface Database{
    void Insert(String data);
    void Delete(String data);
    void Update(String data, String update);
}