package com.unccstudio.gems.gems;

import java.io.Serializable;

/**
 * Created by Administrator on 3/24/2015.
 */
public class GEM implements Serializable{
    private String name, macAddress, timeSinceLastMoved, color, note, shelfLifeType;
    private int age; //time since was added
    private int temperature, shelfLife, quantity;
    private boolean attached;

    public GEM() {
        this.name = "";
        this.attached = false;
        this.quantity = 0;
        this.shelfLife = 0;
        this.temperature = 0;
        this.age = 0;
        this.shelfLifeType = "";
        this.note = "";
        this.color = "";
        this.timeSinceLastMoved = "";
        this.macAddress = "";
    }

    public GEM(String name, boolean attached, int quantity, int shelfLife, int temperature, int age, String shelfLifeType, String note, String color, String timeSinceLastMoved, String macAddress) {
        this.name = name;
        this.attached = attached;
        this.quantity = quantity;
        this.shelfLife = shelfLife;
        this.temperature = temperature;
        this.age = age;
        this.shelfLifeType = shelfLifeType;
        this.note = note;
        this.color = color;
        this.timeSinceLastMoved = timeSinceLastMoved;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getShelfLifeType() {
        return shelfLifeType;
    }

    public void setShelfLifeType(String shelfLifeType) {
        this.shelfLifeType = shelfLifeType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTimeSinceLastMoved() {
        return timeSinceLastMoved;
    }

    public void setTimeSinceLastMoved(String timeSinceLastMoved) {
        this.timeSinceLastMoved = timeSinceLastMoved;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
