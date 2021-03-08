package com.example.terminal.model;

interface CarInterface {

    public void setHorsePower(int horsePower);
    public void setMake(String make);
    public String getMake();

}

public class Car implements CarInterface {
    private String make;
    private int horsePower;

    public Car(String make){
        this.make = make;
    }

    @Override
    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    @Override
    public void setMake(String make) {
        this.make = make;
    }

    @Override
    public String getMake() {
        return this.make;
    }

}
